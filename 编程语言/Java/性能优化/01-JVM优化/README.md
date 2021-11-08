## part-one

- [ ] 了解下我们为什么要学习`JVM`优化
- [ ] 掌握`jvm`的运行参数以及参数的设置
- [ ] 掌握`jvm`的内存模型（堆内存）
- [ ] 掌握`jmap`命令的使用以及通过`MAT`工具进行分析
- [ ] 掌握定位分析内存溢出的方法
- [ ] 掌握 `stack`命令的使用
- [ ] 掌握 `VisualJVM`工具的使用

### 1、我们为什么要对`JVM`做优化

在本地开发环境中我们很少会遇到需要对`jvm`进行优化的需求，但是到了生产环境，我们可能将有下面的需求

- 运行的应用“卡住了↑，日志不输出，程序没有反应
- 服务器的CPU负载突然升高
- 在多线程应用下，如何分配线程的数量

本文中，我将对`jvm`进行更深入的分析，我们不仅要让程序能跑起来，而且是可以跑的更快！可以分析解决在生产环境中所遇到的各种“棘手“的问题

### 2、`jvm`的运行参数

在`jvm`中有很多的参数可以进行设置，这样可以让`jvm`在各种环境中都能够高效的运行。 绝大部分的参数保持默认即可。 

#### 2.1、三种参数类型

`JVM`的参数类型分为三类，分别是:

- 标准参数

```shell
-help
-version
```

- -X参数（非标准参数）

  ```shell
  -Xint
  -Xcomp
  ```

- -XX（参数（使用率较高）

```shell
-XX:newSize
-XX:+UseSerialGC
```



#### 2.2、标准参数

`jvm`的标准参数，一般都是很稳定的，在未来的`JVM`版本中不会改变，可以使用`java -help` 检索出所有的标准参数。

```shell
[root@localhost java]# java -help
用法: java [-options] class [args...]
           (执行类)
   或  java [-options] -jar jarfile [args...]
           (执行 jar 文件)
其中选项包括:
    -d32          使用 32 位数据模型 (如果可用)
    -d64          使用 64 位数据模型 (如果可用)
    -server       选择 "server" VM
                  默认 VM 是 server.
-cp <目录和 zip/jar 文件的类搜索路径>
-classpath <目录和 zip/jar 文件的类搜索路径>
              用 : 分隔的目录, JAR 档案
              和 ZIP 档案列表, 用于搜索类文件。
-D<名称>=<值>
              设置系统属性
-verbose:[class|gc|jni]
              启用详细输出
-version      输出产品版本并退出
-version:<值>
              需要指定的版本才能运行
-showversion  输出产品版本并继续
-jre-restrict-search | -no-jre-restrict-search
              在版本搜索中包括/排除用户专用 JRE
-? -help      输出此帮助消息
-X            输出非标准选项的帮助
-ea[:<packagename>...|:<classname>]
-enableassertions[:<packagename>...|:<classname>]
              按指定的粒度启用断言
-da[:<packagename>...|:<classname>]
-disableassertions[:<packagename>...|:<classname>]
              禁用具有指定粒度的断言
-esa | -enablesystemassertions
              启用系统断言
-dsa | -disablesystemassertions
              禁用系统断言
-agentlib:<libname>[=<选项>]
              加载本机代理库 <libname>, 例如 -agentlib:hprof
              另请参阅 -agentlib:jdwp=help 和 -agentlib:hprof=help
-agentpath:<pathname>[=<选项>]
              按完整路径名加载本机代理库
-javaagent:<jarpath>[=<选项>]
              加载 Java 编程语言代理, 请参阅 java.lang.instrument
-splash:<imagepath>
              使用指定的图像显示启动屏幕
```

##### 2.2.1、实战

**实战1：查看jvm版本**

```shell
[root@localhost java]# java -version
java version "1.8.0_11"
Java(TM) SE Runtime Environment (build 1.8.0_11-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.11-b03, mixed mode)
```

**实战2：通过-D设置系统属性参数**

```java
public class TestJVM {
    public static void main(String[] args) {
        String str = System.getProperty("str");
        if (str == null) {
            System.out.println("没有参数");
        } else {
            System.out.println(str);
        }
    }
}
```

将代码拷贝到`Linux`中

编译：

```shell
javac TestJVM.java
```

测试：

```shell
java -Dstr=lzj TestJVM
```

结果：

