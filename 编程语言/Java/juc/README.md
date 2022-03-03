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

#### **Future和Callable接口**

Future接口定义了操作异步任务执行一些方法，如获取异步任务的执行结果、取消任务的执行、判断任务是否被取消、判断任务执行是否完毕等。

Callable接口中定义了需要有返回的任务需要实现的方法

```
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

比如主线程让一个子线程去执行任务，子线程可能比较耗时，启动子线程开始执行任务后，主线程就去做其他事情了，过了一会才去获取子任务的执行结果。



#### 从之前的FutureTask说开去

![image-20220228204828839](https://gitee.com/JKcoding/imgs/raw/master/img/202202282048179.png)

```java
FutureTask<Integer> task = new FutureTask<Integer>(()-> {
    Thread.sleep(5000);
    return 1;
});
new Thread(task).start();
System.out.println("main1");
System.out.println(task.get());
System.out.println("main2");
```

一旦调用`get()`方法，不管是否计算完成都会导致阻塞，o(╥﹏╥)o

`isDone()`轮询：轮询的方式会耗费无谓的CPU资源，而且也不见得能及时地得到计算结果。如果想要异步获取结果,通常都会以轮询的方式去获取结果尽量不要阻塞

当需要完成复杂的任务时：

- 应对Future的完成时间，完成了可以告诉我，也就是我们的回调通知
- 将两个异步计算合成一个异步计算，这两个异步计算互相独立，同时第二个又依赖第一个的结果。
- 当Future集合中某个任务最快结束时，返回结果。
- 等待Future结合中的所有任务都完成。



#### 对Future的改进

`CompletableFuture`和`CompletionStage`源码分别介绍

![image-20220301215625859](https://gitee.com/JKcoding/imgs/raw/master/img/202203012156213.png)

![image-20220301215728034](https://gitee.com/JKcoding/imgs/raw/master/img/202203012157672.png)

**接口`CompletionStage`**：

- completionStage代表异步计算过程中的某一个阶段，一个阶段完成以后可能会触发另一个阶段
- 一个阶段的计算执行可以是`Function`，`Comsumer`或者`Runnable`。比如`stage.thenApply(x ->System.out.print(x)).thenRun(()->System.out.println())`
- 一个阶段的执行可能是被单个阶段的完成触发，也可能是由多个阶段一起触发

代表异步计算过程中的某一个阶段，一个阶段完成以后可能会触发另外一个阶段，有些类似Linux系统的管道分隔符传参数。

**类`CompletableFuture`**:

- 在`Java8`中，`CompletableFuture`提供了非常强大的`Future`的扩展功能，可以帮助我们简化异步编程的复杂性，并且提供了函数式编程的能力，可以通过回调的方式处理计算结果，也提供了转换和组合`CompletableFuture`的方法。
- 它可能代表一个明确完成的`Future`，也有可能代表一个完成阶段（`CompletionStage`），它支持在计算完成以后触发一些函数或执行某些动作。
- 它实现了`Future`和`CompletionStaget`接口



**核心的四个静态方法，来创建一个异步操作**

`runAsync` 无 返回值：

```java
public static CompletableFuture<Void> runAsync(Runnable runnable)`
public static CompletableFuture<Void> runAsync(Runnable runnable,Executor executor)
```

`supplyAsync` 有 返回值：

```java
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier,Executor executor)
```

**上述`Executor executor`参数说明**：

- 没有指定`Executor`的方法，直接使用默认的`ForkJoinPool.commonPool()` 作为它的线程池执行异步代码。
- 如果指定线程池，则使用我们自定义的或者特别指定的线程池执行异步代码

代码：

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
    System.out.println(Thread.currentThread().getName() + "----come in-1");
});

CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
    System.out.println(Thread.currentThread().getName() + "----come in-2");
}, executor);
System.out.println(f1.get());
System.out.println(f2.get());
System.out.println("------------");

CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
    System.out.println(Thread.currentThread().getName() + "----come in-1 supplyAsync");
    return "supplyAsync - 1";
});
CompletableFuture<String> f4 = CompletableFuture.supplyAsync(() -> {
    System.out.println(Thread.currentThread().getName() + "----come in-2 supplyAsync");
    return "supplyAsync - 2";
}, executor);

