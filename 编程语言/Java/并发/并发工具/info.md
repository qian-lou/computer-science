# 1.线程池

## 1.1 构造参数

| 参数名          | 类型                     | 含义                                                         |
| --------------- | ------------------------ | ------------------------------------------------------------ |
| corePoolSize    | int                      | 核心线程数，在线程池完成初始化后，默认情况下，线程池中并没有任务线程，线程池会等待有任务到来时，再去创建新线程去执行任务 |
| maximumPoolSize | int                      | 线程池在核心线程数的基础上，额外增加一些线程，但是新增加的线程数有一个上限，最大量就是maximumPoolSize |
| keepAliveTime   | long                     | 保持存活时间，如果线程池当前的线程数多于corePoolSize，当这些多余线程空闲时间超过keepAliveTime，它们就会被终止 |
| workQueue       | BlockingQueue            | 任务存储队列                                                 |
| threadFactory   | ThreadFactory            | 当线程池需要新的线程的时候，会使用threadFactory来生成新的线程，默认使用Executors.defaultThreadFactory()，创建出来的线程都在同一个线程组，拥有同样的优先级并且都不是守护线程 |
| handler         | RejectedExecutionHandler | 由于线程池无法接受你所提交的任务的拒绝策略                   |

## 1.2 添加线程规则 

1. 如果线程数小于**corePoolSize**，即使其他工作线程处于空闲，也会创建一个新线程来运行任务
2. 如果线程数等于（或大于）**corePoolSize**但少于**maximumPoolSize**，则将任务放入队列
3. 如果队列已满，并且线程数少于**maximumPoolSize**，则创建一个新线程来运行任务
4. 如果列队已满，并且线程数大于或者等于**maximumPoolSize**，则拒绝该任务

工作队列有3种类型：

1. 直接交接：**SynchronousQueue**， 无容量，无缓冲
2. 无界队列：**LinkedBlockingQueue**，可以无限制添加，直到oom，**maximumPoolSize**将无意义
3. 有界队列：**ArrayBlockingQueue**

## 1.3 四种线程池

1. **newFixedThreadPool**

   ```java
   public static ExecutorService newFixedThreadPool(int nThreads) {
       return new ThreadPoolExecutor(nThreads, nThreads,
                                     0L, TimeUnit.MILLISECONDS,
                                     new LinkedBlockingQueue<Runnable>());
   }
   ```

   由于传进去的**LinkedBlockingQueue**是没有容量上限的，所以请求数越来越多，并且无法及时处理完毕的时候，也就是请求堆积的时候，会容易造成占用大量的内存，可能导致OOM

2. **newSingleThreadExecutor**

   ```java
   public static ExecutorService newSingleThreadExecutor() {
       return new FinalizableDelegatedExecutorService
           (new ThreadPoolExecutor(1, 1,
                                   0L, TimeUnit.MILLISECONDS,
                                   new LinkedBlockingQueue<Runnable>()));
   }
   ```

   可以看出，这里和刚才的**newFixedThreadPool**的原理基本一样，只不过线程数直接设置成了1，所以这也会导致同样的问题，也就是当请求堆积的时候，可能会占用大量的内存

3. **newCachedThreadPool**

   ```java
   public static ExecutorService newCachedThreadPool() {
       return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                     60L, TimeUnit.SECONDS,
                                     new SynchronousQueue<Runnable>());
   }
   ```

   **maximumPoolSize**被设置成了**Integer.MAX_VALUE**，这可能会创建数量非常多的线程，甚至导致OOM

4. newScheduledThreadPool

   ```java
   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
       return new ScheduledThreadPoolExecutor(corePoolSize);
   }
   public ScheduledThreadPoolExecutor(int corePoolSize) {
       super(corePoolSize, Integer.MAX_VALUE,
             DEFAULT_KEEPALIVE_MILLIS, MILLISECONDS,
             new DelayedWorkQueue());
   }
   ```

   ```java
   ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
           scheduledExecutorService.scheduleAtFixedRate(()->{
               System.out.println("delay 1 seconds, and excute every 3s.");
           }, 1, 3, TimeUnit.SECONDS);
   ```

   ```shell
   Tue Oct 05 18:21:54 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:21:57 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:22:00 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:22:03 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:22:06 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:22:09 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:22:12 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:22:15 CST 2021delay 1 seconds, and excute every 3s.
   Tue Oct 05 18:22:18 CST 2021delay 1 seconds, and excute every 3s.
   ```

## 1.4 手动创建线程池

### 1.4.1 线程数设置

1. CPU密集型（加密，计算hash等）：最佳线程数为CPU核心数的1~2倍左右

2. 耗时IO型（读写数据库、文件、网络读写等）：最佳线程数一般会大于CPU核心数很多倍，以JVM线程监控显示繁忙情况为依据，保证线程空闲可以衔接上，参考Brain Goetz推荐的计算方法：

   线程数 = CPU核心数 * （1 + 平均等待时间 / 平均工作时间）



