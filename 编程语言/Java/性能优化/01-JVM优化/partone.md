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



### 3、JVM的内存模型

#### 3.1、jdk1.7的堆内存模型

![image-20211113022102011](https://gitee.com/JKcoding/imgs/raw/master/img/202111130221539.png)

- Young年轻区（代）

​	`Young`区被划分为三部分，Eden区和两个大小严格相同的 `Survivor`区，其中`Survivor`区间中，某一时刻只有其中一个是被使用的，另外一个留做垃圾收集时复制对象用，在Eden区间变满的时候，`GC`就会将存活的对象移到空闲的 `Survivor`区间中，根据`JVM`的策略，在经过几次垃圾收集后，任然存活于 `Survivor`的对象将被移动到 `Tenured`区间

- Tenured年老区

​	`Tenured`区主要保存生命周期长的对象，一般是一些老的对象，当一些对象在 `Young`复制转移一定的次数以后，对象就会被转移到 Tenured区，一般如果系统中用了`application`级别的缓存，缓存中的对象往往会被转移到这一区间。

- Perm永久区

​	`Perm`代主要保存`class`， `method`，`filed`对象，这部份的空间一般不会溢出，除非一次性加载了很多的类，不过在涉及到热部署的应用服务器的时候，有时候会遇到`java.lang.OutofMemoryError: PermGen space`的错误，造成这个错误的很大原因就有可能是每次都重新部署，但是重新部署后，类的`class`没有被卸载掉，这样就造成了大量的`class`对象保存在了`perm`中，这种情况下，一般重新启动应用服务器可以解决问题。

-  Virtual区

​	最大内存和初始内存的差值，就是 `Virtual`区。



#### 3.2、jdk1.8的堆内存模型

![image-20211113143226408](https://gitee.com/JKcoding/imgs/raw/master/img/202111131432854.png)

由上图可以看出，jdk1.8的内存模型是由2部分组成，**年轻代+年老代**

**年轻代**: `Eden + 2 * Survivor`

**年老代**: `old Gen`

在`jdk1.8`中变化最大的`Perm`区，用 `Metaspace`（元数据空间）进行了替换。

需要特别说明的是: `Metaspace`所占用的内存空间不是在虚拟机内部，而是在本地内存空间中，这也是与`jdk1.7`的永久代最大的区别所在

![image-20211113145240254](https://gitee.com/JKcoding/imgs/raw/master/img/202111131452293.png)

#### 3.3、为什么要废弃1.7中的永久区

官网给出的解释：[http://openjdk.java.net/jeps/122](http://openjdk.java.net/jeps/122)

> This is part of the JRockit and Hotspot convergence effort. J Rockit
>
> customers do not need to configure the permanent generation （since JRockit
>
> does not have a permanent generation） and are accustomed to not
>
> configuring the permanent generation
>
> 移除永久代是为融合 Hotspot JVM与 JRockit VM而做出的努力，因为 JRockit没有永久代，
>
> 不需要配置永久代。

现实使用中，由于永久代内存经常不够用或发生内存泄露，爆出异常 `java.lang.OutOfMemoryError: PermGen`

基于此，将永久区废弃，而改用元空间，改为了使用本地内存空间。



#### 3.4、通过jstat命令查看堆内存使用情况

jstat命令可以查看堆内存各部分的使用量，以及加载类的数量。

命令格式：`jstat [-命令选项] [进程ID] [间隔时间/毫秒] [查询次数]`

##### 3.4.1、查看class加载统计

![image-20211113151524052](https://gitee.com/JKcoding/imgs/raw/master/img/202111131515607.png)

说明：

- `Loaded`：加载`class`的数量 
- `Bytes`：所占用空间大小 
- `Unloaded`：未加载数量 
- `Bytes`：未加载占用空间 
- `Time`：时间

##### 3.4.2、查看编译统计

![image-20211113153029037](https://gitee.com/JKcoding/imgs/raw/master/img/202111131530758.png)

说明：

- `Compiled`：编译数量。 
- `Failed`：失败数量 
- `Invalid`：不可用数量 
- `Time`：时间 
- `FailedType`：失败类型 
- `FailedMethod`：失败的方法 

##### 3.4.3、垃圾回收统计

```shell
[root@localhost ~]# jps
1687 TestJVM
1758 Jps
[root@localhost ~]# jstat -gc 1687
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
128.0  128.0   0.0    38.4   1024.0   847.3    60160.0     565.6    4864.0 3542.0 512.0  372.1       3    0.004   0      0.000    0.004
[root@localhost ~]# jstat -gc 1687 1000 5 //每隔1秒输出一次，总共5次
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
128.0  128.0   0.0    38.4   1024.0   927.9    60160.0     565.6    4864.0 3542.0 512.0  372.1       3    0.004   0      0.000    0.004
128.0  128.0   0.0    38.4   1024.0   927.9    60160.0     565.6    4864.0 3542.0 512.0  372.1       3    0.004   0      0.000    0.004
128.0  128.0   0.0    38.4   1024.0   927.9    60160.0     565.6    4864.0 3542.0 512.0  372.1       3    0.004   0      0.000    0.004
128.0  128.0   0.0    38.4   1024.0   927.9    60160.0     565.6    4864.0 3542.0 512.0  372.1       3    0.004   0      0.000    0.004
128.0  128.0   0.0    38.4   1024.0   927.9    60160.0     565.6    4864.0 3542.0 512.0  372.1       3    0.004   0      0.000    0.004
```

![image-20211113153252072](https://gitee.com/JKcoding/imgs/raw/master/img/202111131532395.png)

说明：

- `S0C`：第一个`Survivor`区的大小（KB） 
- `S1C`：第二个`Survivor`区的大小（KB） 
- `S0U`：第一个`Survivor`区的使用大小（KB） 
- `S1U`：第二个`Survivor`区的使用大小（KB） 
- `EC`：`Eden`区的大小（KB） 
- `EU`：`Eden`区的使用大小（KB） 
- `OC`：`Old`区大小（KB） 
- `OU`：`Old`使用大小（KB） 
- `MC`：方法区大小（KB） 
- `MU`：方法区使用大小（KB） 
- `CCSC`：压缩类空间大小（KB） 
- `CCSU`：压缩类空间使用大小（KB） 
- `YGC`：年轻代垃圾回收次数 
- `YGCT`：年轻代垃圾回收消耗时间 
- `FGC`：老年代垃圾回收次数 
- `FGCT`：老年代垃圾回收消耗时间 
- `GCT`：垃圾回收消耗总时间



### 4、jmap的使用以及内存溢出分析

前面通过`jstat`可以对`jvm`堆的内存进行统计分析，而`jmap`可以获取到更加详细的内容， 

如：内存使用情况的汇总、对内存溢出的定位与分析。

#### 4.1、查看内存使用情况

```shell
jps
jmap -heap [进程ID]
```

```java
[root@localhost ~]# jps
1687 TestJVM
2087 Jps
[root@localhost ~]# jmap -heap 1687
Attaching to process ID 1687, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.11-b03

using thread-local object allocation.
Mark Sweep Compact GC

Heap Configuration: //堆内存配置信息
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 994050048 (948.0MB)
   NewSize                  = 1310720 (1.25MB)
   MaxNewSize               = 331350016 (316.0MB)
   OldSize                  = 61603840 (58.75MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage: //堆内存使用情况
New Generation (Eden + 1 Survivor Space):
   capacity = 27787264 (26.5MB)
   used     = 16584256 (15.81597900390625MB)
   free     = 11203008 (10.68402099609375MB)
   59.68293963738208% used
Eden Space:
   capacity = 24707072 (23.5625MB)
   used     = 16584256 (15.81597900390625MB)
   free     = 8122816 (7.74652099609375MB)
   67.12351831896552% used
From Space:
   capacity = 3080192 (2.9375MB)
   used     = 0 (0.0MB)
   free     = 3080192 (2.9375MB)
   0.0% used
To Space:
   capacity = 3080192 (2.9375MB)
   used     = 0 (0.0MB)
   free     = 3080192 (2.9375MB)
   0.0% used
tenured generation:
   capacity = 61603840 (58.75MB)
   used     = 610840 (0.5825424194335938MB)
   free     = 60993000 (58.167457580566406MB)
   0.991561564993351% used

1760 interned Strings occupying 117784 bytes.
```

![image-20211113181811486](https://gitee.com/JKcoding/imgs/raw/master/img/202111131818975.png)



#### 4.2、查看内存中对象数量以及大小

```shell
#查看所有对象，包括活跃以及非活跃的
jmap ‐histo <pid> | more
#查看活跃对象
jmap ‐histo:live <pid> | more
```

![image-20211113182259140](https://gitee.com/JKcoding/imgs/raw/master/img/202111131823737.png)

```shell
#对象说明 
B byte 
C char 
D double 
F float 
I int 
J long 
Z boolean 
[ 数组，如[I表示int[] 
[L+类名 其他对象
```

#### 4.3、将内存使用情况dump到文件

有些时候我们需要将`jvm`当前内存中的情况`dump`到文件中，然后对它进行分析，`jmap`也是支持`dump`到文件中的

```shell
#用法：
jmap ‐dump:format=b,file=dumpFileName <pid>
#示例
jmap ‐dump:format=b,file=/test/dump.dat 6219
```

![image-20211113184450618](https://gitee.com/JKcoding/imgs/raw/master/img/202111131844860.png)

![image-20211113184519996](https://gitee.com/JKcoding/imgs/raw/master/img/202111131845248.png)

可以看到，生成了`dump.dat`文件

#### 4.4、通过jhat对dump文件进行分析

将`jvm`的内存`dump`到文件中，这个文件是一个二进制的文件，不方便查看，这时我们可以借助于`jhat`工具进行查看。 

```shell
#用法：
jhat ‐port <port> <file>
```

![image-20211113195422447](https://gitee.com/JKcoding/imgs/raw/master/img/202111131954574.png)

![image-20211113195533985](https://gitee.com/JKcoding/imgs/raw/master/img/202111131955100.png)

#### 4.5、使用MAT工具对dump文件进行分析

`MAT(Memory Analyzer Tool)`，一个基于Eclipse的内存分析工具，是一个快速、功能丰富的JAVA heap分析工具，它可以帮助我们查找内存泄漏和减少内存消耗。使用内存分析工具从众多的对象中进行分析，快速的计算出在内存中对象的占用大小，看看是谁阻止了垃圾收集器的回收工作，并可以通过报表直观的查看到可能造成这种结果的对象。

官网地址：[https://www.eclipse.org/mat/](https://www.eclipse.org/mat/)

下载：[https://www.eclipse.org/mat/downloads.php](https://www.eclipse.org/mat/downloads.php)

![image-20211113195840520](https://gitee.com/JKcoding/imgs/raw/master/img/202111131958668.png)

![image-20211113195928993](https://gitee.com/JKcoding/imgs/raw/master/img/202111131959183.png)

![image-20211113195951976](https://gitee.com/JKcoding/imgs/raw/master/img/202111131959117.png)

![image-20211113200049638](https://gitee.com/JKcoding/imgs/raw/master/img/202111132000729.png)

![image-20211113200405947](https://gitee.com/JKcoding/imgs/raw/master/img/202111132004507.png)

![image-20211113200516184](https://gitee.com/JKcoding/imgs/raw/master/img/202111132005346.png)

查看对象以及它的依赖：

![image-20211113200603824](https://gitee.com/JKcoding/imgs/raw/master/img/202111132006133.png)

查看可能存在内存泄漏的分析：

![image-20211113200700441](https://gitee.com/JKcoding/imgs/raw/master/img/202111132007374.png)



### 5、实战：内存溢出的定位与分析

​	内存溢出在实际的生产环境中经常会遇到，比如，不断的将数据写入到一个集合中，出现了死循环，读取超大的文件等等，都可能会造成内存溢出。如果出现了内存溢出，首先我们需要定位到发生内存溢出的环节，并且进行分析，是正常还是非正常情况，如果是正常的需求，就应该考虑加大内存的设置，如果是非正常需求，那么就要对代码进行修改，修复这个`bug`。首先，我们得先学会如何定位问题，然后再进行分析。如何定位问题呢，我们需要借助于`jmap`与`MAT`工具进行定位分析。接下来，我们模拟内存溢出的场景。

#### 5.1、模拟内存溢出

编写代码，向List集合中添加100万个字符串，每个字符串由1000个UUID组成。如果程序能够正常执行，最后打印ok。

```java
public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < 1000000; i++) {
        String str = "";
        for (int j = 0; j < 1000; j++) {
            str += UUID.randomUUID().toString();
        }
        list.add(str);
    }
    System.out.println("ok");
}
```

设置jvm参数：

![image-20211113202409207](https://gitee.com/JKcoding/imgs/raw/master/img/202111132024139.png)

```shell
#参数如下
-Xms8m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError
```

#### 5.2、运行测试

![image-20211113202529583](https://gitee.com/JKcoding/imgs/raw/master/img/202111132025983.png)

可以看到，当发生内存溢出时，会dump文件生成到`java_pid12332.hprof`

#### 5.3、导入到MAT工具中进行分析

![image-20211113202731289](https://gitee.com/JKcoding/imgs/raw/master/img/202111132027581.png)

点击`detail`查看详情：

![image-20211113204224297](https://gitee.com/JKcoding/imgs/raw/master/img/202111132042322.png)

可以看到集合中存储了大量的`uuid`字符串



### 6、jstack的使用

​	有些时候我们需要查看下`jvm`中的线程执行情况，比如，发现服务器的`CPU`的负载突然增高了、出现了死锁、死循环等，我们该如何分析呢？由于程序是正常运行的，没有任何的输出，从日志方面也看不出什么问题，所以就需要看下`jvm`的内部线程的执行情况，然后再进行分析查找出原因。这个时候，就需要借助于`jstack`命令了，`jstack`的作用是将正在运行的`jvm`的线程情况进行快照，并且打印出来：

```shell
#用法
jstack <pid>
```

![image-20211113205456989](https://gitee.com/JKcoding/imgs/raw/master/img/202111132054129.png)



#### 6.1、线程的状态

![image-20211113212304284](https://gitee.com/JKcoding/imgs/raw/master/img/202111132123554.png)

#### 6.2、实战：死锁问题

如果在生产环境发生了死锁，我们将看到的是部署的程序没有任何反应了，这个时候我们可以借助`jstack`进行分析，下面我们实战下查找死锁的原因。

##### 6.2.1、构造死锁

编写代码，启动2个线程，`Thread1`拿到了`obj1`锁，准备去拿`obj2`锁时，`obj2`已经被`Thread2`锁定，所以发送了死锁。

```java
Object obj1 = new Object();
Object obj2 = new Object();
new Thread(() -> {
    synchronized (obj1) {
        System.out.println(Thread.currentThread().getName() + "获取的锁obj1");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (obj2) {
            System.out.println(Thread.currentThread().getName() + "获取的锁obj2");
        }
    }
}).start();
new Thread(() -> {
    synchronized (obj2) {
        System.out.println(Thread.currentThread().getName() + "获取的锁obj2");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (obj1) {
            System.out.println(Thread.currentThread().getName() + "获取的锁obj1");
        }
    }
}).start();
```

![image-20211113213439947](https://gitee.com/JKcoding/imgs/raw/master/img/202111132134009.png)



##### 6.2.2、使用jstack进行分析

![image-20211113213904820](https://gitee.com/JKcoding/imgs/raw/master/img/202111132139851.png)

![image-20211113214007772](https://gitee.com/JKcoding/imgs/raw/master/img/202111132140776.png)

![image-20211113214047578](https://gitee.com/JKcoding/imgs/raw/master/img/202111132140896.png)

**在输出的信息中，已经看到，发现了1个死锁，关键看这个**：![image-20211113214135649](https://gitee.com/JKcoding/imgs/raw/master/img/202111132141900.png)

可以看到

`Thread1`获取了锁`<0x00000000c4d21b38>`, 等待获取  `<0x00000000c4d21b28>`这个锁

`Thread0`获取了锁`<0x00000000c4d21b28>`, 等待获取  `<0x00000000c4d21b38>`这个锁

由此可见，发生了死锁。



### 7、**VisualVM**工具的使用

VisualVM，能够监控线程，内存情况，查看方法的CPU时间和内存中的对 象，已被GC的对象，反向查看分配的堆栈(如100个String对象分别由哪几个对象分配出来的)。VisualVM使用简单，几乎0配置，功能还是比较丰富的，几乎囊括了其它JDK自带命令的所有功能。 

- 内存信息 
- 线程信息 
- `Dump`堆（本地进程） 
- `Dump`线程（本地进程） 
- 打开堆`Dump`。堆`Dump`可以用`jmap`来生成。 
- 打开线程`Dump` 
- 生成应用快照（包含内存信息、线程信息等等） 
- 性能分析。`CPU`分析（各个方法调用时间，检查哪些方法耗时多），内存分析（各类 
- 对象占用的内存，检查哪些类占用内存多） 
- ……



![image-20211113225546759](https://gitee.com/JKcoding/imgs/raw/master/img/202111132255990.png)



**死锁程序**：

```java
public static void main(String[] args) {
    Object obj1 = new Object();
    Object obj2 = new Object();
    new Thread(() -> {
        synchronized (obj1) {
            System.out.println(Thread.currentThread().getName() + "获取的锁obj1");
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (obj2) {
                System.out.println(Thread.currentThread().getName() + "获取的锁obj2");
            }
        }
    }).start();
    new Thread(() -> {
        synchronized (obj2) {
            System.out.println(Thread.currentThread().getName() + "获取的锁obj2");
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (obj1) {
                System.out.println(Thread.currentThread().getName() + "获取的锁obj1");
            }
        }
    }).start();
}
```

![image-20211113225805731](https://gitee.com/JKcoding/imgs/raw/master/img/202111132258150.png)

![image-20211113225837602](https://gitee.com/JKcoding/imgs/raw/master/img/202111132258953.png)



visual VM：[https://www.cnblogs.com/happy-rabbit/p/6232581.html](https://www.cnblogs.com/happy-rabbit/p/6232581.html)