System.out.println(f3.get());
System.out.println(f4.get());
executor.shutdown();
```

从`Java8`开始引入了`CompletableFuture`，它是`Future`的功能增强版，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
    System.out.println(Thread.currentThread().getName() + "----come in-1");
    try {
        TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return 1;
}, executor).thenApply(f -> {
    System.out.println(Thread.currentThread().getName() + "thenApplyAsync");
    try {
        TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return f + 2;
}).whenComplete((v, e) -> {
    System.out.println(Thread.currentThread().getName() + "whenComplete");
    if (e == null) {
        System.out.println("---result: " + v);
    }
}).exceptionally(e -> {
    e.printStackTrace();
    return null;
});

System.out.println("main over");
while (!f1.isDone()) {}
executor.shutdown();
```

**`CompletableFuture`的优点：**

- 异步任务结束时，会自动回调某个对象的方法
- 异步任务出错时，会自动回调某个对象的方法
- 主线程设置好回调后，不再关心异步任务的执行，异步任务之间可以顺序执行



#### 案例精讲-从电商网站的比价需求说开去

> 经常出现在等待某条 SQL 执行完成后，再继续执行下一条 SQL ，而这两条 SQL 本身是并无关系的，可以同时进行执行的。
>
> 我们希望能够两条 SQL 同时进行处理，而不是等待其中的某一条 SQL 完成后，再继续下一条。同理，
> 对于分布式微服务的调用，按照实际业务，如果是无关联step by step的业务，可以尝试是否可以多箭齐发，同时调用。
>
> 我们去比同一个商品在各个平台上的价格，要求获得一个清单列表，
> 1 step by step，查完京东查淘宝，查完淘宝查天猫......
>
> 2 all   一口气同时查询。。。。。

```java
public class Code3 {

    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("pdd"),
            new NetMall("tmall")
    );

    private static ExecutorService service = Executors.newFixedThreadPool(50);

    public static List<String> getPriceByStep(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall ->
                        String.format(productName + " in %s price is %.2f",
                                netMall.getMallName(), netMall.calPrice(productName)))
                .collect(Collectors.toList());
    }

    public static List<String> getPriceByAsync(List<NetMall> list, String productName) {
        return list.stream()
                .map(netMall ->
                        CompletableFuture.supplyAsync(() ->
                                String.format(productName + " in %s price is %.2f",
                                netMall.getMallName(), netMall.calPrice(productName)), service)
                )
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<String> mysqls = getPriceByStep(list, "mysql");
        mysqls.forEach(System.out::println);
        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) + " ms");
        start = System.currentTimeMillis();
        List<String> oracles = getPriceByAsync(list, "oracle");
        oracles.forEach(System.out::println);
        end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) + " ms");
    }
}

class NetMall {
    private String mallName;

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public NetMall(String mallName) {
        this.mallName = mallName;
    }
    public double calPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}
```



#### `CompletableFuture`常用方法

##### 获得结果和触发计算

```java
public T    get()
public T    get(long timeout, TimeUnit unit)
public T    getNow(T valueIfAbsent) 立即获取结果不阻塞，计算完，返回计算完成后的结果，没算完，返回设定的valueIfAbsent值
```

`getNow`方法

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    try {
        TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return 1;
});
System.out.println(future.getNow(2));
```

```java
public T    join()
```

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    System.out.println(CompletableFuture.supplyAsync(() -> "abc").thenApply(r -> r + "de").join());
}
```

**主动触发计算**：`public boolean complete(T value)` 是否打断get方法立即返回括号值

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 533;
    });

    //注释掉暂停线程，get还没有算完只能返回complete方法设置的444；暂停2秒钟线程，异步线程能够计算完成返回get
    try {
        TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    //当调用CompletableFuture.get()被阻塞的时候,complete方法就是结束阻塞并get()获取设置的complete里面的值.
    System.out.println(completableFuture.complete(444) + "\t" + completableFuture.get());
}
```



##### 对计算结果进行处理

`thenApply`：计算结果存在依赖关系，这两个线程串行化

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture.supplyAsync(() -> {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1024;
    }).thenApply(f -> {
        System.out.println("--2--");
        return f + 1;
    }).thenApply(f -> {
        System.out.println("--3--");
        return f + 1;
    }).whenComplete((v, e) -> {
        System.out.println("--v: " + v);
    }).exceptionally(e -> {
        e.printStackTrace();
        return null;
    });
    // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
    try {
        TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

}
```

由于存在依赖关系(当前步错，不走下一步)，当前步骤有异常的话就叫停。