![image-20211108213723378](https://gitee.com/JKcoding/imgs/raw/master/img/202111082137239.png)



##### 2.2.2、-server与-client参数

可以通过 `-server`或`-client`设置`jvm`的运行参数。

它们的区别是 :

1. `Server JVM`的初始堆空间会大些，默认使用的是并行垃圾回收器，启动慢运行快
2. `Client JVM`相对来讲会保守一些，初始堆空间会小些，使用串行的垃圾回收器，它的目标是为了让`JVM`的启动速度更快，但运行速度会比 Server模式慢些。

`JVM`在启动的时候会根据硬件和操作系统自动选择使用 `Server`还是 `Client`类型的`JVM`。

​		32位操作系统: 如果是 `Windows`系统，不论硬件配置如何，都默认使用 `Client`类型的`JVM`。

​								如果是其他操作系统上，机器配置有`2GB`以上的内存同时有2个以上CPU的话默认使用 server模式，否则使用 client模式。

​		64位操作系统: 只有 `server`类型，不支持 `client`类型测试



#### 2.3、-X参数

`jvm`的`-X`参数是非标准参数，在不同版本的`jvm`中，参数可能会有所不同，可以通过`java -X`查看非标准参数。

```shell
-Xmixed           混合模式执行 (默认)
-Xint             仅解释模式执行
-Xbootclasspath:<用 : 分隔的目录和 zip/jar 文件>
                  设置搜索路径以引导类和资源
-Xbootclasspath/a:<用 : 分隔的目录和 zip/jar 文件>
                  附加在引导类路径末尾
-Xbootclasspath/p:<用 : 分隔的目录和 zip/jar 文件>
                  置于引导类路径之前
-Xdiag            显示附加诊断消息
-Xnoclassgc       禁用类垃圾收集
-Xincgc           启用增量垃圾收集
-Xloggc:<file>    将 GC 状态记录在文件中 (带时间戳)
-Xbatch           禁用后台编译
-Xms<size>        设置初始 Java 堆大小
-Xmx<size>        设置最大 Java 堆大小
-Xss<size>        设置 Java 线程堆栈大小
-Xprof            输出 cpu 配置文件数据
-Xfuture          启用最严格的检查, 预期将来的默认值
-Xrs              减少 Java/VM 对操作系统信号的使用 (请参阅文档)
-Xcheck:jni       对 JNI 函数执行其他检查
-Xshare:off       不尝试使用共享类数据
-Xshare:auto      在可能的情况下使用共享类数据 (默认)
-Xshare:on        要求使用共享类数据, 否则将失败。
-XshowSettings    显示所有设置并继续
-XshowSettings:all
                  显示所有设置并继续
-XshowSettings:vm 显示所有与 vm 相关的设置并继续
-XshowSettings:properties
                  显示所有属性设置并继续
-XshowSettings:locale
                  显示所有与区域设置相关的设置并继续
-X 选项是非标准选项, 如有更改, 恕不另行通知。
```

##### 2.3.1、-Xmint、-Xcomp、-Xmixed

1. **-Xmint**: 在解释模式（ interpreted mode）下，`-Xmint`标记会强制`jvm`执行所有的字节码，当然这会降低运行速度，通常低10倍或更多。
2. **-Xcomp**:`-Xcomp`参数与它（`-Xmint`）正好相反，`jvm`在第一次使用时会把所有的字节码编译成本地代码，从而带来最大程度的优化, 然而，很多应用在使用`Xcomp`也会有一些性能损失，当然这比使用`-Xmint`损失的少，原因是`-Xcomp`没有让`jvm`启用`JIT`编译器的全部功能。`JIT`编译器可以对是否需要编译做判断，如果所有代码都进行编译的话，对于一些只执行一次的代码就没有意义了。
3. **-Xmixed:**`-Xmixed`是混合模式，将解释模式与编译模式进行混合使用，由`jvm`自己决定，这是`jvm`默认的模式，也是推荐使用的模式。



#### 2.4 、-XX参数

`-XX`参数也是非标准参数，主要用于`jvm`的调优和 `debug`操作

`-XX`参数的使用有2种方式，一种是 `boolean`类型，一种是非 `boolean`类型

​	boolean类型

​		格式: -XX:[+-]，如:-XX:+DisableExplicitGC表示禁用手动调用gc操作，也就是说调用`System.gc()`无效

​	非 boolean类型

​		格式:-XX:  ，如:-XX:NewRatio=1表示新生代和老年代的比值

```shell
java -XX:+DisableExplictitGC TestJVM
```



#### 2.5、 -Xms和-Xmx参数

​	`-Xms`与`-Xmx`分别是设置m的堆内存的初始大小和最大大小。

​	`-Xmx2048m`:等价于`-XX:MaxHeapSize`，设置`jvm`最大堆内存为`2048M`

​	`-Xms512m`:等价于`-XX: InitialHeapSize`，设置`jvm`初始堆内存为`512M`

​	适当的调整`jvm`的内存大小，可以充分利用服务器资源，让程序跑的更快。

```shell
java -Xms512m -Xmx2048m TestJVM
```



#### 2.6、查看jvm的运行参数

有些时候我们需要查看`jvm`的运行参数，这个需求可能会存在2种情况：

第一，运行`java`命令时打印出运行参数； 

第二，查看正在运行的`java`进程的参数；

##### 2.6.1、运行java命令时打印参数

运行`java`命令时打印参数，需要添加`-XX:+PrintFlagsFinal`参数即可。

```shell
java -XX:+PrintFlagsFinal TestJVM
```

![image-20211109000555687](https://gitee.com/JKcoding/imgs/raw/master/img/202111090005563.png)

```shell
java -XX:+PrintFlagsFinal -XX:hashCode=4 TestJVM
```

![image-20211109000735622](https://gitee.com/JKcoding/imgs/raw/master/img/202111090007626.png)



##### 2.6.2、查看正在运行的`jvm`参数

```java
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJVM {
    public static void main(String[] args) throws InterruptedException {
       while (true) {
           Thread.sleep(1000L);
           System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
       }
    }
}
```

编译运行：

```shell
javac TestJVM.java
java TestJVM
```

查看java进程：

```shell
jps
```

![image-20211109001727242](https://gitee.com/JKcoding/imgs/raw/master/img/202111090017634.png)

查看jvm参数：

```shell
jinfo -flags 2381
```

![image-20211109001920564](https://gitee.com/JKcoding/imgs/raw/master/img/202111090019674.png)

```shell
jinfo -flag MaxHeapSize 2381
```

![image-20211109002013926](https://gitee.com/JKcoding/imgs/raw/master/img/202111090020665.png)

