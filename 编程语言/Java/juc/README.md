### 线程基础

#### JUC一些知识点

- `ReentrantLock`：`ReentrantReadWriteLock`和`Condition`
- 工具类：`CountDownLatch`、`CyclicBarrier`、`Semaphore`
- 线程池与阻塞队列
- `ForkJoinPool`与`ForkJoinTask`
- `Java8`新特性函数式编程、方法引用、`lambda Express`
- 原子操作类`Atomic`
- `volatile`
- `Callable`和`FutureTask`
- and so on。。。

#### 口诀

- 高内聚低耦合前提下，封装思想
- 判断、干活、通知
- 防止虚假唤醒,wait方法要注意使用while判断
- 注意标志位flag，可能是volatile的

#### 为什么多线程极其重要？？？

##### 	硬件方面

​	摩尔定律失效：

> 摩尔定律：
> 它是由英特尔创始人之一Gordon Moore(戈登·摩尔)提出来的。其内容为：
> 当价格不变时，集成电路上可容纳的元器件的数目约每隔18-24个月便会增加一倍，性能也将提升一倍。
> 换言之，每一美元所能买到的电脑性能，将每隔18-24个月翻一倍以上。这一定律揭示了信息技术进步的速度。
>
> 可是从2003年开始CPU主频已经不再翻倍，而是采用多核而不是更快的主频。
>
> 摩尔定律失效。
>
> 在主频不再提高且核数在不断增加的情况下，要想让程序更快就要用到并行或并发编程。

##### 	软件方面

​	高并发系统，异步+回调等生产需求



#### 从start一个线程说起

##### Java线程理解以及`openjdk`中的实现

```java
new Thread(() -> {}).start();
```

在`Thread.java` 中

```java
public synchronized void start() {
    /**
     * This method is not invoked for the main method thread or "system"
     * group threads created/set up by the VM. Any new functionality added
     * to this method in the future may have to also be added to the VM.
     *
     * A zero status value corresponds to state "NEW".
     */
    if (threadStatus != 0)
        throw new IllegalThreadStateException();

    /* Notify the group that this thread is about to be started
     * so that it can be added to the group's list of threads
     * and the group's unstarted count can be decremented. */
    group.add(this);

    boolean started = false;
    try {
        start0();
        started = true;
    } finally {
        try {
            if (!started) {
                group.threadStartFailed(this);
            }
        } catch (Throwable ignore) {
            /* do nothing. If start0 threw a Throwable then
              it will be passed up the call stack */
        }
    }
}

private native void start0();
```

Java语言本身底层就是C++语言

OpenJDK源码网址http://openjdk.java.net/  下载后查看openjdk8\hotspot\src\share\vm\runtime



##### 更加底层的C++源码解读

- `openjdk8\jdk\src\share\native\java\lang`

```
thread.c

java线程是通过start的方法启动执行的，主要内容在native方法start0中，Openjdk的写JNI一般是一一对应的，Thread.java对应的就是Thread.c，start0其实就是JVM_StartThread。此时查看源代码可以看到在jvm.h中找到了声明，jvm.cpp中有实现。
```

![image-20220220175336939](https://gitee.com/JKcoding/imgs/raw/master/img/202202201753069.png)



- `openjdk8\hotspot\src\share\vm\prims`

```
jvm.cpp
```

![image-20220220175503405](https://gitee.com/JKcoding/imgs/raw/master/img/202202201755336.png)

![image-20220220175521613](https://gitee.com/JKcoding/imgs/raw/master/img/202202201755625.png)

- `openjdk8\hotspot\src\share\vm\runtime`

```
thread.cpp
```

![image-20220220175603253](https://gitee.com/JKcoding/imgs/raw/master/img/202202201756174.png)



##### Java多线程相关概念

###### 进程

是程序的⼀次执⾏，是系统进⾏资源分配和调度的独⽴单位，每⼀个进程都有它⾃⼰的内存空间和系统资源

###### 线程

在同⼀个进程内⼜可以执⾏多个任务，⽽这每⼀个任务我们就可以看做是⼀个线程

⼀个进程会有1个或多个线程的

###### 面试题：何为进程和线程？

![image-20220220183542171](https://gitee.com/JKcoding/imgs/raw/master/img/202202201835503.png)

![image-20220220183601649](https://gitee.com/JKcoding/imgs/raw/master/img/202202201836815.png)



![image-20220220183624015](https://gitee.com/JKcoding/imgs/raw/master/img/202202201836903.png)



![image-20220220183658903](https://gitee.com/JKcoding/imgs/raw/master/img/202202201837127.png)

![image-20220220183805252](https://gitee.com/JKcoding/imgs/raw/master/img/202202201838306.png)



![image-20220220183821956](https://gitee.com/JKcoding/imgs/raw/master/img/202202201838878.png)



![image-20220220183836118](https://gitee.com/JKcoding/imgs/raw/master/img/202202201838502.png)

![image-20220220183851792](https://gitee.com/JKcoding/imgs/raw/master/img/202202201838143.png)

![image-20220220183904045](https://gitee.com/JKcoding/imgs/raw/master/img/202202201839098.png)



![image-20220220183915581](https://gitee.com/JKcoding/imgs/raw/master/img/202202201839545.png)



![image-20220220183933792](https://gitee.com/JKcoding/imgs/raw/master/img/202202201839716.png)





###### 管程

Monitor(监视器)，也就是我们平时所说的锁

Monitor其实是一种同步机制，他的义务是保证（同一时间）只有一个线程可以访问被保护的数据和代码。

JVM中同步是基于进入和退出监视器对象(Monitor,管程对象)来实现的，每个对象实例都会有一个Monitor对象，

```java
Object o = new Object();
new Thread(() -> {
    synchronized (o){

    }
},"t1").start();
```

Monitor对象会和Java对象一同创建并销毁，它底层是由C++语言来实现的。

![image-20220220184809797](https://gitee.com/JKcoding/imgs/raw/master/img/202202201848170.png)

​	JVM第3版

![image-20220220184848873](https://gitee.com/JKcoding/imgs/raw/master/img/202202201848887.png)



##### 用户线程和守护线程

Java线程分为用户线程和守护线程，线程的daemon属性为true表示是守护线程，false表示是用户线程

###### 守护线程：

是一种特殊的线程，在后台默默地完成一些系统性的服务，比如垃圾回收线程

###### 用户线程

是系统的工作线程，它会完成这个程序需要完成的业务操作

```Java
 public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 开始运行，" + (Thread.currentThread().isDaemon() ? "守护线程" : "用户线程"));
            while (true) {

            }
        }, "t1");
        //线程的daemon属性为true表示是守护线程，false表示是用户线程
        t1.setDaemon(true);
        t1.start();
        //3秒钟后主线程再运行
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("----------main线程运行完毕");
    }
```

###### 重点

- 当程序中所有用户线程执行完毕之后，不管守护线程是否结束，系统都会自动退出。如果用户线程全部结束了，意味着程序需要完成的业务操作已经结束了，系统可以退出了。所以当系统只剩下守护进程的时候，java虚拟机会自动退出
- 设置守护线程，需要在`start()`方法之前进行



### CompletableFuture

Future和Callable接口







### 说说Java“锁”事

### LockSupport与线程中断

### Java内存模型之JMM

### volatile与Java内存模型

### CAS

### 原子操作类之18罗汉增强

### 聊聊ThreadLocal

### Java对象内存布局和对象头

### Synchronized与锁升级

### AbstractQueuedSynchronizer之AQS

### ReentrantLock、ReentrantReadWriteLock、StampedLock讲解

### 总结