`handle`：有异常也可以往下一步走，根据带的异常参数可以进一步处理

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture.supplyAsync(() -> {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--1--");
        return 1024;
    }).handle((f, e) -> {
        int age = 1 / 0;
        System.out.println("--2--");
        return f + 1;
    }).handle((f, e) -> {
        System.out.println("--3--");
        return f + 1;
    }).whenComplete((v, e) -> {
        System.out.println("---v: " + v);
    }).exceptionally(e -> {
        e.printStackTrace();
        return null;
    });
    System.out.println("-----主线程结束，END");

    // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
    try {
        TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

}

-----主线程结束，END
--1--
--3--
---v: null
java.util.concurrent.CompletionException: java.lang.NullPointerException
```

总结：

![image-20220301231659490](https://gitee.com/JKcoding/imgs/raw/master/img/202203012317283.png)

![image-20220301231714338](https://gitee.com/JKcoding/imgs/raw/master/img/202203012317625.png)



##### 对计算结果进行消费

接收任务的处理结果，并消费处理，无返回结果

`thenAccept`

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture.supplyAsync(() -> 1)
            .thenApply(f -> f + 1)
            .thenApply(f -> f + 1)
            .thenAccept(System.out::println);
}
```

任务之间的顺序执行

`thenRun`：`thenRun(Runnable runnable)`

`thenAccept`：`thenAccept(Consumer action)`，任务 A 执行完执行 B，B 需要 A 的结果，但是任务 B 无返回值

`thenApply`：`thenApply(Function)` ，任务 A 执行完执行 B，B 需要 A 的结果，同时任务 B 有返回值

```java
System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());
System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {}).join());
System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB").join());
```



##### 对计算速度进行选用

`applyToEither`   谁快用谁

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture<Integer> completableFuture01 = CompletableFuture.supplyAsync(() -> {
        System.out.println(Thread.currentThread().getName() + "\t" + "-completableFuture01-come in");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 10;
    });
    CompletableFuture<Integer> completableFuture02 = CompletableFuture.supplyAsync(() -> {
        System.out.println(Thread.currentThread().getName() + "\t" + "-completableFuture02-come in");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 20;
    });
    //completableFuture01和completableFuture02 谁快用谁的结果
    System.out.println(completableFuture01.applyToEither(completableFuture02, f -> {
        System.out.println(Thread.currentThread().getName() + "\t" + "-applyToEither- come int");
        return f + 1;
    }).join());
}

ForkJoinPool.commonPool-worker-19	-completableFuture01-come in
ForkJoinPool.commonPool-worker-5	-completableFuture02-come in
ForkJoinPool.commonPool-worker-5	-applyToEither- come int
21
```



##### 对计算结果进行合并

两个`CompletionStage`任务都完成后，最终能把两个任务的结果一起交给`thenCombine` 

先完成的先等着，等待其它分支任务

`thenCombine`

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture<Integer> completableFuture01 = CompletableFuture.supplyAsync(() -> {
        System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
        return 10;
    });
    CompletableFuture<Integer> completableFuture02 = CompletableFuture.supplyAsync(() -> {
        System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
        return 20;
    });
    System.out.println(completableFuture01.thenCombine(completableFuture02, (x, y) -> x + y).join());
}
```

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> thenCombineResult = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 1");
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 2");
            return 20;
        }), (x,y) -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 3");
            return x + y;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 4");
            return 30;
        }),(a,b) -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 5");
            return a + b;
        });
        System.out.println("-----主线程结束，END");
        System.out.println(thenCombineResult.get());


        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        try { TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
    }

