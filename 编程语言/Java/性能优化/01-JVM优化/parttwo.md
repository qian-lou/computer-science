## part-two

- [ ] 了解什么是垃圾回收 
- [ ] 掌握垃圾会回收的常见算法 
- [ ] 学习串行、并行、并发、`G1`垃圾收集器 
- [ ] 学习`GC`日志的可视化查看



## 1、什么是垃圾回收？

程序的运行必然需要申请内存资源，无效的对象资源如果不及时处理就会一直占有内存资源，最终将导致内存溢出，所以对内存资源的管理是非常重要了。

### 1.1、C/C++语言的垃圾回收

在C/C++语言中，没有自动垃圾回收机制，是通过new关键字申请内存资源，通过delete关键字释放内存资源。 如果，程序员在某些位置没有写delete进行释放，那么申请的对象将一直占用内存资源， 最终可能会导致内存溢出。

### 1.2、Java语言的垃圾回收

为了让程序员更专注于代码的实现，而不用过多的考虑内存释放的问题，所以，在Java语言中，有了自动的垃圾回收机制，也就是我们熟悉的`GC`。 有了垃圾回收机制后，程序员只需要关心内存的申请即可，内存的释放由系统自动识别完成。 换句话说，自动的垃圾回收的算法就会变得非常重要了，如果因为算法的不合理，导致内存资源一直没有释放，同样也可能会导致内存溢出的。当然，除了Java语言，C#、Python等语言也都有自动的垃圾回收机制。



## 2、常见的垃圾回收算法

自动化的管理内存资源，垃圾回收机制必须要有一套算法来进行计算，哪些是有效的对象，哪些是无效的对象，对于无效的对象就要进行回收处理。**常见的垃圾回收算法有：引用计数法、标记清除法、标记压缩法、复制算法、分代算法等。**

### 2.1、引用计数法

引用计数是历史最悠久的一种算法，最早George E. Collins在1960的时候首次提出，50年后的今天，该算法依然被很多编程语言使用。 

#### 2.1.1、原理

假设有一个对象A，任何一个对象对A的引用，那么对象A的引用计数器+1，当引用失败时，对象A的引用计数器就-1，如果对象A的计数器的值为0，就说明对象A没有引用了，可以被回收。

#### 2.1.2、优缺点

**优点**：

- 实时性较高，无需等到内存不够的时候，才开始回收，运行时根据对象的计数器是否为0，就可以直接回收。 
- 在垃圾回收过程中，应用无需挂起。如果申请内存时，内存不足，则立刻报out of memory错误。 
- 区域性，更新对象的计数器时，只是影响到该对象，不会扫描全部对象。 

**缺点**：

- 每次对象被引用时，都需要去更新计数器，有一点时间开销。 
- 浪费CPU资源，即使内存够用，仍然在运行时进行计数器的统计。 
- 无法解决循环引用问题。（最大的缺点） 

**什么是循环引用**？