```





### 说说Java“锁”事

先看看一些面试题，看看有没有打击到你

> 一、Synchronized相关问题
>
> 1、Synchronized用过吗，其原理是什么？
>
> 2、你刚才提到获取对象的锁，这个“锁”到底是什么？如何确定对象的锁？
>
> 3、什么是可重入性，为什么说Synchronized是可重入锁？
>
> 4、JVM对java的原生锁做了哪些优化？
>
> 5、为什么说Synchronized是非公平锁？
>
> 6、什么是锁消除和锁粗化？
>
> 7、为什么说Synchronized是一个悲观锁？乐观锁的实现原理又是什么？什么是CAS
>
> 8、乐观锁一定就是好的吗？
>
> 二、可重入锁ReentrantLock以及其他显示锁相关问题
>
> 1、跟Synchronized相比，可重入锁ReentrantLock其实现原理有什么不同？
>
> 2、那么请谈谈AQS框架是怎么回事？
>
> 3、请尽可能详尽地对比下Synchronized和ReentrantLock的异同
>
> 4、ReentrantLock是如何实现可重入锁性的？



#### 乐观锁和悲观锁

**悲观锁**


认为自己在使用数据的时候一定有别的线程来修改数据，因此在获取数据的时候会先加锁，确保数据不会被别的线程修改。synchronized关键字和Lock的实现类都是悲观锁

- 适合写操作多的场景，先加锁可以保证写操作时数据正确。
- 显式的锁定之后再操作同步资源

**乐观锁**


乐观锁认为自己在使用数据时不会有别的线程修改数据，所以不会添加锁，只是在更新数据的时候去判断之前有没有别的线程更新了这个数据。

如果这个数据没有被更新，当前线程将自己修改的数据成功写入。如果数据已经被其他线程更新，则根据不同的实现方式执行不同的操作

乐观锁在Java中是通过使用无锁编程来实现，最常采用的是CAS算法，Java原子类中的递增操作就通过CAS自旋实现的。

- 适合读操作多的场景，不加锁的特点能够使其读操作的性能大幅提升。

- 乐观锁则直接去操作同步资源，是一种无锁算法，得之我幸不得我命，再抢

- 乐观锁一般有两种实现方式：

  采用版本号机制

  `CAS`（`Compare-and-Swap`，即比较并替换）算法实现

```java
//=============悲观锁的调用方式
public synchronized void m1() {
    //加锁后的业务逻辑......
}
ReentrantLock lock = new ReentrantLock();
public void m2() {
    lock.lock();
    try {
        // 操作同步资源
    } finally {
        lock.unlock();
    }
}
//=============乐观锁的调用方式
// 保证多个线程使用的是同一个AtomicInteger
private AtomicInteger atomicInteger = new AtomicInteger();
public void m3() {
    atomicInteger.incrementAndGet();
}
```



#### 锁的是什么

通过8种情况演示锁运行案例，看看我们到底锁的是什么

看看JVM中对应的锁在哪里？

![image-20220303103440787](https://gitee.com/JKcoding/imgs/raw/master/img01/image-20220303103440787.png)



`synchronized`有三种应用方式

​	JDK源码(notify方法)说明举例![image-20220303103618621](https://gitee.com/JKcoding/imgs/raw/master/img01/image-20220303103618621.png)

8种锁的案例实际体现在3个地方

- 作用于实例方法，当前实例加锁，进入同步代码前要获得当前实例的锁；
- 作用于代码块，对括号里配置的对象加锁。
- 作用于静态方法，当前类加锁，进去同步代码前要获得当前类对象的锁；



**从字节码角度分析`synchronized`实现**

javap -c ***.class文件反编译

假如你需要更多信息：

```shell
1）javap -v ***.class文件反编译
2） -v  -verbose             输出附加信息（包括行号、本地变量表，反汇编等详细信息）
```

下面看看三种代码

**（1）synchronized同步代码块**

```java
public static void main(String[] args) {
    new Demo01().m1();
}

public void m1() {
    synchronized (this) {
        System.out.println("hello world");
    }
}
```

使用`javap -c  ***.class`反编译

```java
public void m1();
    Code:
       0: aload_0
       1: dup
       2: astore_1
       3: monitorenter
       4: getstatic     #26                 // Field java/lang/System.out:Ljava/io/PrintStream;
       7: ldc           #27                 // String hello world
       9: invokevirtual #28                 // Method java/io/PrintStream.println:(Ljava/lang/String;)
V
      12: aload_1
      13: monitorexit
      14: goto          22
      17: astore_2
      18: aload_1
      19: monitorexit
      20: aload_2
      21: athrow
      22: return
    Exception table:
       from    to  target type
           4    14    17   any
          17    20    17   any

```

![image-20220303105451130](https://gitee.com/JKcoding/imgs/raw/master/img01/image-20220303105451130.png)

可以看到有一个`monitorenter`和两个`monitorexit`，有两个`monitorexit`是为了在异常情况下也能释放锁

那么一定是一个enter两个exit吗？看看下面，添加一个异常

```java
public void m1() {
    synchronized (this) {
        System.out.println("hello world");
        throw new RuntimeException("test synchronized");
    }
}
```

看看反编译后的

```java
 public void m1();
    Code:
       0: aload_0
       1: dup
       2: astore_1
       3: monitorenter
       4: getstatic     #26                 // Field java/lang/System.out:Ljava/io/PrintStream;
       7: ldc           #27                 // String hello world
       9: invokevirtual #28                 // Method java/io/PrintStream.println:(Ljava/lang/String;)