![image-20211116233221105](https://gitee.com/JKcoding/imgs/raw/master/img/202111162332941.png)

```java
public class Demo12 {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        a.b = b;
        b.a = a;
        a = null;
        b = null;
    }
}

class A {
    public B b;
}
class B {
    public A a;
}
```

**虽然a和b都为null，但是由于a和b存在循环引用，这样a和b永远都不会被回收**。



### 2.2、标记清除法

标记清除算法，是将垃圾回收分为2个阶段，分别是标记和清除。 

- 标记：从根节点开始标记引用的对象。 
- 清除：未被标记引用的对象就是垃圾对象，可以被清理。

#### 2.2.1、原理

![image-20211116234116603](https://gitee.com/JKcoding/imgs/raw/master/img/202111162341772.png)

这张图代表的是程序运行期间所有对象的状态，它们的标志位全部是0（也就是未标记，以下默认0就是未标记，1为已标记），假设这会儿有效内存空间耗尽了，`JVM`将会停止应用程序的运行并开启`GC`线程，然后开始进行标记工作，按照**根搜索算法**，标记完以后， 对象的状态如下图

![image-20211116233935255](https://gitee.com/JKcoding/imgs/raw/master/img/202111162339326.png)

可以看到，按照根搜索算法，所有从root对象可达的对象就被标记为了存活的对象，此时已经完成了第一阶段标记。接下来，就要执行第二阶段清除了，那么清除完以后，剩下的对象以及对象的状态如下图所示

![image-20211116234305700](https://gitee.com/JKcoding/imgs/raw/master/img/202111162343049.png)

可以看到，没有被标记的对象将会回收清除掉，而被标记的对象将会留下，并且会将标记位重新归0。接下来就不用说了，唤醒停止的程序线程，让程序继续运行即可。 

#### 2.2.2、优缺点

可以看到，标记清除算法解决了引用计数算法中的循环引用的问题，没有从`root`节点引用的对象都会被回收。 同样，标记清除算法也是有缺点的： 

- 效率较低，标记和清除两个动作都需要遍历所有的对象，并且在`GC`时，需要停止应用程序，对于交互性要求比较高的应用而言这个体验是非常差的。 
- 通过标记清除算法清理出来的内存，碎片化较为严重，因为被回收的对象可能存在于内存的各个角落，所以清理出来的内存是不连的



### 2.3、标记压缩算法

标记压缩算法是在标记清除算法的基础之上，做了优化改进的算法。和标记清除算法一样，也是从根节点开始，对对象的引用进行标记，在清理阶段，并不是简单的清理未标记的对象，而是将存活的对象压缩到内存的一端，然后清理边界以外的垃圾，从而解决了碎片化的问题。

#### 2.3.1、原理

![image-20211116235906225](https://gitee.com/JKcoding/imgs/raw/master/img/202111162359300.png)

#### 2.3.2、优缺点

优缺点同标记清除算法，解决了标记清除算法的碎片化的问题，同时，标记压缩算法多了一步，对象移动内存位置的步骤，其效率也有有一定的影响。



### 2.4、复制算法

复制算法的核心就是，将原有的内存空间一分为二，每次只用其中的一块，在垃圾回收时，将正在使用的对象复制到另一个内存空间中，然后将该内存空间清空，交换两个内存的角色，完成垃圾的回收。 如果内存中的垃圾对象较多，需要复制的对象就较少，这种情况下适合使用该方式并且效率比较高，反之，则不适合。

#### **2.4.1**、**JVM**中年轻代内存空间 

- 在`GC`开始的时候，对象只会存在于`Eden`区和名为`“From”`的`Survivor`区，`Survivor`区`“To”`是空的。 
-  紧接着进行`GC`，`Eden`区中所有存活的对象都会被复制到`“To”`，而在`“From”`区中，仍存活的对象会根据他们的年龄值来决定去向。年龄达到一定值(年龄阈值，可以通过`-XX:MaxTenuringThreshold`来设置）的对象会被移动到年老代中，没有达到阈值的对象会被复制到`“To”`区域。 
- 经过这次`GC`后，`Eden`区和`From`区已经被清空。这个时候，`“From”`和`“To”`会交换他们的角色，也就是新的`“To”`就是上次`GC`前的`“From”`，新的`“From”`就是上次`GC`前的`“To”`。不管怎样，都会保证名为`To`的`Survivor`区域是空的。 
- `GC`会一直重复这样的过程，直到`“To”`区被填满，`“To”`区被填满之后，会将所有对象移动到年老代中。 

#### 2.4.2、优缺点

**优点**：

- 在垃圾对象多的情况下，效率较高 
- 清理后，内存无碎片

**缺点**：

- 在垃圾对象少的情况下，不适用，如：老年代内存 
- 分配的2块内存空间，在同一个时刻，只能使用一半，内存使用率较低 



### 2.5、分代算法

前面介绍了多种回收算法，每一种算法都有自己的优点也有缺点，谁都不能替代谁，所以根据垃圾回收对象的特点进行选择，才是明智的选择。 

分代算法其实就是这样的，根据回收对象的特点进行选择，在`jvm`中，**年轻代适合使用复制算法**，**老年代适合使用标记清除或标记压缩算法**。



## 3、垃圾收集器以及内存分配

前面我们讲了垃圾回收的算法，还需要有具体的实现，在`jvm`中，实现了多种垃圾收集器，包括：串行垃圾收集器、并行垃圾收集器、`CMS`（并发）垃圾收集器、`G1`垃圾收集器，接下来，我们一个个的了解学习。

### 3.1、串行垃圾收集器

**串行垃圾收集器**，是指使用单线程进行垃圾回收，垃圾回收时，只有一个线程在工作，并且`java`应用中的所有线程都要暂停，等待垃圾回收的完成。这种现象称之为 `STW(Stop-The-World)`。对于交互性较强的应用而言，这种垃圾收集器是不能够接受的。 一般在`Javaweb`应用中是不会采用该收集器的。

#### 3.1.1、编写测试代码

```java
public class TestGC {
    public static void main(String[] args) throws InterruptedException {
        List<Object> list = new ArrayList<>();
        while (true) {
            int sleeps = new Random().nextInt(100);
            if (System.currentTimeMillis() % 2 == 0) {
                list.clear();
            } else {
                for (int i = 0; i < 10000; i++) {
                    Properties props = new Properties();
                    props.put("key_" + i, "value_" + System.currentTimeMillis());
                    list.add(props);
                }
            }
            Thread.sleep(sleeps);
        }
    }
}
```

#### 3.1.2、设置垃圾回收为串行收集器

在程序运行参数中添加2个参数，如下：

`-XX:+UseSerialGC`：指定年轻代和老年代都使用串行垃圾收集器

`-XX:+PrintGCDetails`：打印垃圾回收的详细信息

![image-20211122205543956](https://gitee.com/JKcoding/imgs/raw/master/img/202111222055057.png)

启动程序，可以看到下面信息：

```shell
[0.004s][warning][gc] -XX:+PrintGCDetails is deprecated. Will use -Xlog:gc* instead.
[0.011s][info   ][gc] Using Serial
[0.011s][info   ][gc,heap,coops] Heap address: 0x00000000ff000000, size: 16 MB, Compressed Oops mode: 32-bit
[0.162s][info   ][gc,start     ] GC(0) Pause Young (Allocation Failure)
[0.168s][info   ][gc,heap      ] GC(0) DefNew: 4416K->511K(4928K)
[0.168s][info   ][gc,heap      ] GC(0) Tenured: 0K->2503K(10944K)
[0.168s][info   ][gc,metaspace ] GC(0) Metaspace: 6761K->6761K(1056768K)
[0.168s][info   ][gc           ] GC(0) Pause Young (Allocation Failure) 4M->2M(15M) 5.262ms
[0.168s][info   ][gc,cpu       ] GC(0) User=0.00s Sys=0.00s Real=0.01s
[0.312s][info   ][gc,start     ] GC(1) Pause Young (Allocation Failure)
[0.316s][info   ][gc,heap      ] GC(1) DefNew: 4927K->511K(4928K)
[0.316s][info   ][gc,heap      ] GC(1) Tenured: 2503K->5352K(10944K)
[0.316s][info   ][gc,metaspace ] GC(1) Metaspace: 6761K->6761K(1056768K)
[0.316s][info   ][gc           ] GC(1) Pause Young (Allocation Failure) 7M->5M(15M) 4.595ms
```

GC日志信息解读：

年轻代的内存GC前后的大小：

`DefNew`：表示使用的是串行垃圾收集器。

`4416K->511K(4928K)`：表示，年轻代`GC`前，占有`4416K`内存，`GC`后，占有`512K`内存，总大小`4928K`

### 3.2、并行垃圾收集器

并行垃圾收集器在串行垃圾收集器的基础之上做了改进，将单线程改为了多线程进行垃圾回收，这样可以缩短垃圾回收的时间。（这里是指，并行能力较强的机器） 当然了，并行垃圾收集器在收集的过程中也会暂停应用程序，这个和串行垃圾回收器是一样的，只是并行执行，速度更快些，暂停的时间更短一些。

#### 3.2.1、**ParNew**垃圾收集器

`ParNew`垃圾收集器是工作在年轻代上的，只是将串行的垃圾收集器改为了并行。

通过`-XX:+UseParNewGC`参数设置年轻代使用`ParNew`回收器，老年代使用的依然是串行收集器。

#### 3.2.2、**ParallelGC**垃圾收集器 

ParallelGC收集器工作机制和ParNewGC收集器一样，只是在此基础之上，新增了两个和系统吞吐量相关的参数，使得其使用起来更加的灵活和高效。 

相关参数如下： 

`-XX:+UseParallelGC` ：年轻代使用`ParallelGC`垃圾回收器，老年代使用串行回收器。

`-XX:+UseParallelOldGC`：年轻代使用`ParallelGC`垃圾回收器，老年代使用`ParallelOldGC`垃圾回收器。

`-XX:MaxGCPauseMillis`：

- 设置最大的垃圾收集时的停顿时间，单位为毫秒
- 需要注意的时，ParallelGC为了达到设置的停顿时间，可能会调整堆大小或其他的参数，如果堆的大小设置的较小，就会导致GC工作变得很频繁，反而可能会影响到性能。 
- 该参数使用需谨慎。

`-XX:GCTimeRatio` ：

- 设置垃圾回收时间占程序运行时间的百分比，公式为`1/(1+n)`
- 它的值为`0~100`之间的数字，默认值为`99`，也就是垃圾回收时间不能超过`1%`

 `-XX:UseAdaptiveSizePolicy`：

- 自适应`GC`模式，垃圾回收器将自动调整年轻代、老年代等参数，达到吞吐量、堆大小、停顿时间之间的平衡
- 一般用于，手动调整参数比较困难的场景，让收集器自动进行调整

```shell
-XX:+UseParallelGC
-XX:+UseParallelOldGC
-XX:MaxGCPauseMillis=100
-XX:+PrintGCDetails
-Xms16m
-Xmx16m
```

![image-20211122213956451](https://gitee.com/JKcoding/imgs/raw/master/img/202111222139271.png)

```shell
[0.003s][warning][gc] -XX:+PrintGCDetails is deprecated. Will use -Xlog:gc* instead.
[0.012s][info   ][gc] Using Parallel
[0.012s][info   ][gc,heap,coops] Heap address: 0x00000000ff000000, size: 16 MB, Compressed Oops mode: 32-bit
[0.185s][info   ][gc,start     ] GC(0) Pause Young (Allocation Failure)
[0.190s][info   ][gc,heap      ] GC(0) PSYoungGen: 4096K->504K(4608K)
[0.190s][info   ][gc,heap      ] GC(0) ParOldGen: 0K->2220K(11264K)
[0.190s][info   ][gc,metaspace ] GC(0) Metaspace: 6762K->6762K(1056768K)
[0.190s][info   ][gc           ] GC(0) Pause Young (Allocation Failure) 4M->2M(15M) 5.126ms
[0.190s][info   ][gc,cpu       ] GC(0) User=0.00s Sys=0.00s Real=0.00s
[0.317s][info   ][gc,start     ] GC(1) Pause Young (Allocation Failure)
[0.322s][info   ][gc,heap      ] GC(1) PSYoungGen: 4600K->504K(4608K)
[0.322s][info   ][gc,heap      ] GC(1) ParOldGen: 2220K->5011K(11264K)
[0.322s][info   ][gc,metaspace ] GC(1) Metaspace: 6762K->6762K(1056768K)
[0.322s][info   ][gc           ] GC(1) Pause Young (Allocation Failure) 6M->5M(15M) 5.109ms
[0.322s][info   ][gc,cpu       ] GC(1) User=0.02s Sys=0.02s Real=0.01s
[0.409s][info   ][gc,start     ] GC(2) Pause Young (Allocation Failure)
[0.413s][info   ][gc,heap      ] GC(2) PSYoungGen: 4600K->512K(4608K)
[0.413s][info   ][gc,heap      ] GC(2) ParOldGen: 5011K->9101K(11264K)
[0.413s][info   ][gc,metaspace ] GC(2) Metaspace: 6762K->6762K(1056768K)
[0.413s][info   ][gc           ] GC(2) Pause Young (Allocation Failure) 9M->9M(15M) 4.873ms
[0.413s][info   ][gc,cpu       ] GC(2) User=0.00s Sys=0.03s Real=0.01s
[0.413s][info   ][gc,start     ] GC(3) Pause Full (Ergonomics)
[0.414s][info   ][gc,phases,start] GC(3) Marking Phase
[0.419s][info   ][gc,phases      ] GC(3) Marking Phase 5.855ms
[0.419s][info   ][gc,phases,start] GC(3) Summary Phase
[0.419s][info   ][gc,phases      ] GC(3) Summary Phase 0.010ms
[0.419s][info   ][gc,phases,start] GC(3) Adjust Roots
[0.420s][info   ][gc,phases      ] GC(3) Adjust Roots 1.105ms
[0.421s][info   ][gc,phases,start] GC(3) Compaction Phase
[0.430s][info   ][gc,phases      ] GC(3) Compaction Phase 9.803ms
```

以上信息可以看出，年轻代和老年代都使用了`ParallelGC`垃圾回收器

### 3.3、CMS垃圾收集器

`CMS`全称 `Concurrent Mark Sweep`，是一款并发的、使用标记-清除算法的垃圾回收器， 该回收器是针对老年代垃圾回收的，通过参数-`XX:+UseConcMarkSweepGC`进行设置。 

`CMS`垃圾回收器的执行过程如下： 

![image-20211122220218602](https://gitee.com/JKcoding/imgs/raw/master/img/202111222202685.png)

- 初始化标记(`CMS-initial-mark`) ,标记`root`，会导致`stw`； 
- 并发标记(`CMS-concurrent-mark`)，与用户线程同时运行； 
- 预清理（`CMS-concurrent-preclean`），与用户线程同时运行； 
- 重新标记(`CMS-remark`) ，会导致`stw`； 
- 并发清除(`CMS-concurrent-sweep`)，与用户线程同时运行； 
- 调整堆大小，设置`CMS`在清理之后进行内存压缩，目的是清理内存中的碎片； 
- 并发重置状态等待下次`CMS`的触发(`CMS-concurrent-reset`)，与用户线程同时运行



### 3.4、G1垃圾收集器（重点）

`G1`垃圾收集器是在`jdk1.7`中正式使用的全新的垃圾收集器，`oracle`官方计划在`jdk9`中将`G1`变成默认的垃圾收集器，以替代`CMS`

`G1`的设计原则就是简化`JVM`性能调优，开发人员只需要简单的三步即可完成调优： 

1. 第一步，开启G1垃圾收集器 

2. 第二步，设置堆的最大内存 

3. 第三步，设置最大的停顿时间 

`G1`中提供了三种模式垃圾回收模式，`Young GC`、`Mixed GC` 和 `Full GC`，在不同的条件下被触发。

#### 3.4.1、原理

`G1`垃圾收集器相对比其他收集器而言，最大的区别在于它取消了年轻代、老年代的物理划分，取而代之的是将堆划分为若干个区域（`Region`），这些区域中包含了有逻辑上的年轻代、老年代区域。 这样做的好处就是，我们再也不用单独的空间对每个代进行设置了，不用担心每个代内存是否足够。

![image-20211122230758846](https://gitee.com/JKcoding/imgs/raw/master/img/202111222308122.png)

![image-20211122230820576](https://gitee.com/JKcoding/imgs/raw/master/img/202111222308823.png)

在`G1`划分的区域中，年轻代的垃圾收集依然采用暂停所有应用线程的方式，将存活对象拷贝到老年代或者`Survivor`空间，`G1`收集器通过将对象从一个区域复制到另外一个区域，完成了清理工作。 这就意味着，在正常的处理过程中，`G1`完成了堆的压缩（至少是部分堆的压缩），这样也就不会有`cms`内存碎片问题的存在了。 在`G1`中，有一种特殊的区域，叫`Humongous`区域。

- 如果一个对象占用的空间超过了分区容量`50%`以上，`G1`收集器就认为这是一个巨型对象。 
- 这些巨型对象，默认直接会被分配在老年代，但是如果它是一个短期存在的巨型对象，就会对垃圾收集器造成负面影响。 
- 为了解决这个问题，`G1`划分了一个`Humongous`区，它用来专门存放巨型对象。如果一个H区装不下一个巨型对象，那么`G1`会寻找连续的H分区来存储。为了能找到连续的H区，有时候不得不启动`Full GC`

#### 3.4.2、Young GC

`Young GC`主要是对`Eden`区进行`GC`，它在`Eden`空间耗尽时会被触发。

- `Eden`空间的数据移动到`Survivor`空间中，如果`Survivor`空间不够，`Eden`空间的部分数据会直接晋升到年老代空间。 
- `Survivor`区的数据移动到新的`Survivor`区中，也有部分数据晋升到老年代空间中。 
- 最终`Eden`空间的数据为空，`GC`停止工作，应用线程继续执行。

![image-20211122232404534](https://gitee.com/JKcoding/imgs/raw/master/img/202111222324845.png)



##### 3.4.2.1、**Remembered Set**（已记忆集合）

在GC年轻代的对象时，我们如何找到年轻代中对象的根对象呢？ 

根对象可能是在年轻代中，也可以在老年代中，那么老年代中的所有对象都是根么？ 

如果全量扫描老年代，那么这样扫描下来会耗费大量的时间。 

于是，G1引进了RSet的概念。它的全称是Remembered Set，其作用是跟踪指向某个堆内的对象引用。

![image-20211122232537233](https://gitee.com/JKcoding/imgs/raw/master/img/202111222325371.png)

每个`Region`初始化时，会初始化一个`RSet`，该集合用来记录并跟踪其它`Region`指向该`Region`中对象的引用，每个`Region`默认按照`512Kb`划分成多个`Card`，所以`RSet`需要记录的东西应该是 `xx Region`的 `xx Card`。

#### 3.4.3、**Mixed GC** 

当越来越多的对象晋升到老年代`old region`时，为了避免堆内存被耗尽，虚拟机会触发一个混合的垃圾收集器，即`Mixed GC`，该算法并不是一个`Old GC`，除了回收整个`Young Region`，还会回收一部分的`Old Region`，这里需要注意：是一部分老年代，而不是全部老年代，可以选择哪些`old region`进行收集，从而可以对垃圾回收的耗时时间进行控制。 也要注意的是Mixed GC 并不是FULL GC

**`MixedGC`什么时候触发？** 由参数 **-XX:InitiatingHeapOccupancyPercent=n** 决定。默认：**45%**，该参数的意思是：**当老年代大小占整个堆大小百分比达到该阀值时触发**

它的GC步骤分2步：

1. 全局并发标记（`global concurrent marking`） 

2. 拷贝存活对象（`evacuation`）

##### 3.4.3.1、全局并发标记

全局并发标记，执行过程分为五个步骤：

- 初始标记（initial mark，STW） 

​		标记从根节点直接可达的对象，这个阶段会执行一次年轻代GC，会产生全局停顿。

- 根区域扫描（root region scan）

​		`G1 GC` 在初始标记的存活区扫描对老年代的引用，并标记被引用的对象。 

​		该阶段与应用程序（非 `STW`）同时运行，并且只有完成该阶段后，才能开始下一次 `STW` 年轻代垃圾回收。 

- 并发标记（Concurrent Marking） 

  `G1 GC` 在整个堆中查找可访问的（存活的）对象。该阶段与应用程序同时运行，可以被 `STW` 年轻代垃圾回收中断。 

- 重新标记（Remark，STW）

  该阶段是 `STW` 回收，因为程序在运行，针对上一次的标记进行修正

- 清除垃圾（Cleanup，STW） 

​		清点和重置标记状态，该阶段会`STW`，这个阶段并不会实际上去做垃圾的收集， 等待`evacuation`阶段来回收。 

3.4.3.2、拷贝存活对象

`Evacuation`阶段是全暂停的。该阶段把一部分`Region`里的活对象拷贝到另一部分`Region` 中，从而实现垃圾的回收清理

#### 3.4.4、G1收集器相关参数

- XX:+UseG1GC

  使用 G1 垃圾收集器

- -XX:MaxGCPauseMillis

  设置期望达到的最大GC停顿时间指标（JVM会尽力实现，但不保证达到），默认值是 200 毫秒

- -XX:G1HeapRegionSize=n

  设置的 G1 区域的大小。值是 2 的幂，范围是 1 MB 到 32 MB 之间。目标是根据最小的 Java 堆大小划分出约 2048 个区域。 默认是堆内存的1/2000。 

- -XX:ParallelGCThreads=n

  设置 STW 工作线程数的值。将 n 的值设置为逻辑处理器的数量。n 的值与逻辑处理器的数量相同，最多为 8

- -XX:ConcGCThreads=n

  设置并行标记的线程数。将 n 设置为并行垃圾回收线程数 (ParallelGCThreads) 的 1/4 左右。 

- -XX:InitiatingHeapOccupancyPercent=n

​		设置触发标记周期的 Java 堆占用率阈值。默认占用率是整个 Java 堆的 45%

#### 3.4.5、对于**G1**垃圾收集器优化建议

- 年轻代大小 

  避免使用 -Xmn 选项或 -XX:NewRatio 等其他相关选项显式设置年轻代大小。 

  固定年轻代的大小会覆盖暂停时间目标。

- 暂停时间目标不要太过严苛 

​		G1 GC 的吞吐量目标是 90% 的应用程序时间和 10%的垃圾回收时间。 

​		评估 G1 GC 的吞吐量时，暂停时间目标不要太严苛。目标太过严苛表示您愿意承受更多的垃圾回收开销，而这会直接影响到吞吐量

​	

### 4、可视化**GC**日志分析工具

#### 4.1、**GC**日志输出参数

前面通过`-XX:+PrintGCDetails`可以对`GC`日志进行打印，我们就可以在控制台查看，这样虽然可以查看GC的信息，但是并不直观，可以借助于第三方的GC日志分析工具进行查看。

在日志打印输出涉及到的参数如下：

```shell
‐XX:+PrintGC 输出GC日志 
‐XX:+PrintGCDetails 输出GC的详细日志 
‐XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式） 
‐XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2013‐05‐ 04T21:53:59.234+0800） 
‐XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息 
‐Xloggc:../logs/gc.log 日志文件的输出路径
```

测试：

```shell
‐XX:+UseG1GC ‐XX:MaxGCPauseMillis=100 ‐Xmx256m ‐XX:+PrintGCDetails ‐ XX:+PrintGCTimeStamps ‐XX:+PrintGCDateStamps ‐XX:+PrintHeapAtGC ‐ Xloggc:F://test//gc.log
```

运行后就可以在F盘下生成gc.log文件。

#### 4.2、**GC Easy** 可视化工具

GC Easy是一款在线的可视化工具，易用、功能强大，网站：http://gceasy.io/ 