V
      12: new           #29                 // class java/lang/RuntimeException
      15: dup
      16: ldc           #30                 // String test synchronized
      18: invokespecial #31                 // Method java/lang/RuntimeException."<init>":(Ljava/lang/
String;)V
      21: athrow
      22: astore_2
      23: aload_1
      24: monitorexit
      25: aload_2
      26: athrow
    Exception table:
       from    to  target type
           4    25    22   any

```

可以看到只有一个`monitorexit`了



**（2）synchronized普通同步方法**

```java
public static void main(String[] args) {
    new Demo01().m1();
}

public synchronized void m1() {
    System.out.println("hello world");
}
```

`javap -v ***.class`文件反编译

```java
public synchronized void m1();
    descriptor: ()V
    flags: (0x0021) ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #26                 // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #27                 // String hello world
         5: invokevirtual #28                 // Method java/io/PrintStream.println:(Ljava/lang/String
;)V
         8: return
      LineNumberTable:
        line 51: 0
        line 52: 8
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       9     0  this   Lcom/example/Demo01;
```

调用指令将会检查方法的`ACC_SYNCHRONIZED`访问标志是否被设置。如果设置了，执行线程会将先持有`monitor`然后再执行方法，最后在方法完成(无论是正常完成还是非正常完成)时释放 `monitor`



**（3）synchronized静态同步方法**

```java
public static void main(String[] args) {
    m1();
}
public static synchronized void m1() {
    System.out.println("hello world");
}
```

`javap -v ***.class`文件反编译

```java
 public static synchronized void m1();
    descriptor: ()V
    flags: (0x0029) ACC_PUBLIC, ACC_STATIC, ACC_SYNCHRONIZED
    Code:
      stack=2, locals=0, args_size=0
         0: getstatic     #24                 // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #25                 // String hello world
         5: invokevirtual #26                 // Method java/io/PrintStream.println:(Ljava/lang/String
;)V
         8: return
      LineNumberTable:
        line 51: 0
        line 52: 8
```

`ACC_STATIC`, `ACC_SYNCHRONIZED`访问标志区分该方法是否静态同步方法



**反编译`synchronized`锁的是什么**

**什么是管程monitor**

管程 (英语：`Monitors`，也称为监视器) 是一种程序结构，结构内的多个子程序（对象或模块）形成的多个工作线程互斥访问共享资源。这些共享资源一般是硬件设备或一群变量。对共享变量能够进行的所有操作集中在一个模块中。（把信号量及其操作原语“封装”在一个对象内部）管程实现了在一个时间点，最多只有一个线程在执行管程的某个子程序。管程提供了一种机制，管程可以看做一个软件模块，它是将共享的变量和对于这些共享变量的操作封装起来，形成一个具有一定接口的功能模块，进程可以调用管程来实现进程级别的并发控制。

![image-20220303111735524](https://gitee.com/JKcoding/imgs/raw/master/img01/image-20220303111735524.png)



在`HotSpot`虚拟机中，`monitor`采用`ObjectMonitor`实现

上述C++源码解读：`ObjectMonitor.java→ObjectMonitor.cpp→objectMonitor.hpp`

`objectMonitor.hpp:`![image-20220303111838827](https://gitee.com/JKcoding/imgs/raw/master/img01/image-20220303111838827.png)

`ObjectMonitor`中有几个关键属性

| 属性        | 功能                              |
| ----------- | --------------------------------- |
| _owner      | 指向持有ObjectMonitor对象的线程   |
| _WaitSet    | 存放处于wait状态的线程队列        |
| _EntryList  | 存放处于等待锁block状态的线程队列 |
| _recursions | 锁的重入次数                      |
| _count      | 用来记录该线程获取锁的次数        |

每个对象天生都带着一个对象监视器

对于`synchronized`关键字，我会在《`Synchronized`与锁升级》还会再深度讲解，提前剧透

 `synchronized`必须作用于某个对象中，所以Java在对象的头文件存储了锁的相关信息。锁升级功能主要依赖于 `MarkWord` 中的锁标志位和释放偏向锁标志位，后续讲解锁升级时候我们再加深，目前为了承前启后的学习，对下图先混个眼熟即可，`O(∩_∩)O`

![image-20220303112358646](https://gitee.com/JKcoding/imgs/raw/master/img01/image-20220303112358646.png)



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