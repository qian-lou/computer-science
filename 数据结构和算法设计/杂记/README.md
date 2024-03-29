## 设计RandomPool结构 

【题目】 

​	设计一种结构，在该结构中有如下三个功能: 

- insert(key): 将某个key加入到该结构，做到不重复加入 
- delete(key): 将原本在结构中的某个key移除 
- getRandom(): 等概率随机返回结构中的任何一个key。 

【要求】 

​	Insert、delete和getRandom方法的时间复杂度都是O(1)

```java
public class Pool<K> {
    private HashMap<K, Integer> keyIndexMap = new HashMap<>();
    private HashMap<Integer, K> indexKeyMap = new HashMap<>();
    private int size;

    public void insert(K key) {
        if (!this.keyIndexMap.containsKey(key)) {
            this.keyIndexMap.put(key, this.size);
            this.indexKeyMap.put(this.size++, key);
        }
    }

    public void delete(K key) {
        if (this.keyIndexMap.containsKey(key)) {
            int deleteIndex = this.keyIndexMap.get(key);
            int lastIndex = --this.size;
            K lastKey = this.indexKeyMap.get(lastIndex);
            this.keyIndexMap.put(lastKey, deleteIndex);
            this.indexKeyMap.put(deleteIndex, lastKey);
            this.keyIndexMap.remove(key);
            this.indexKeyMap.remove(lastIndex);
        }
    }

    public K getRandom() {
        if (this.size == 0) {
            return null;
        }
        int randomIndex = (int) (Math.random() * this.size);
        return this.indexKeyMap.get(randomIndex);
    }
}
```



## 详解布隆过滤器

先了解位图，例如定义一个int数组，长度为3，则总共有 32 * 3 =  96

```java
//总共96位
private int[] arr = new int[3];

public void setBitNToZero(int n) {
    int numIndex = n / 32;
    int bitIndex = n % 32;
    //获取bitIndex的状态
    int status = (arr[numIndex] >> bitIndex) & 1;
    if (status == 0) return;
    //将bitIndex位设置成0
    arr[numIndex] = arr[numIndex] & (~(1 << bitIndex));
}

public void setBitNToOne(int n) {
    int numIndex = n / 32;
    int bitIndex = n % 32;
    //获取bitIndex的状态
    int status = (arr[numIndex] >> bitIndex) & 1;
    if (status == 1) return;
    //将bitIndex位设置成1
    arr[numIndex] = arr[numIndex] | (1 << bitIndex);
}
```

下面内容来自原文https://www.cnblogs.com/liyulong1982/p/6013002.html

直观的说，bloom算法类似一个hashset，用来判断某个元素（key）是否在某个集合中。和一般的hashset不同的是，这个算法无需存储key的值，对于每个key，只需要k个比特位，每个存储一个标志，用来判断key是否在集合中。

算法：
1、首先需要k个hash函数，每个函数可以把key散列成为1个整数
2、初始化时，需要一个长度为n比特的数组，每个比特位初始化为0
3、某个key加入集合时，用k个hash函数计算出k个散列值，并把数组中对应的比特位置为1
4、判断某个key是否在集合时，用k个hash函数计算出k个散列值，并查询数组中对应的比特位，如果所有的比特位都是1，认为在集合中。

优点：不需要存储key，节省空间

缺点：
1、算法判断key在集合中时，有一定的概率key其实不在集合中
2、无法删除

**典型的应用场景**：
某些存储系统的设计中，会存在空查询缺陷：当查询一个不存在的key时，需要访问慢设备，导致效率低下。
比如一个前端页面的缓存系统，可能这样设计：先查询某个页面在本地是否存在，如果存在就直接返回，如果不存在，就从后端获取。但是当频繁从缓存系统查询一个页面时，缓存系统将会频繁请求后端，把压力导入后端。

这是只要增加一个bloom算法的服务，后端插入一个key时，在这个服务中设置一次，需要查询后端时，先判断key在后端是否存在，这样就能避免后端的压力。

 

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061546385.png)

 

**布隆过滤器［1］**（Bloom Filter）是由布隆（Burton Howard Bloom）在1970年提出的。它实际上是由一个很长的二进制向量和一系列随机映射函数组成，布隆过滤器可以用于检索一个元素是否在一个集合中。它的优点是空间效率和查询时间都远远超过一般的算法，缺点是有一定的误识别率（假正例False positives，即Bloom Filter报告某一元素存在于某集合中，但是实际上该元素并不在集合中）和删除困难，但是没有识别错误的情形（即假反例False negatives，如果某个元素确实没有在该集合中，那么Bloom Filter 是不会报告该元素存在于集合中的，所以不会漏报）。

在日常生活中，包括在设计计算机软件时，我们经常要判断一个元素是否在一个集合中。比如在字处理软件中，需要检查一个英语单词是否拼写正确（也就是要判断 它是否在已知的字典中）；在 FBI，一个嫌疑人的名字是否已经在嫌疑名单上；在网络爬虫里，一个网址是否被访问过等等。最直接的方法就是将集合中全部的元素存在计算机中，遇到一个新 元素时，将它和集合中的元素直接比较即可。一般来讲，计算机中的集合是用哈希表（hash table）来存储的。它的好处是快速准确，缺点是费存储空间。当集合比较小时，这个问题不显著，但是当集合巨大时，哈希表存储效率低的问题就显现出来 了。比如说，一个象 Yahoo,Hotmail 和 Gmai 那样的公众电子邮件（email）提供商，总是需要过滤来自发送垃圾邮件的人（spamer）的垃圾邮件。一个办法就是记录下那些发垃圾邮件的 email 地址。由于那些发送者不停地在注册新的地址，全世界少说也有几十亿个发垃圾邮件的地址，将他们都存起来则需要大量的网络服务器。如果用哈希表，每存储一亿 个 email 地址， 就需要 1.6GB 的内存（用哈希表实现的具体办法是将每一个 email 地址对应成一个八字节的信息指纹（详见：[googlechinablog.com/2006/08/blog-post.html）](http://googlechinablog.com/2006/08/blog-post.html)， 然后将这些信息指纹存入哈希表，由于哈希表的存储效率一般只有 50%，因此一个 email 地址需要占用十六个字节。一亿个地址大约要 1.6GB， 即十六亿字节的内存）。因此存贮几十亿个邮件地址可能需要上百 GB 的内存。除非是超级计算机，一般服务器是无法存储的［2］。（该段引用谷歌数学之美：http://www.google.com.hk/ggblog/googlechinablog/2007/07/bloom-filter_7469.html）

### 基本概念

如果想判断一个元素是不是在一个集合里，一般想到的是将所有元素保存起来，然后通过比较确定。链表，树等等数据结构都是这种思路. 但是随着集合中元素的增加，我们需要的存储空间越来越大，检索速度也越来越慢。不过世界上还有一种叫作散列表（又叫哈希表，Hash table）的数据结构。它可以通过一个Hash函数将一个元素映射成一个位阵列（Bit Array）中的一个点。这样一来，我们只要看看这个点是不是 1 就知道可以集合中有没有它了。这就是布隆过滤器的基本思想。

Hash面临的问题就是冲突。假设 Hash 函数是良好的，如果我们的位阵列长度为 m 个点，那么如果我们想将冲突率降低到例如 1%, 这个散列表就只能容纳 m/100 个元素。显然这就不叫空间有效了（Space-efficient）。解决方法也简单，就是使用多个 Hash，如果它们有一个说元素不在集合中，那肯定就不在。如果它们都说在，虽然也有一定可能性它们在说谎，不过直觉上判断这种事情的概率是比较低的。

### 优点

相比于其它的数据结构，布隆过滤器在空间和时间方面都有巨大的优势。布隆过滤器存储空间和插入/查询时间都是常数。另外, Hash 函数相互之间没有关系，方便由硬件并行实现。布隆过滤器不需要存储元素本身，在某些对保密要求非常严格的场合有优势。

布隆过滤器可以表示全集，其它任何数据结构都不能；

k 和 m 相同，使用同一组 Hash 函数的两个布隆过滤器的交并差运算可以使用位操作进行。

### 缺点

但是布隆过滤器的缺点和优点一样明显。误算率（False Positive）是其中之一。随着存入的元素数量增加，误算率随之增加。但是如果元素数量太少，则使用散列表足矣。

另外，一般情况下不能从布隆过滤器中删除元素. 我们很容易想到把位列阵变成整数数组，每插入一个元素相应的计数器加1, 这样删除元素时将计数器减掉就可以了。然而要保证安全的删除元素并非如此简单。首先我们必须保证删除的元素的确在布隆过滤器里面. 这一点单凭这个过滤器是无法保证的。另外计数器回绕也会造成问题。

### False positives 概率推导

假设 Hash 函数以等概率条件选择并设置 Bit Array 中的某一位，m 是该位数组的大小，k 是 Hash 函数的个数，那么位数组中某一特定的位在进行元素插入时的 Hash 操作中没有被置位的概率是：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061546015.png)

那么在所有 k 次 Hash 操作后该位都没有被置 "1" 的概率是：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547012.png)

如果我们插入了 n 个元素，那么某一位仍然为 "0" 的概率是：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547508.png)

因而该位为 "1"的概率是：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547874.png)

现在检测某一元素是否在该集合中。标明某个元素是否在集合中所需的 k 个位置都按照如上的方法设置为 "1"，但是该方法可能会使算法错误的认为某一原本不在集合中的元素却被检测为在该集合中（False Positives），该概率由以下公式确定：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547422.png)

其实上述结果是在假定由每个 Hash 计算出需要设置的位（bit） 的位置是相互独立为前提计算出来的，不难看出，随着 m （位数组大小）的增加，假正例（False Positives）的概率会下降，同时随着插入元素个数 n 的增加，False Positives的概率又会上升，对于给定的m，n，如何选择Hash函数个数 k 由以下公式确定：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547039.png)

此时False Positives的概率为：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547730.png)

而对于给定的False Positives概率 p，如何选择最优的位数组大小 m 呢，

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547386.png)

上式表明，位数组的大小最好与插入元素的个数成线性关系，对于给定的 m，n，k，假正例概率最大为：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547923.png)

###  

下图是布隆过滤器假正例概率 p 与位数组大小 m 和集合中插入元素个数 n 的关系图，假定 Hash 函数个数选取最优数目：![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547273.png)

 

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547321.png)

### Bloom Filter 用例

Google 著名的分布式数据库 Bigtable 使用了布隆过滤器来查找不存在的行或列，以减少磁盘查找的IO次数［3］。

Squid 网页代理缓存服务器在 [cache digests ](http://wiki.squid-cache.org/SquidFaq/CacheDigests)中使用了也布隆过滤器［4］。

Venti 文档存储系统也采用布隆过滤器来检测先前存储的数据［5］。

SPIN 模型检测器也使用布隆过滤器在大规模验证问题时跟踪可达状态空间［6］。

Google Chrome浏览器使用了布隆过滤器加速安全浏览服务［7］。

在很多Key-Value系统中也使用了布隆过滤器来加快查询过程，如 Hbase，Accumulo，Leveldb，一般而言，Value 保存在磁盘中，访问磁盘需要花费大量时间，然而使用布隆过滤器可以快速判断某个Key对应的Value是否存在，因此可以避免很多不必要的磁盘IO操作，只是引入布隆过滤器会带来一定的内存消耗，下图是在Key-Value系统中布隆过滤器的典型使用：

![img](https://gitee.com/JKcoding/imgs/raw/master/img/202202061547259.png)

###  

### 布隆过滤器相关扩展

####  Counting filters

基本的布隆过滤器不支持删除（Deletion）操作，但是 Counting filters 提供了一种可以不用重新构建布隆过滤器但却支持元素删除操作的方法。在Counting filters中原来的位数组中的每一位由 bit 扩展为 n-bit 计数器，实际上，基本的布隆过滤器可以看作是只有一位的计数器的Counting filters。原来的插入操作也被扩展为把 n-bit 的位计数器加1，查找操作即检查位数组非零即可，而删除操作定义为把位数组的相应位减1，但是该方法也有位的算术溢出问题，即某一位在多次删除操作后可能变成负值，所以位数组大小 m 需要充分大。另外一个问题是Counting filters不具备伸缩性，由于Counting filters不能扩展，所以需要保存的最大的元素个数需要提前知道。否则一旦插入的元素个数超过了位数组的容量，false positive的发生概率将会急剧增加。当然也有人提出了一种基于 D-left Hash 方法实现支持删除操作的布隆过滤器，同时空间效率也比Counting filters高。

#### Data synchronization

Byers等人提出了使用布隆过滤器近似数据同步［9］。

#### Bloomier filters

Chazelle 等人提出了一个通用的布隆过滤器，该布隆过滤器可以将某一值与每个已经插入的元素关联起来，并实现了一个关联数组Map［10］。与普通的布隆过滤器一样，Chazelle实现的布隆过滤器也可以达到较低的空间消耗，但同时也会产生false positive，不过，在Bloomier filter中，某 key 如果不在 map 中，false positive在会返回时会被定义出的。该Map 结构不会返回与 key 相关的在 map 中的错误的值。



## 并查集

```java
public static class Element<V> {
    public V value;
    public Element(V value) {
        this.value = value;
    }
}

public static class UnionFindSet<V> {
    public HashMap<V, Element<V>> elementMap;
    public HashMap<Element<V>, Element<V>> fatherMap;
    public HashMap<Element<V>, Integer> sizeMap;

    public UnionFindSet(List<V> list) {
        elementMap = new HashMap<>();
        fatherMap = new HashMap<>();
        sizeMap = new HashMap<>();
        for (V value : list) {
            Element<V> element = new Element<>(value);
            elementMap.put(value, element);
            fatherMap.put(element, element);
            sizeMap.put(element, 1);
        }
    }

    public boolean isSameSet(V a, V b) {
        if (elementMap.containsKey(a) && elementMap.containsKey(b)) {
            return findHead(elementMap.get(a)) == findHead(elementMap.get(b));
        }
        return false;
    }

    public void union(V a, V b) {
        if (elementMap.containsKey(a) && elementMap.containsKey(b)) {
            Element<V> aF = findHead(elementMap.get(a));
            Element<V> bF = findHead(elementMap.get(b));
            if (aF != bF) {
                Element<V> big = sizeMap.get(aF) >= sizeMap.get(bF) ? aF : bF;
                Element<V> small = big == aF ? bF : aF;
                fatherMap.put(small, big);
                sizeMap.put(big, sizeMap.get(aF) + sizeMap.get(bF));
                sizeMap.remove(small);
            }
        }
    }

    private Element<V> findHead(Element<V> element) {
        Element<V> cur = element;
        Stack<Element<V>> path = new Stack<>();
        while (cur != fatherMap.get(cur)) {
            path.push(cur);
            cur = fatherMap.get(cur);
        }
        //扁平化并查集
        while (!path.isEmpty()) {
            fatherMap.put(path.pop(), cur);
        }
        return cur;
    }
}
```

```java
public static void main(String[] args) {
    Integer[] arr = {1,2,3,4,5,6,7,8,9};
    UnionFindSet<Integer> unionFindSet = new UnionFindSet<Integer>(Arrays.asList(arr));
    unionFindSet.union(1,2);
    unionFindSet.union(1, 3);
    unionFindSet.union(3, 9);
    System.out.println(unionFindSet.isSameSet(2, 3));
    System.out.println(unionFindSet.isSameSet(1, 9));
}
```



## 岛问题 

【题目】 

一个矩阵中只有0和1两种值，每个位置都可以和自己的上、下、左、右 四个位置相连，如 

果有一片1连在一起，这个部分叫做一个岛，求一个矩阵中有多少个岛? 

【举例】 

```
001010 

111010 

100100 

000000 
```

这个矩阵中有三个岛 

思路：遍历每一个数，只要遇到1，就进行感染过程即调用`infect`方法

`infect`方法会将连着的区域全部变成2

时间复杂度：`O(N*M)`

```java
public static int countIslands(int[][] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int N = arr.length;
    int M = arr[0].length;
    int res = 0;
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            if (arr[i][j] == 1) {
                res++;
                infect(arr, i, j, N, M);
            }
        }
    }
    return res;
}
private static void infect(int[][] arr, int i, int j, int N, int M) {
    if (i < 0 || i >= N || j < 0 || j >= M || arr[i][j] != 1) {
        return;
    }
    arr[i][j] = 2;
    infect(arr, i + 1, j, N, M);
    infect(arr, i - 1, j, N, M);
    infect(arr, i, j + 1, N, M);
    infect(arr, i , j - 1, N, M);
}
```

【进阶】 

如何设计一个并行算法解决这个问题

将矩阵拆分成若干份，每一份使用一个cpu进行处理，统计当前的岛屿数量和边界信息，然后合并各个份的时候，如果相邻的两个点是岛屿连接点，则岛屿总数量减一，直到所有边界合并完成（合并使用并查集）



## KMP

```java 
public class Code {

    public static int getIndexOf(String s, String m) {
        if (s == null || m == null || m.length() < 1 || s.length() < m.length()) {
            return -1;
        }
        char[] str1 = s.toCharArray();
        char[] str2 = m.toCharArray();
        int i1 = 0;
        int i2 = 0;
        //O(M)
        int[] next = getNextArray(str2);
        //O(N)
        while (i1 < str1.length && i2 < str2.length) {
            if (str1[i1] == str2[i2]) {
                i1++;
                i2++;
            } else if (next[i2] == -1) {
                i1++;
            } else {
                i2 = next[i2];
            }
        }
        return i2 == str2.length ? i1 - i2 : -1;
    }

    public static int[] getNextArray(char[] ms) {
        if (ms.length == 1) {
            return new int[] {-1};
        }
        int[] next = new int[ms.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2;
        int cn = 0;
        while (i < next.length) {
            if (ms[i - 1] == ms[cn]) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }

    public static void main(String[] args) {
        String s = "abcabcababaccc";
        String m = "cab";
        int index = getIndexOf(s, m);
        System.out.println(index);
    }
}
```



## 由一个代表题目，引出一种结构 

【题目】 

有一个整型数组arr和一个大小为w的窗口从数组的最左边滑到最右边，窗口每次 向右边滑 

一个位置。 

例如，数组为[4,3,5,4,3,3,6,7]，窗口大小为3时: 

```
[4 3 5]4 3 3 6 7 
4[3 5 4]3 3 6 7 
4 3[5 4 3]3 6 7 
4 3 5[4 3 3]6 7 
4 3 5 4[3 3 6]7 
4 3 5 4 3[3 6 7] 
```

窗口中最大值为5 窗口中最大值为5 窗口中最大值为5 窗口中最大值为4 窗口中最大值为6  窗口中最大值为7 

如果数组长度为n，窗口大小为w，则一共产生n-w+1个窗口的最大值。 

请实现一个函数。 输入:整型数组arr，窗口大小为w。 

输出:一个长度为n-w+1的数组res，res[i]表示每一种窗口状态下的 以本题为例，结果应该返回{5,5,5,4,6,7}。

```java
public static int[] getMaxWindow(int[] arr, int w) {
    if (arr == null || w < 1 || arr.length < w) {
        return null;
    }
    LinkedList<Integer> qmax = new LinkedList<>();
    int[] res = new int[arr.length - w + 1];
    int index = 0;
    for (int i = 0; i < arr.length; i++) {
        while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[i]) {
            //将队列里面比i位置小的全部弹出
            qmax.pollLast();
        }
        qmax.addLast(i);
        //过期的丢弃
        if (qmax.peekFirst() == i - w) {
            qmax.pollFirst();
        }
        //形成窗口
        if (i >= w - 1) {
            res[index++] = arr[qmax.peekFirst()];
        }
    }
    return res;
}
```



## 单调栈

在数组中想找到一个数，左边和右边比这个数小、且离这个数最近的位置。 如果对每一个数都想求这样的信息，能不能整体代价达到O(N)？需要使用到单调栈结构，单调栈结构的原理和实现

没重复值：

```java
public static int[][] getNearLessNoRepeat(int[] arr) {
    int[][] res = new int[arr.length][2];
    Stack<Integer> stack = new Stack<>();
    for (int i = 0; i < arr.length; i++) {
        while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
            int popIndex = stack.pop();
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
            res[popIndex][0] = leftLessIndex;
            res[popIndex][1] = i;
        }
        stack.push(i);
    }
    while (!stack.isEmpty()) {
        int popIndex = stack.pop();
        int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
        res[popIndex][0] = leftLessIndex;
        res[popIndex][1] = -1;
    }
    return res;
}
```

有重复值：

```java
public static int[][] getNearLess(int[] arr) {
    int[][] res = new int[arr.length][2];
    Stack<List<Integer>> stack = new Stack<>();
    for (int i = 0; i < arr.length; i++) {
        while (!stack.isEmpty() && arr[stack.peek().get(0)] < arr[i]) {
            List<Integer> pops = stack.pop();
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
            for (Integer pop : pops) {
                res[pop][0] = leftLessIndex;
                res[pop][1] = i;
            }
        }
        if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
            stack.peek().add(i);
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(i);
            stack.push(list);
        }
    }
    while (!stack.isEmpty()) {
        List<Integer> pops = stack.pop();
        int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
        for (Integer pop : pops) {
            res[pop][0] = leftLessIndex;
            res[pop][1] = -1;
        }
    }
    return res;
}
```



## 树形dp套路

**树形dp套路第一步**： 

以某个节点X为头节点的子树中，分析答案有哪些可能性，并且这种分析是以X的左子树、X的右子树和X整棵树的角度来考虑可能性的 

**树形dp套路第二步**： 

根据第一步的可能性分析，列出所有需要的信息 

**树形dp套路第三步**： 

合并第二步的信息，对左树和右树提出同样的要求，并写出信息结构 

**树形dp套路第四步**： 

设计递归函数，递归函数是处理以X为头节点的情况下的答案。 包括设计递归的basecase，默认直接得到左树和右树的所有信息，以及把可能性做整合，并且要返回第三步的信息结构这四个小步骤



## 叉树节点间的最大距离问题 

从二叉树的节点a出发，可以向上或者向下走，但沿途的节点只能经过一次，到达节点b时路径上的节点个数叫作a到b的距离，那么二叉树任何两个节点之间都有距离，求整棵树上的最大距离。

```java
public static class Node {
    public int value;
    public Node left;
    public Node right;

    public Node(int value) {
        this.value = value;
    }
}
public static class Info {
    public int maxDistance;
    public int height;

    public Info(int maxDistance, int height) {
        this.maxDistance = maxDistance;
        this.height = height;
    }
}
//求整棵树上的最大距离
public static int maxDistance(Node head) {
    return process(head).maxDistance;
}

private static Info process(Node x) {
    if (x == null) {
        return new Info(0, 0);
    }
    Info leftInfo = process(x.left);
    Info rightInfo = process(x.right);
    int p1 = leftInfo.maxDistance;
    int p2 = rightInfo.maxDistance;
    int p3 = leftInfo.height + 1 + rightInfo.height;
    int maxDistance = Math.max(p3, Math.max(p1, p2));
    int height = Math.max(leftInfo.height, rightInfo.height) + 1;
    return new Info(maxDistance, height);
}
```





## 派对的最大快乐值 

员工信息的定义如下: 

```java
class Employee { 
	public int happy; // 这名员工可以带来的快乐值 
	List<Employee> subordinates; // 这名员工有哪些直接下级 
}
```

公司的每个员工都符合 Employee 类的描述。整个公司的人员结构可以看作是一棵标准的、 没有环的多叉树。树的头节点是公司唯一的老板。除老板之外的每个员工都有唯一的直接上级。 叶节点是没有任何下属的基层员工(subordinates列表为空)，除基层员工外，每个员工都有一个或多个直接下级。 这个公司现在要办party，你可以决定哪些员工来，哪些员工不来。但是要遵循如下规则。 

1. 如果某个员工来了，那么这个员工的所有直接下级都不能来 
2. 派对的整体快乐值是所有到场员工快乐值的累加 
3. 你的目标是让派对的整体快乐值尽量大 

给定一棵多叉树的头节点boss，请返回派对的最大快乐值。

```java
public class Code9 {
    public static class Employee {
        public int happy;
        List<Employee> subordinates;
    }

    public static class Info {
        public int laiMaxHappy;
        public int buMaxHappy;

        public Info(int laiMaxHappy, int buMaxHappy) {
            this.laiMaxHappy = laiMaxHappy;
            this.buMaxHappy = buMaxHappy;
        }
    }

    public static Info process(Employee x) {
        if (x.subordinates.isEmpty()) {
            return new Info(x.happy, 0);
        }
        int lai = x.happy;
        int bu = 0;
        for (Employee next : x.subordinates) {
            Info nextInfo = process(next);
            lai += nextInfo.buMaxHappy;
            bu += Math.max(nextInfo.buMaxHappy, nextInfo.laiMaxHappy);
        }
        return new Info(lai, bu);
    }
	//求最大快乐值
    public static int maxHappy(Employee boss) {
        Info headInfo = process(boss);
        return Math.max(headInfo.laiMaxHappy, headInfo.buMaxHappy);
    }
}
```



## Morris遍历 

一种遍历二叉树的方式，并且时间复杂度`O(N)`，额外空间复杂度`O(1)`  通过利用原树中大量空闲指针的方式，达到节省空间的目的

**Morris遍历细节** 

假设来到当前节点cur，开始时cur来到头节点位置 

1）如果cur没有左孩子，cur向右移动(cur = cur.right) 

2）如果cur有左孩子，找到左子树上最右的节点mostRight： 

​	a.如果mostRight的右指针指向空，让其指向cur，然后cur向左移动(cur = cur.left) 

​	b.如果mostRight的右指针指向cur，让其指向null，然后cur向右移动(cur = cur.right) 

3）cur为空时遍历停止

```java
public static class Node {
        public Node left;
        public Node right;
}
public static void morris(Node head) {
    if (head == null) {
        return;
    }
    Node cur = head;
    Node mostRight = null;
    while (cur != null) {
        mostRight = cur.left;
        if (mostRight != null) {
            while (mostRight.right != null && mostRight.right != cur) {
                mostRight = mostRight.right;
            }
            if (mostRight.right == null) {
                mostRight.right = cur;
                cur = cur.left;
                continue;
            } else {
                mostRight.right = null;
            }
        }
        cur = cur.right;
    }
}
```

**Morris遍历的实质** 

建立一种机制，对于没有左子树的节点只到达一次，对于有左子树的节点会到达两次 

**前中后序遍历**

```java
/**
 * @Description TODO
 * @Author 千楼
 * @Version 1.0
 **/
public class Code10 {

    public static class Node {
        public Node left;
        public Node right;
        public int value;

        public Node(int value) {
            this.value = value;
        }
        public Node() {
        }
    }
	//前序遍历
    public static void morris_pre(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    System.out.print(cur.value + " ");
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            } else {
                System.out.print(cur.value + " ");
            }
            cur = cur.right;
        }
        System.out.println();
    }
    //中序遍历
    public static void morris_in(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            System.out.print(cur.value + " ");
            cur = cur.right;
        }
        System.out.println();
    }
    //后续遍历
    public static void morris_post(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                    //第二次出现的时候，cur的左子树的右边界的逆序
                    printEdge(cur.left);
                }
            }
            cur = cur.right;
        }
        printEdge(head);
        System.out.println();
    }

    public static void printEdge(Node x) {
        Node tail = reverseEdge(x);
        Node cur = tail;
        while (cur != null) {
            System.out.print(cur.value + " ");
            cur = cur.right;
        }
        reverseEdge(tail);
    }
    public static Node reverseEdge(Node from) {
        Node pre = null;
        Node next = null;
        while (from != null) {
            next = from.right;
            from.right = pre;
            pre = from;
            from = next;
        }
        return pre;
    }




    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(7);
        n1.left = n2;
        n1.right = n3;
        n2.left = n4;
        n2.right = n5;
        n3.left = n6;
        n3.right = n7;
        morris_pre(n1);
        morris_in(n1);
        morris_post(n1);
    }

}
```



## **使用`Morris`遍历判断是否`BST`**

时间复杂度O(N)  空间复杂度O(1)

```java
public static boolean isBST(Node head) {
    if (head == null) {
        return true;
    }
    Node cur = head;
    Node mostRight = null;
    int preVal = Integer.MIN_VALUE;
    while (cur != null) {
        mostRight = cur.left;
        if (mostRight != null) {
            while (mostRight.right != null && mostRight.right != cur) {
                mostRight = mostRight.right;
            }
            if (mostRight.right == null) {
                mostRight.right = cur;
                cur = cur.left;
                continue;
            } else {
                mostRight.right = null;
            }
        }
        if (preVal > cur.value) {
            return false;
        }
        preVal = cur.value;
        cur = cur.right;
    }
    return true;
}
```





## 大数据题目的解题技巧

1）哈希函数可以把数据按照种类均匀分流 

2）布隆过滤器用于集合的建立与查询，并可以节省大量空间 

3）一致性哈希解决数据服务器的负载管理问题 

4）利用并查集结构做岛问题的并行计算 

5）位图解决某一范围上数字的出现情况，并可以节省大量空间 

6）利用分段统计思想、并进一步节省大量空间 

7）利用堆、外排序来做多个处理单元的结果合并 





## 有一个包含100亿个URL的大文件，假设每个URL占用64B，请找出其中所有重复的URL 

【补充】 

某搜索公司一天的用户搜索词汇是海量的(百亿数据量)，请设计一种求出每天热门Top100词汇的可行办法

思路：利用哈希函数分流到多个文件中，每个文件属于同一种类型的URL，然后再统计，统计完成后合并数据

补充：每个文件的大根堆求Topk，然后总的再用大根堆求top100





## 32位无符号整数的范围是0~4294967295，现在有40亿个无符号整数，可以使用最多1GB的内存，找出所有出现了两次的数。 

位图解决，使用2个bit位统计

【补充】 

可以使用最多10MB的内存，怎么找到这40亿个整数的中位数？

使用10kb内存解决方法：定义无符号 unsigned int []， 长度是10kb/4b约等于2500左右的长度，取2048长度，

然后将所有数字进行词频统计，0~2048的统计，后面以此类型，然后看看第20亿个数在哪个范围内，然后使用同样的词频统计方法对该范围继续统计，直到找到中位数





## [位运算的题目 ]给定两个有符号32位整数a和b，返回a和b中较大的。 

【要求】 

不用做任何比较判断。

```java 
public static int flip(int n) {
    return n ^ 1;
}

public static int sign(int n) {
    return flip((n >> 31) & 1);
}

public static int getMax(int a, int b) {
    int c = a - b;
    int sa = sign(a);
    int sb = sign(b);
    int sc = sign(c);
    //a和b符号不一样 1  一样 0
    int difSab = sa ^ sb;
    //a和b符号不一样 0  一样 1
    int sameSab = flip(difSab);
    //如果 a b符号不一样， sa=1,说明a为正，b是负数，返回a returnA=1；如果sa=0，说明a为负数，b是整数，返回b，returnA=0
    //如果a b符号一样，则肯定不会溢出，使用sameSab * sc;
    int returnA = difSab * sa + sameSab * sc;
    int returnB = flip(returnA);
    return a * returnA + b * returnB;
}
```



## 判断一个32位正数是不是2的幂、4的幂

```java
public static boolean is2Power(int n) {
    return (n & (n - 1)) == 0;
}

public static boolean is4Power(int n) {
    return (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
}
```





## 给定两个有符号32位整数a和b，不能使用算术运算符，分别实现a和b的加、减、乘、除运算

【要求】 

如果给定a、b执行加减乘除的运算结果就会导致数据的溢出，那么你实现的函数不必对此负责，除此之外请保证计算过程不发生溢出

```java
//加法
public static int add(int a, int b) {
        int sum = a;
        while (b != 0) {
            sum = a ^ b;
            b = (a & b) << 1;
            a = sum;
        }
        return sum;
    }

    public static int negNum(int n) {
        return add(~n, 1);
    }
	//减法
    public static int minus(int a, int b) {
        return add(a, negNum(b));
    }
	//乘法
    public static int mul(int a, int b) {
        int sum = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                sum = add(sum, a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return sum;
    }

    public static boolean isNeg(int n) {
        return n < 0;
    }
	//除法
    public static int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a;
        int y = isNeg(b) ? negNum(b) : b;
        int res = 0;
        for (int i = 31; i > -1 ; i = minus(i, 1)) {
            if ((x >> i) >= y) {
                res |= (1 << i);
                x = minus(x , y << i);
            }
        }
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }
```





## 机器人达到指定位置方法数 

【题目】 

假设有排成一行的 N 个位置，记为 1~N，N 一定大于或等于 2。开始时机器人在其中的 M 位置上(M 一定是 1~N 中的一个)，机器人可以往左走或者往右走，如果机器人来到 1 位置， 那么下一步只能往右来到 2 位置;如果机器人来到 N 位置，那么下一步只能往左来到 N-1 位置。 规定机器人必须走 K 步，最终能来到 P 位置(P 也一定是 1~N 中的一个)的方法有多少种。给定四个参数 N、M、K、P，返回方法数。 

【举例】 

N=5,M=2,K=3,P=3 

上面的参数代表所有位置为 1 2 3 4 5。机器人最开始在 2 位置上，必须经过 3 步，最后到 达 3 位置。走的方法只有如下 3 种: 

1)从2到1，从1到2，从2到3 

2)从2到3，从3到2，从2到3 

3)从2到3，从3到4，从4到3 

所以返回方法数 3。 N=3,M=1,K=3,P=3 

上面的参数代表所有位置为 1 2 3。机器人最开始在 1 位置上，必须经过 3 步，最后到达 3 位置。怎么走也不可能，所以返回方法数 0。

简单递归

```java 
public static int walkWays(int N, int P, int M, int K) {
      return f(N, P, K, M);
    }

public static int f(int N, int E, int rest, int cur) {
    if (rest == 0) {
        return cur == E ? 1 : 0;
    }
    if (cur == 1) {
        return f(N, E, rest - 1, 2);
    }
    if (cur == N) {
        return f(N, E, rest - 1, N - 1);
    }
    return f(N, E, rest - 1, cur - 1) + f(N, E, rest - 1, cur + 1);
}
```

记忆化搜索

```java
public static int walkWays(int N, int P, int M, int K) {
	  int[][] dp = new int[K + 1][N + 1];
	  //dp全部初始化为-1
      return f(N, P, K, M, dp);
    }
public static int f(int N, int E, int rest, int cur, int[][] dp) {
    if (dp[rest][cur] != -1) {
        return dp[rest][cur];
    }
    if (rest == 0) {
        dp[rest][cur] = cur == E ? 1 : 0;
    } else if (cur == 1) {
        dp[rest][cur] = f(N, E, rest - 1, 2, dp);
    } else if (cur == N) {
        dp[rest][cur] = f(N, E, rest - 1, N - 1, dp);
    } else {
        dp[rest][cur] = f(N, E, rest - 1, cur - 1, dp) + f(N, E, rest - 1, cur + 1, dp);
    }
    return dp[rest][cur];
}
```

动态规划

```java
public static int walkWays(int N, int P, int M, int K) {
    int[][] dp = new int[K + 1][N + 1];
    dp[0][P] = 1;
    for (int i = 1; i <= K; i++) {
        for (int j = 1; j <= N; j++) {
            if (j == N) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j + 1];
            }
        }
    }
    return dp[K][M];
}
```



## 换钱的最少货币数 

【题目】 

给定数组 arr，arr 中所有的值都为正数且不重复。每个值代表一种面值的货币，每种面值的货币可以使用1张，再给定一个整数 aim，代表要找的钱数，求组成 aim 的最少货币数。

简单递归

```java
public static int process(int[] arr, int index, int rest) {
    if (rest < 0) {
        return -1;
    }
    if (rest == 0) {
        return 0;
    }
    if (index == arr.length) {
        return -1;
    }
    int p1 = process(arr, index + 1, rest);
    int p2 = process(arr, index + 1, rest - arr[index]);
    if (p1 == -1 && p2 == -1) {
        return -1;
    }
    if (p1 == -1 || p2 == -1) {
        return p1 == -1 ? p2 + 1 : p1;
    }
    return Math.min(p1, p2 + 1);
}

public static int minCoin(int[] arr, int aim) {
    return process(arr, 0, aim);
}
```

记忆化搜索参考上面**机器人达到指定位置方法数**实现

递归改动态规划

```java
public static int minCoin2(int[] arr, int aim) {
    int N = arr.length;
    int[][] dp = new int[N + 1][aim + 1];
    for (int row = 0; row <= N; row++) {
        dp[row][0] = 0;
    }
    for (int col = 1; col <= aim; col++) {
        dp[N][col] = -1;
    }
    for (int index = N - 1; index >= 0; index--) {
        for (int rest = 1; rest <= aim; rest++) {
           int p1 = dp[index + 1][rest];
           int p2 = -1;
           if (rest - arr[index] >= 0) {
               p2 = dp[index + 1][rest - arr[index]];
           }
           if (p1 == -1 && p2 == -1) {
               dp[index][rest] = -1;
           } else {
               if (p1 == -1) {
                   dp[index][rest] = p2 + 1;
               } else if (p2 == -1) {
                   dp[index][rest] = p1;
               } else {
                   dp[index][rest] = Math.min(p1, p2 + 1);
               }
           }

        }
    }
    return dp[0][aim];
}
```





## 象棋中马的跳法 

【题目】 

请同学们自行搜索或者想象一个象棋的棋盘，然后把整个棋盘放入第一象限，棋盘的最左下角是(0,0)位置。那么整个棋盘就是横坐标上9条线、纵坐标上10条线的一个区域。给你三个参数，x，y，k，返回如果“马”从(0,0)位置出发，必须走k步，最后落在(x,y)上的方法数有多少种？

递归

```java
public static int process(int x, int y, int step) {
    if (x < 0 || x > 8 || y < 0 || y > 9) {
        return 0;
    }
    if (step == 0) {
        return (x == 0 && y == 0) ? 1 : 0;
    }
    return  process(x - 1, y + 2, step - 1) +
            process(x + 1, y + 2, step - 1) +
            process(x + 2, y + 1, step - 1) +
            process(x + 2, y - 1, step - 1) +
            process(x + 1, y - 2, step - 1) +
            process(x - 1, y - 2, step - 1) +
            process(x - 2, y - 1, step - 1) +
            process(x - 2, y + 1, step - 1);
}
```

动态规划

```java
public static int dpWays(int x, int y, int step) {
    if (x < 0 || x > 8 || y < 0 || y > 9 || step < 0) {
        return 0;
    }
    int[][][] dp = new int[9][10][step + 1];
    dp[0][0][0] = 1;
    for (int h = 1; h <= step; h++) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 10; c++) {
                dp[r][c][h] += getValue(dp, r - 1, c + 2, h - 1);
                dp[r][c][h] += getValue(dp, r + 1, c + 2, h - 1);
                dp[r][c][h] += getValue(dp, r + 2, c + 1, h - 1);
                dp[r][c][h] += getValue(dp, r + 2, c - 1, h - 1);
                dp[r][c][h] += getValue(dp, r + 1, c - 2, h - 1);
                dp[r][c][h] += getValue(dp, r - 1, c - 2, h - 1);
                dp[r][c][h] += getValue(dp, r - 2, c - 1, h - 1);
                dp[r][c][h] += getValue(dp, r - 2, c + 1, h - 1);
            }
        }
    }
    return dp[x][y][step];
}
public static int getValue(int[][][] dp, int row, int col, int step) {
        if (row < 0 || row > 8 || col < 0 || col > 9) {
            return 0;
        }
        return dp[row][col][step];
}
```





## Bob的生存概率 

【题目】 

给定五个参数n,m,i,j,k。表示在一个N*M的区域，Bob处在(i,j)点，每次Bob等概率的向上、下、左、右四个方向移动一步，Bob必须走K步。如果走完之后，Bob还停留在这个区域上， 就算Bob存活，否则就算Bob死亡。请求解Bob的生存概率，返回字符串表示分数的方式。

递归

```java
public static long process(int N, int M, int row, int col, int rest) {
    if (row < 0 || row == N || col < 0 || col == M) {
        return 0;
    }
    if (rest == 0) {
        return 1;
    }
    long live = process(N, M, row - 1, col, rest - 1);
    live += process(N, M, row + 1, col, rest - 1);
    live += process(N, M, row, col + 1, rest - 1);
    live += process(N, M, row, col - 1, rest - 1);
    return live;
}

public static String bob(int N, int M, int i, int j, int K) {
    long all = (long)Math.pow(4, K);
    long live = process(N, M, i, j, K);
    long gcd = gcd(all, live);
    return (live / gcd) + "/" + (all / gcd);
}

public static long gcd(long m, long n) {
    return n == 0 ? m : gcd(n, m % n);
}
```

动态规划参考上一题

```java
public static String bob2(int N, int M, int i, int j, int K) {
		int[][][] dp = new int[N + 2][M + 2][K + 1];
		for (int row = 1; row <= N; row++) {
			for (int col = 1; col <= M; col++) {
				dp[row][col][0] = 1;
			}
		}
		for (int rest = 1; rest <= K; rest++) {
			for (int row = 1; row <= N; row++) {
				for (int col = 1; col <= M; col++) {
					dp[row][col][rest] = dp[row - 1][col][rest - 1];
					dp[row][col][rest] += dp[row + 1][col][rest - 1];
					dp[row][col][rest] += dp[row][col - 1][rest - 1];
					dp[row][col][rest] += dp[row][col + 1][rest - 1];
				}
			}
		}
		long all = (long) Math.pow(4, K);
		long live = dp[i + 1][j + 1][K];
		long gcd = gcd(all, live);
		return String.valueOf((live / gcd) + "/" + (all / gcd));
}
public static long gcd(long m, long n) {
    return n == 0 ? m : gcd(n, m % n);
}
```



## 换钱的总方法数

给定数组 arr，arr 中所有的值都为正数且不重复。每个值代表一种面值的货币，每种面值的货币可以使用任意张，再给定一个整数 aim，代表要找的钱数，求组成 aim 的总方法数。

递归

```java
public static int way1(int[] arr, int aim) {
    return process(arr, 0, aim);
}

private static int process(int[] arr, int index, int rest) {
    if (index == arr.length) {
        return rest == 0 ? 1 : 0;
    }
    int ways = 0;
    for (int zhang = 0; arr[index] * zhang <= rest; zhang++) {
        ways += process(arr, index + 1, rest - arr[index] * zhang);
    }
    return ways;
}
```

动态规划

```java
public static int dpWays(int[] arr, int aim) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int N = arr.length;
    int[][] dp = new int[N + 1][aim + 1];
    dp[N][0] = 1;
    for (int row = N - 1; row >= 0; row--) {
        for (int col = 0; col <= aim; col++) {
            dp[row][col] = dp[row + 1][col];
            if (col - arr[row] >= 0) {
                dp[row][col] +=  dp[row][col - arr[row]];
            }
        }
    }
    return dp[0][aim];
}
```





## 给定一个有序数组arr，代表数轴上从左到右有n个点arr[0]、arr[1]...arr[n－1]， 给定一个正数L，代表一根长度为L的绳子，求绳子最多能覆盖其中的几个点。

解法一：二分法 时间复杂度 O(N*logN)

```java
public static int maxPoint(int[] arr, int L) {
    int res = 1;
    for (int i = 0; i < arr.length; i++) {
        int nearest = nearestIndex(arr, i, arr[i] - L);
        res = Math.max(res, i - nearest + 1);
    }
    return res;
}


private static int nearestIndex(int[] arr, int R, int value) {
    int L = 0;
    int index = R;
    while (L < R) {
        int mid = L + ((R - L) >> 1);
        if (arr[mid] >= value) {
            index = mid;
            R = mid - 1;
        } else {
            L = mid + 1;
        }
    }
    return index;
}
```

解法二：滑动窗口 O(N)

```java
public static int maxPoint2(int[] arr, int L) {
   int res = 1;
   int begin = 0;
   int end = 1;
   while (end < arr.length) {
       if (arr[end] - arr[begin] > L) {
           res = Math.max(res, end - begin);
           begin++;
       } else {
           end++;
       }
   }
   return res;
}
```





## 小虎去附近的商店买苹果，奸诈的商贩使用了捆绑交易，只提供6个每袋和8个每袋的包装包装不可拆分。可是小虎现在只想购买恰好n个苹果，小虎想购买尽量少的袋数方便携带。如果不能购买恰好n个苹果，小虎将不会购买。输入一个整数n，表示小虎想购买的个苹果，返回最小使用多少袋子。如果无论如何都不能正好装下，返回-1。

```java
public static int minBags(int apple) {
    if (apple < 0) {
        return -1;
    }
    int bag6 = -1;
    int bag8 = apple / 8;
    int rest = apple - bag8 * 8;
    while (bag8 >= 0 && rest < 24) {
        int restUse6 = minBagBase6(rest);
        if (restUse6 != -1) {
            bag6 = restUse6;
            break;
        }
        rest = apple - 8 * (--bag8);
    }
    return bag6 == -1 ? -1 : bag6 + bag8;
}

private static int minBagBase6(int rest) {
    return rest % 6 == 0 ? (rest / 6) : -1;
}
```

apple = 【0， 1000】找规律，发现下面

```java
public static int minBagAwesome(int apple) {
    if ((apple & 1) != 0) {
        return -1;
    }
    if (apple < 18) {
        return apple == 0 ? 0 : (apple == 6 || apple == 8) ? 1
                : (apple == 12 || apple == 14 || apple == 16) ? 2 : -1;
    }
    return (apple - 18) / 8 + 3;
}
```



## 两只牛吃草，分先吃和后吃，一次可以吃4的n次方份(n=0 1 2 3 ...)  谁吃完草就赢了

打表法

```java
public static String winner1(int n) {
    if (n < 5) {
        return (n == 0 || n == 2) ? "后手" : "先手";
    }
    int base = 1;
    while (base <= n) {
        if (winner1(n - base).equals("后手")) {
            return "先手";
        }
        if (base > n / 4) {
            break;
        }
        base *= 4;
    }
    return "后手";
}

public static String winner2(int n) {
    if (n % 5 == 0 || n % 5 == 2) {
        return "后手";
    }
    return "先手";
}
```





## 牛牛有一些排成一行的正方形。每个正方形已经被染成红色或者绿色。牛牛现在可以选择任意一个正方形然后用这两种颜色的任意一种进行染色,这个正方形的颜色将会被覆盖。牛牛的目标是在完成染色之后,每个红色R都比每个绿色G距离最左侧近。 牛牛想知道他最少需要涂染几个正方形。 如样例所示: s = RGRGR  我们涂染之后变成RRRGG满足要求了,涂染的个数为2,没有比这个更好的涂染方案。

预处理

```java
public static int minPaint(String s) {
    if (s == null || s.length() < 2) {
        return 0;
    }
    char[] chs = s.toCharArray();
    //i~N有位置有R的个数
    int[] right = new int[chs.length];
    right[chs.length - 1] = chs[chs.length - 1] == 'R' ? 1 : 0;
    for (int i = chs.length - 2; i >= 0; i--) {
        right[i] = right[i + 1] + (chs[i] == 'R' ? 1 : 0);
    }
    int res = right[0];
    int left = 0;
    for (int i = 0; i < chs.length - 1; i++) {
        left += chs[i] == 'G' ? 1 : 0;
        res = Math.min(res, left + right[i + 1]);
    }
    res = Math.min(res, left + (chs[chs.length - 1] == 'G' ? 1 : 0));
    return res;
}

public static void main(String[] args) {
    String test = "GGGGGR";
    System.out.println(minPaint(test));

}
```





## 给定一个N*N的矩阵matrix，只有0和1两种值，返回边框全是1的最大正方形的边长长度。 

例如: 

```
01111 

01001 

01001 

01111 

01011 
```

其中边框全是1的最大正方形的大小为4*4，所以返回4。

```java
public static int getMaxSize(int[][] m) {
    int[][] right = new int[m.length][m[0].length];
    int[][] down = new int[m.length][m[0].length];
    setBorderMap(m, right, down);
    for (int size = Math.min(m.length, m[0].length); size != 0; size--) {
        if (hasSizeOfBorder(size, right, down)) {
            return size;
        }
    }
    return 0;
}

private static boolean hasSizeOfBorder(int size, int[][] right, int[][] down) {
    for (int i = 0; i < right.length - size + 1; i++) {
        for (int j = 0; j < right[0].length - size + 1; j++) {
            if (right[i][j] >= size && down[i][j] >= size
                    && right[i + size - 1][j] >= size
                    && down[i][j + size - 1] >= size) {
                return true;
            }
        }
    }
    return false;
}

private static void setBorderMap(int[][] m, int[][] right, int[][] down) {
    int r = m.length;
    int c = m[0].length;
    if (m[r - 1][c - 1] == 1) {
        right[r - 1][c - 1] = 1;
        down[r - 1][c - 1] = 1;
    }
    for (int i = r - 2; i >= 0; i--) {
        if (m[i][c - 1] == 1) {
            right[i][c - 1] = 1;
            down[i][c - 1] = down[i + 1][c - 1] + 1;
        }
    }
    for (int i = c - 2; i >= 0; i--) {
        if (m[r - 1][i] == 1) {
            right[r - 1][i] = right[r - 1][i + 1] + 1;
            down[r - 1][i] = 1;
        }
    }
    for (int i = r - 2; i != -1; i--) {
        for (int j = c - 2; j != -1; j--) {
            if (m[i][j] == 1) {
                right[i][j] = right[i][j + 1] + 1;
                down[i][j] = down[i + 1][j] + 1;
            }
        }
    }
}
```





## 给定一个非负整数n，代表二叉树的节点个数。返回能形成多少种不同的二叉树结构

递归

```java
public static int process(int n) {
    if (n < 0) {
        return 0;
    }
    if (n == 0) {
        return 1;
    }
    if (n == 1) {
        return 1;
    }
    if (n == 2) {
        return 2;
    }
    int res = 0;
    for (int leftNum = 0; leftNum <= n - 1; leftNum++) {
        int leftWays = process(leftNum);
        int rightWays = process(n - 1 - leftNum);
        res += leftWays * rightWays;
    }
    return res;
}
```

动态规划

```java
public static int numTrees(int n) {
    if (n < 0) {
        return 0;
    }
    if (n < 2) {
        return 1;
    }
    int[] dp = new int[n + 1];
    dp[0] = 1;
    for (int i = 1; i < n + 1; i++) {
        for (int j = 0; j <= i - 1; j++) {
            dp[i] += dp[j] * dp[i - j - 1];
        }
    }
    return dp[n];
}
```





## 一个完整的括号字符串定义规则如下: 

①空字符串是完整的。 

②如果s是完整的字符串，那么(s)也是完整的。 

③如果s和t是完整的字符串，将它们连接起来形成的st也是完整的。 

例如，"(()())", ""和"(())()"是完整的括号字符串，"())(", "()(" 和 ")" 是不完整的括号字符串。 

牛牛有一个括号字符串s,现在需要在其中任意位置尽量少地添加括号,将其转化为一个完整的括号字符串。请问牛牛至少需要添加多少个括号。



前置题目：如何判断完整性

```java
public static boolean isValid(String s) {
    char[] arr = s.toCharArray();
    int come = 0;
    for (char c : arr) {
        if (c == '(') {
            come++;
        } else {
            come--;
            if (come < 0) {
                return false;
            }
        }
    }
    return come == 0;
}
```



```java
public static int needParentheses(String str) {
    int count = 0;
    int ans = 0;
    for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) == '(') {
            count++;
        } else {
            if (count == 0) {
                ans++;
            } else {
                count--;
            }
        }
    }
    return count + ans;
}
```





## 给定一个数组arr，求差值为k的去重数字对。

```java
public static List<List<Integer>> addPair(int[] arr, int k) {
    HashSet<Integer> set = new HashSet<>();
    for(int num : arr) {
        set.add(num);
    }
    List<List<Integer>> ans = new ArrayList<>();
    for (int num : set) {
        if (set.contains(num + k)) {
            ans.add(Arrays.asList(num, num + k));
        }
    }
    return ans;
}
```



## 给一个包含n个整数元素的集合a，一个包含m个整数元素的集合b。 

定义magic操作为，从一个集合中取出一个元素，放到另一个集合里，且操作过后每个集合的平均值都大大于于操作前。 

注意以下两点： 

1）不可以把一个集合的元素取空，这样就没有平均值了 

2）值为x的元素从集合b取出放入集合a，但集合a中已经有值为x的元素，则a的平均值不变（因为集合元素不会重复），b的平均值可能会改变（因为x被取出了）

问最多可以进行多少次magic操作？

```java
// 请保证arr1无重复值、arr2中无重复值，且arr1和arr2肯定有数字
public static int maxOps(int[] arr1, int[] arr2) {
    double sum1 = 0;
    for (int i = 0; i < arr1.length; i++) {
        sum1 += (double) arr1[i];
    }
    double sum2 = 0;
    for (int i = 0; i < arr2.length; i++) {
        sum2 += (double) arr2[i];
    }
    if (avg(sum1, arr1.length) == avg(sum2, arr2.length)) {
        return 0;
    }
    int[] arrMore = null;
    int[] arrLess = null;
    double sumMore = 0;
    double sumLess = 0;
    if (avg(sum1, arr1.length) > avg(sum2, arr2.length)) {
        arrMore = arr1;
        sumMore = sum1;
        arrLess = arr2;
        sumLess = sum2;
    } else {
        arrMore = arr2;
        sumMore = sum2;
        arrLess = arr1;
        sumLess = sum1;
    }
    Arrays.sort(arrMore);
    HashSet<Integer> setLess = new HashSet<>();
    for (int num : arrLess) {
        setLess.add(num);
    }
    int moreSize = arrMore.length;
    int lessSize = arrLess.length;
    int ops = 0;
    for (int i = 0; i < arrMore.length; i++) {
        double cur = arrMore[i];
        //如果cur的值比较大集合的平均值小 比较小集合的平均值大，同时较小的集合不包含cur ，可以将cur从大集合放到小集合
        if (cur < avg(sumMore, moreSize) && cur > avg(sumLess, lessSize)
        && !setLess.contains(arrMore[i])) {
            sumMore -= cur;
            moreSize--;
            sumLess += cur;
            lessSize++;
            setLess.add(arrMore[i]);
            ops++;
        }
    }
    return ops;
}
public static double avg(double sum, int size) {
    return sum / (double) (size);
}
```



## 括号问题

一个合法的括号匹配序列有以下定义:

①空串""是一个合法的括号匹配序列

②如果"X"和"Y"都是合法的括号匹配序列,"XY"也是一个合法的括号匹配序列

③如果"X"是一个合法的括号匹配序列,那么"(X)"也是一个合法的括号匹配序列

④每个合法的括号序列都可以由以上规则生成。

例如:"","()","()()","((()))"都是合法的括号序列对于一个合法的括号序列我们又有以下定义它的深度:

①空串""的深度是0

②如果字符串"X"的深度是x,字符串"Y"的深度是y,那么字符串"XY"的深度为max(x,y)

3、如果"X"的深度是x,那么字符串"(X)"的深度是x+1例如:"()()()"的深度是1,"((()))"的深度是3。牛牛现在给你一个合法的括号序列,需要你计算出其深度。

```java
public static int deep(String s) {
    char[] str = s.toCharArray();
    int count = 0;
    int max = 0;
    for (char c : str) {
        if (c == '(') {
            max = Math.max(max, ++count);
        } else {
            count--;
        }
    }
    return max;
}
```

进阶：字符串只含有"(" ")" 合法匹配的最大长度

```java
public static int maxLength(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    char[] str = s.toCharArray();
    int[] dp = new int[str.length];
    int pre = 0;
    int res = 0;
    for (int i = 1; i < str.length; i++) {
        if (str[i] == ')') {
            pre = i - dp[i - 1] - 1;
            if (pre >= 0 && str[pre] == '(') {
                dp[i] = dp[i - 1] + 2 + (pre > 0 ? dp[pre - 1] : 0);
            }
        }
        res = Math.max(res, dp[i]);
    }
    return res;
}
```



## 请编写一个程序，对一个栈里的整型数据，按升序进行排序（即排序前，栈里的数据是无序的，排序后最大元素位于栈顶），要求最多只能使用一个额外的栈存放临时数据，但不得将元素复制到别的数据结构中。

```java
public static void sortStackByStack(Stack<Integer> stack) {
    Stack<Integer> help = new Stack<>();
    while (!stack.isEmpty()) {
        int cur = stack.pop();
        while (!help.isEmpty() && help.peek() < cur) {
            stack.push(help.pop());
        }
        help.push(cur);
    }
    while (!help.isEmpty()) {
        stack.push(help.pop());
    }
}
```



## 将给定的数转换为字符串，原则如下：1对应a，2对应b，…..26对应z，例如12258可以转换为"abbeh","aveh","abyh","lbeh"and"lyh"，个数为5，编写一个函数，给出可以转换的不同字符串的个数。

递归

```java
public static int process(char[] str, int index) {
    if (index == str.length) {
        return 1;
    }
    if (str[index] == '0') {
        return 0;
    }
    int res = process(str, index + 1);
    if (index == str.length - 1) {
        return res;
    }
    if ((str[index] - '0') * 10 + str[index] - '0' < 27) {
        res += process(str, index + 2);
    }
    return res;
}
```

动态规划

```java
public static int dpWays(int num) {
    if (num < 1) {
        return 0;
    }
    char[] str = String.valueOf(num).toCharArray();
    int N = str.length;
    int[] dp = new int[N + 1];
    dp[N] = 1;
    dp[N - 1] = str[N - 1] == '0' ? 0 : 1;
    for (int i = N - 2; i >= 0; i--) {
        if (str[i] == '0') {
            dp[i] = 0;
        } else {
            dp[i] = dp[i + 1] + (((str[i] - '0') * 10 + str[i] - '0') < 27 ? dp[i + 2] : 0);
        }
    }
    return dp[0];
}
```



## 二叉树每个结点都有一个int型权值，给定一棵二叉树，要求计算出从根结点到叶结点的所有路径中，权值和最大的值为多少。

解法一：

```java
public static int maxSum = Integer.MIN_VALUE;

public static int maxPath(Node head) {
    if (head == null) {
        return 0;
    }
    process(head, 0);
    return maxSum;
}

public static void process(Node x, int pre) {
    if (x.left == null && x.right == null) {
        maxSum = Math.max(maxSum, pre + x.value);
        return;
    }
    if (x.left != null) {
        process(x.left, pre + x.value);
    }
    if (x.right != null) {
        process(x.right, pre + x.value);
    }
}
```

解法二：

```java
public static int maxPath2(Node head) {
    if (head == null) {
        return 0;
    }
    return process2(head);
}

public static int process2(Node x) {
    if (x.left == null && x.right == null) {
        return x.value;
    }
    int next = Integer.MIN_VALUE;
    if (x.left != null) {
        next = process2(x.left);
    }
    if (x.right != null) {
        next = Math.max(next, process2(x.right));
    }
    return x.value + next;
}
```



## 给定一个元素为非负整数的二维数组matrix，每行和每列都是从小到大有序的。 再给定一个非负整数aim，请判断aim是否在matrix中。

```java
public static boolean isContains(int[][] matrix, int k) {
    int row = 0;
    int col = matrix[0].length - 1;
    while (row < matrix.length && col > -1) {
        if (matrix[row][col] == k) {
            return true;
        }
        if (matrix[row][col] > k) {
            col--;
        } else {
            row++;
        }
    }
    return false;
}
```



## 有n个打包机器从左到右一字排开，上方有一个自动装置会抓取一批放物品到每个打包机上，放到每个机器上的这些物品数量有多有少，由于物品数量不相同，需要工人将每个机器上的物品进行移动从而到达物品数量相等才能打包。每个物品重量太大、 每次只能搬一个物品进行移动，为了省力，只在相邻的机器上移动。请计算在搬动最小轮数的前提下，使每个机器上的物品数量相等。如果不能使每个机器上的物品相同， 返回-1。 

例如[1,0,5]表示有3个机器，每个机器上分别有1、0、5个物品，经过这些轮后： 

第一轮：1 0 <- 5 => 1 1 4 

第二轮：1 <-1<- 4 => 2 1 3 

第三轮： 2 1 <- 3 => 2 2 2 

移动了3轮，每个机器上的物品相等，所以返回3   例如[2,2,3]表示有3个机器，每个机器上分别有2、2、3个物品， 这些物品不管怎么移动，都不能使三个机器上物品数量相等，返回-1

```java
public static int minOps(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int size = arr.length;
    int sum = 0;
    for (int i = 0; i < size; i++) {
        sum += arr[i];
    }
    if (sum % size != 0) {
        return -1;
    }
    int avg = sum / size;
    int leftSum = 0;
    int ans = 0;
    for (int i = 0; i < arr.length; i++) {
        int leftRest = leftSum - i * avg;
        int rightRest = (sum - leftSum - arr[i]) - (size - i - 1) * avg;
        if (leftRest < 0 && rightRest < 0) {
            ans = Math.max(ans, Math.abs(leftRest) + Math.abs(rightRest));
        } else {
            ans = Math.max(ans, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
        }
        leftSum += arr[i];
    }
    return ans;
}
```





## 用螺旋的方式打印矩阵，比如如下的矩阵 

```
0 1 2 3 

4 5 6 7 

8 9 10 11 
```

打印顺序为：0 1 2 3 7 11 10 9 8 4 5 6

```java
public static void spiralOrderPrint(int[][] matrix) {
    int x1 = 0, y1 = 0;
    int x2 = matrix.length - 1, y2 = matrix[0].length - 1;
    while (x1 <= x2 && y1 <= y2) {
        printEdge(matrix, x1++, y1++, x2--, y2--);
    }
}

public static void printEdge(int[][] m, int x1, int y1, int x2, int y2) {
    if (x1 == x2) {
        for (int i = y1; i <= y2; i++) {
            System.out.print(m[x1][i] + " ");
        }
    } else if (y1 == y2) {
        for (int i = x1; i <= x2; i++) {
            System.out.print(m[i][y1] + " ");
        }
    } else {
        int x = x1;
        int y = y1;
        while (y != y2) {
            System.out.print(m[x][y++] + " ");
        }
        while (x != x2) {
            System.out.print(m[x++][y] + " ");
        }
        while (y != y1) {
            System.out.print(m[x][y--] + " ");
        }
        while (x != x1) {
            System.out.print(m[x--][y] + " ");
        }
    }
}
```





## 给定一个正方形矩阵，只用有限几个变量，实现矩阵中每个位置的数顺时针转动90度，比如如下的矩阵 

```
0 1 2 3 

4 5 6 7 

8 9 10 11 

12 13 14 15 
```

矩阵应该被调整为： 

```
12 8 4 0 

13 9 5 1 

14 10 6 2 

15 11 7 3
```

```java
public static void rotateEdge(int[][] m, int x1, int y1, int x2, int y2) {
    int temp = 0;
    for (int i = 0; i < x2 - x1; i++) {
        temp = m[x1][y1 + i];
        m[x1][y1 + i] = m[x2 - i][y1];
        m[x2 - i][y1] = m[x2][y2 - i];
        m[x2][y2 - i] = m[x1 + i][y2];
        m[x1 + i][y2] = temp;
    }
}

public static void  rotate(int[][] matrix) {
    int x1 = 0, y1 = 0;
    int x2 = matrix.length - 1, y2 = matrix[0].length - 1;
    while (x1 <= x2 && y1 <= y2) {
        rotateEdge(matrix, x1++, y1++, x2--, y2--);
    }
}
```



## 用zigzag的方式打印矩阵，比如如下的矩阵 

0 1 2 3 

4 5 6 7 

8 9 10 11 

打印顺序为：0 1 4 8 5 2 3 6 9 10 7 11

```java
public static void printMatrixZigZag(int[][] matrix) {
    int x1 = 0, y1 = 0;
    int x2 = 0, y2 = 0;
    int ex = matrix.length - 1, ey = matrix[0].length - 1;
    boolean fromUp = false;
    while (x1 != ex + 1) {
        printLevel(matrix, x1, y1, x2, y2, fromUp);
        x1 = y1 == ey ? x1 + 1 : x1;
        y1 = y1 == ey ? y1 : y1 + 1;
        y2 = x2 == ex ? y2 + 1 : y2;
        x2 = x2 == ex ? x2 : x2 + 1;
        fromUp = !fromUp;
    }
}
public static void printLevel(int[][] m, int x1, int y1, int x2, int y2, boolean fromUp) {
    if (fromUp) {
        //右上往左下打印
        while (x1 != x2 + 1) {
            System.out.print(m[x1++][y1--] + " ");
        }
    } else {
         //左下往右上打印
        while (x2 != x1 - 1) {
            System.out.print(m[x2--][y2++] + " ");
        }
    }
}
```





## 假设s和m初始化，s = "a"; m = s; 再定义两种操作，

第一种操作： 

m = s; 

s = s + s; 

第二种操作： 

s = s + m; 

求最小的操作步骤数，可以将s拼接到长度等于n

```java
// 附加题：怎么判断一个数是不是质数？
	public static boolean isPrim(int n) {
		if (n < 2) {
			return false;
		}
		int max = (int) Math.sqrt((double) n);
		for (int i = 2; i <= max; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
// 请保证n不是质数
// 返回:
// 0) 所有因子的和，但是因子不包括1
// 1) 所有因子的个数，但是因子不包括1
public static int[] divsSumAndCount(int n) {
	int sum = 0;
	int count = 0;
	for (int i = 2; i <= n; i++) {
		while (n % i == 0) {
			sum += i;
			count++;
			n /= i;
		}
	}
	return new int[] { sum, count };
}

public static int minOps(int n) {
	if (n < 2) {
		return 0;
	}
	if (isPrim(n)) {
		return n - 1;
	}
	int[] divSumAndCount = divsSumAndCount(n);
	return divSumAndCount[0] - divSumAndCount[1];
}
```



## 给定一个字符串类型的数组arr，求其中出现次数最多的前K个

```java
public static class Node {
    public String str;
    public int times;

    public Node(String str, int times) {
        this.str = str;
        this.times = times;
    }
}


public static void printTopKAndRank(String[] arr, int topK) {
    if (arr == null || arr.length == 0 || topK < 1) {
        return;
    }
    HashMap<String, Integer> map = new HashMap<>();
    for (String str : arr) {
        map.put(str, map.getOrDefault(str, 0) + 1);
    }
    topK = Math.min(arr.length, topK);
    PriorityQueue<Node> heap = new PriorityQueue<>((o1, o2) -> o2.times - o1.times);
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
        Node cur = new Node(entry.getKey(), entry.getValue());
        if (heap.size() < topK) {
            heap.add(cur);
        } else {
            if (heap.peek().times < cur.times) {
                heap.poll();
            }
        }
    }
    while (!heap.isEmpty()) {
        System.out.println(heap.poll().str);
    }
}
```



## 实现一个特殊的栈，在实现栈的基本功能的基础上，再实现返回栈中最小元素的操作。

要求：1.pop、push、getMin操作的时间复杂度都是O(1)；

​			2.设计的栈类型可以使用现成的栈结构

```java
public static class MyStack1 {
    private Stack<Integer> stackData;
    private Stack<Integer> stackMin;

    public MyStack1() {
        this.stackData = new Stack<>();
        this.stackMin = new Stack<>();
    }

    public void push(int newNum) {
        if (this.stackMin.isEmpty() || newNum <= this.getMin()) {
            this.stackMin.push(newNum);
        }
        this.stackData.push(newNum);
    }

    public int pop() {
        if (this.stackData.isEmpty()) {
            throw new RuntimeException("Your stack is empty.");
        }
        int value = this.stackData.pop();
        if (value == this.getMin()) {
            this.stackMin.pop();
        }
        return value;
    }

    public int getMin() {
        if (this.stackMin.isEmpty()) {
            throw new RuntimeException("Your stack is empty.");
        }
        return this.stackMin.peek();
    }
}
```



## 如何仅用队列结构实现栈结构? 

```java 
public static class TwoQueuesStack {
    
    private Queue<Integer> queue;
    
    private Queue<Integer> help;

    public TwoQueuesStack() {
        this.queue = new LinkedList<>();
        this.help = new LinkedList<>();
    }

    public void push(int val) {
        queue.add(val);
    }

    public int pop() {
        if (queue.isEmpty()) {
            throw new RuntimeException("Stack is empty.");
        }
        while (queue.size() > 1) {
            help.add(queue.poll());
        }
        int res = queue.poll();
        swap();
        return res;
    }

    public int peek() {
        if (queue.isEmpty()) {
            throw new RuntimeException("Stack is empty.");
        }
        while (queue.size() > 1) {
            help.add(queue.poll());
        }
        int res = queue.poll();
        help.add(res);
        swap();
        return res;
    }

    public void swap() {
        Queue<Integer> tmp = help;
        help = queue;
        queue = tmp;
    }
}
```

## 如何仅用栈结构实现队列结构?

```java 
public static class TwoStacksQueue {
    
    private Stack<Integer> stackPush;
    private Stack<Integer> stackPop;
    public TwoStacksQueue() {
        stackPush = new Stack<>();
        stackPop = new Stack<>();
    }

    public void push(int val) {
        stackPush.push(val);
    }

    public int poll() {
        if (stackPop.isEmpty() && stackPush.isEmpty()) {
            throw new RuntimeException("Queue is empty.");
        }
        if (stackPop.empty()) {
            while (!stackPush.isEmpty()) {
                stackPop.push(stackPush.pop());
            }
        }
        return stackPop.pop();
    }
    
    public int peek() {
        if (stackPop.isEmpty() && stackPush.isEmpty()) {
            throw new RuntimeException("Queue is empty.");
        }
        if (stackPop.empty()) {
            while (!stackPush.isEmpty()) {
                stackPop.push(stackPush.pop());
            }
        }
        return stackPop.peek();
    }
}
```





动态规划的空间压缩技巧

## 给你一个二维数组matrix，其中每个数都是正数，要求从左上角走到右下角。每一步只能向右或者向下，沿途经过的数字要累加起来。最后请返回最小的路径和。

二维数组dp

```java
public static int minPathSum1(int[][] m) {
    if (m == null ||m.length == 0 || m[0] == null || m[0].length == 0) {
        return 0;
    }
    int row = m.length;
    int col = m[0].length;
    int[][] dp = new int[row][col];
    dp[0][0] = m[0][0];
    for (int i = 1; i < row; i++) {
        dp[i][0] = dp[i - 1][0] + m[i][0];
    }
    for (int j = 1; j < col; j++) {
        dp[0][j] = dp[0][j - 1] + m[0][j];
    }
    for (int i = 1; i < row; i++) {
        for (int j = 1; j < col; j++) {
            dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
        }
    }
    return dp[row - 1][col - 1];
}
```

一维数组dp

```java
public static int minPathSum2(int[][] m) {
    if (m == null ||m.length == 0 || m[0] == null || m[0].length == 0) {
        return 0;
    }
    int more = Math.max(m.length, m[0].length);
    int less = Math.min(m.length, m[0].length);
    boolean rowmore = more == m.length;
    int[] arr = new int[less];
    arr[0] = m[0][0];
    for (int i = 1; i < less; i++) {
        arr[i] = arr[i - 1] + (rowmore ? m[0][i] : m[i][0]);
    }
    for (int i = 1; i < more; i++) {
        arr[0] = arr[0] + (rowmore ? m[i][0] : m[0][i]);
        for (int j = 1; j < less; j++) {
            arr[j] = Math.min(arr[j - 1], arr[j]) + (rowmore ? m[i][j] : m[j][i]);
        }
    }
    return arr[less - 1];
}
```

测试

```java
 // for test
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i != matrix.length; i++) {
            for (int j = 0; j != matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static int[][] generateRandomMatrix(int rowSize, int colSize) {
        if (rowSize < 0 || colSize < 0) {
            return null;
        }
        int[][] result = new int[rowSize][colSize];
        for (int i = 0; i != result.length; i++) {
            for (int j = 0; j != result[0].length; j++) {
                result[i][j] = (int) (Math.random() * 10);
            }
        }
        return result;
    }
    public static void main(String[] args) {
        int[][] m = generateRandomMatrix(3, 4);
//        int[][] m = { { 1, 3, 5, 9 }, { 8, 1, 3, 4 }, { 5, 0, 6, 1 },
//                { 8, 8, 4, 0 } };
        printMatrix(m);
        System.out.println(minPathSum1(m));
        System.out.println(minPathSum2(m));
    }
```



## 给定一个数组arr，已知其中所有的值都是非负的，将这个数组看作一个容器，请返回容器能装多少水比如，arr={3，1，2，5，2，4}，根据值画出的直方图就是容器形状，该容器可以装下5格水再比如，arr={4，5，1，3，2}，该容器可以装下2格水

第i位置 的水  =  Math.max(0, Math.min(【0~i-1的最大值】, 【i + 1, N - 1】的最大值)   -   i位置的值)

```java 
public static int getWater2(int[] arr) {
    if (arr == null || arr.length < 3) {
        return 0;
    }
    int value = 0;
    for (int i = 1; i < arr.length - 1; i++) {
        int leftMax = 0;
        int rightMax = 0;
        for (int l = 0; l < i; l++) {
            leftMax = Math.max(leftMax, arr[l]);
        }
        for (int r = i + 1; r < arr.length; r++) {
            rightMax = Math.max(rightMax, arr[r]);
        }
        value += Math.max(0, Math.min(leftMax, rightMax) - arr[i]);
    }
    return value;
}
```

使用两个辅助数组

```java
public static int getWater1(int[] arr) {
    if (arr == null || arr.length < 3) {
        return 0;
    }
    int n = arr.length - 2;
    int[] leftMaxs = new int[n];
    leftMaxs[0] = arr[0];
    for (int i = 1; i < n; i++) {
        leftMaxs[i] = Math.max(leftMaxs[i - 1], arr[i]);
    }
    int[] rightMaxs = new int[n];
    rightMaxs[n - 1] = arr[n + 1];
    for (int j = n - 2; j >= 0; j--) {
        rightMaxs[j] = Math.max(rightMaxs[j + 1], arr[j + 2]);
    }
    int value = 0;
    for (int i = 1; i <= n; i++) {
        value += Math.max(0, Math.min(leftMaxs[i - 1], rightMaxs[i - 1]) - arr[i]);
    }
    return value;
}
```

使用一个辅助数组

```java
public static int getWater3(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length - 2;
        int[] rightMaxs = new int[n];
        rightMaxs[n - 1] = arr[n + 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i + 2]);
        }
        int leftMax = arr[0];
        int value = 0;
        for (int i = 1; i <= n; i++) {
            value += Math.max(0, Math.min(leftMax, rightMaxs[i - 1]) - arr[i]);
            leftMax = Math.max(leftMax, arr[i]);
        }
        return value;
}
```

不使用辅助数组

```java
public static int getWater4(int[] arr) {
    if (arr == null || arr.length < 3) {
        return 0;
    }
    int value = 0;
    int leftMax = arr[0];
    int rightMax = arr[arr.length  - 1];
    int left = 1;
    int right = arr.length - 2;
    while (left <= right) {
        if (leftMax <= rightMax) {
            value += Math.max(0, leftMax - arr[left]);
            leftMax = Math.max(leftMax, arr[left++]);
        } else {
            value += Math.max(0, rightMax - arr[right]);
            rightMax = Math.max(rightMax, arr[right--]);
        }
    }
    return value;
}
```



## 给定一个数组arr长度为N，你可以把任意长度大于0且小于N的前缀作为左部分，剩下的作为右部分。但是每种划分下都有左部分的最大值和右部分的最大值，请返回最大的，左部分最大值减去右部分最大值的绝对值。

暴力

```java
public static int maxABS1(int[] arr) {
    int res = Integer.MIN_VALUE;
    int maxLeft = 0;
    int maxRight = 0;
    for (int i = 0; i < arr.length - 1; i++) {
        maxLeft = Integer.MIN_VALUE;
        for (int j = 0; j <= i; j++) {
            maxLeft = Math.max(maxLeft, arr[j]);
        }
        maxRight = Integer.MIN_VALUE;
        for (int j = i + 1; j < arr.length; j++) {
            maxRight = Math.max(maxRight, arr[j]);
        }
        res = Math.max(res, Math.abs(maxLeft - maxRight));
    }
    return res;
}
```

使用辅助数组

```java
public static int maxABS2(int[] arr) {
    int N = arr.length;
    int[] leftMaxs = new int[N];
    int[] rightMaxs = new int[N];
    leftMaxs[0] = arr[0];
    rightMaxs[N - 1] = arr[N - 1];
    for (int i = 1; i < N; i++) {
        leftMaxs[i] = Math.max(leftMaxs[i - 1], arr[i]);
    }
    for (int i = N - 2; i >= 0; i--) {
        rightMaxs[i] = Math.max(rightMaxs[i + 1], arr[i]);
    }
    int res = 0;
    for (int i = 0; i < N - 1; i++) {
        res = Math.max(res, Math.abs(leftMaxs[i] - rightMaxs[i + 1]));
    }
    return res;
}
```

优化

```java
public static int maxABS3(int[] arr) {
    int max = Integer.MIN_VALUE;
    for (int i = 0; i < arr.length; i++) {
        max = Math.max(max, arr[i]);
    }
    return max - Math.min(arr[0], arr[arr.length - 1]);
}
```



## 如果一个字符串为str，把字符串str前面任意的部分挪到后面形成的字符串叫作str的旋转词。比如str="12345"，str的旋转词有"12345"、"23451"、"34512"、"45123"和"51234"。给定两个字符串a和b，请判断a和b是否互为旋转词。比如：a="cdab"，b="abcd"，返回true。a="1ab2"，b="ab12"，返回false。a="2ab1"，b="ab12"，返回true。

```java
public static boolean isRotation(String a, String b) {
    if (a == null || b == null || a.length() != b.length()) {
        return false;
    }
    String a2 = a + a;
    return getIndexOf(a2, b) != -1;
}
//KMP
public static int getIndexOf(String s, String m) {
    if (s.length() < m.length()) {
        return -1;
    }
    int[] next = getNextArray(m);
    int si = 0, mi = 0;
    while (si < s.length() && mi < m.length()) {
        if (s.charAt(si) == m.charAt(mi)) {
            si++;
            mi++;
        } else if (next[mi] == -1) {
            si++;
        } else {
            mi = next[mi];
        }
    }
    return mi == m.length() ? si - mi : -1;
}
public static int[] getNextArray(String m) {
    char[] ms = m.toCharArray();
    int[] next = new int[ms.length];
    next[0] = -1;
    next[1] = 0;
    int cn = 0;
    int pos = 2;
    while (pos < ms.length) {
        if (ms[pos - 1] == ms[cn]) {
            next[pos++] = ++cn;
        } else if (cn > 0) {
            cn = next[cn];
        } else {
            next[pos++] = 0;
        }
    }
    return next;
}
```





## 给定一个数组arr，如果通过调整可以做到arr中任意两个相邻的数字相乘是4的倍数，返回true；如果不能返回false

```java
public static boolean nearMultiple4Times(int[] arr) {
    	if (arr == null || arr.length == 0) {
            return false;
        }
        //是4的倍数的数有多少个
        int fourTimes = 0;
        // 是偶数但不是4的倍数的数有多少个
        int twoTimes = 0;
        // 奇数有多少个
        int odd = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & 1) != 0) {
                odd++;
                continue;
            }
            if (arr[i] % 4 == 0) {
                fourTimes++;
                continue;
            }
            twoTimes++;
        }
        //只有奇数和4的倍数
        if (twoTimes == 0) {
            return fourTimes + 1 >= odd;
        }
        //没有奇数 没有4的倍数的时候 2的倍数只有大于1个
        if (odd == 0 && fourTimes == 0) {
            return twoTimes > 1;
        }
        return fourTimes >= odd;
    }
```



## logN级别求斐波那契数列

```java
public static int fibonacci(int n) {
    if (n < 1) {
        return 0;
    }
    if (n == 1 || n == 2) {
        return 1;
    }
    int[][] base = {{1, 1},{1, 0}};
    int[][] res = matrixPower(base, n - 2);
    return res[0][0] + res[1][0];
}
private static int[][] matrixPower(int[][] base, int n) {
    int[][] res = new int[base.length][base[0].length];
    for (int i = 0; i < res.length; i++) {
        res[i][i] = 1;
    }
    int[][] tmp = base;
    while (n != 0) {
        if ((n & 1) != 0) {
            res = multiMatrix(res, tmp);
        }
        tmp = multiMatrix(tmp, tmp);
        n >>= 1;
    }
    return res;
}
//矩阵乘法
private static int[][] multiMatrix(int[][] m1, int[][] m2) {
    int[][] res = new int[m1.length][m2[0].length];
    for (int i = 0; i < m1.length; i++) {
        for (int j = 0; j < m2[0].length; j++) {
            for (int k = 0; k < m2.length; k++) {
                res[i][j] += m1[i][k] * m2[k][j];
            }
        }
    }
    return res;
}
```



## 达标字符串

字符串只由'0'和'1'两种字符构成， 

当字符串长度为1时，所有可能的字符串为"0"、"1"； 

当字符串长度为2时，所有可能的字符串为"00"、"01"、"10"、"11"； 

当字符串长度为3时，所有可能的字符串为"000"、"001"、"010"、"011"、"100"、 

"101"、"110"、"111" 

... 

如果某一个字符串中，只要是出现'0'的位置，左边就靠着'1'，这样的字符串叫作达标字符串。 

给定一个正数N，返回所有长度为N的字符串中，达标字符串的数量。 

比如，N=3，返回3，因为只有"101"、"110"、"111"达标。

思路： **斐波那契解法**

```java
public static int getNum1(int n) {
		if (n < 1) {
			return 0;
		}
		return process(1, n);
	}
public static int process(int i, int n) {
	if (i == n - 1) {
		return 2;
	}
	if (i == n) {
		return 1;
	}
	return process(i + 1, n) + process(i + 2, n);
}

public static int getNum2(int n) {
	if (n < 1) {
		return 0;
	}
	if (n == 1) {
		return 1;
	}
	int pre = 1;
	int cur = 1;
	int tmp = 0;
	for (int i = 2; i < n + 1; i++) {
		tmp = cur;
		cur += pre;
		pre = tmp;
	}
	return cur;
}

public static int getNum3(int n) {
	if (n < 1) {
		return 0;
	}
	if (n == 1 || n == 2) {
		return n;
	}
	int[][] base = { { 1, 1 }, { 1, 0 } };
	int[][] res = matrixPower(base, n - 2);
	return 2 * res[0][0] + res[1][0];
}

public static int[][] matrixPower(int[][] m, int p) {
	int[][] res = new int[m.length][m[0].length];
	for (int i = 0; i < res.length; i++) {
		res[i][i] = 1;
	}
	int[][] tmp = m;
	for (; p != 0; p >>= 1) {
		if ((p & 1) != 0) {
			res = muliMatrix(res, tmp);
		}
		tmp = muliMatrix(tmp, tmp);
	}
	return res;
}

public static int[][] muliMatrix(int[][] m1, int[][] m2) {
	int[][] res = new int[m1.length][m2[0].length];
	for (int i = 0; i < m1.length; i++) {
		for (int j = 0; j < m2[0].length; j++) {
			for (int k = 0; k < m2.length; k++) {
				res[i][j] += m1[i][k] * m2[k][j];
			}
		}
	}
	return res;
}

public static void main(String[] args) {
	for (int i = 0; i != 20; i++) {
		System.out.println(getNum1(i));
		System.out.println(getNum2(i));
		System.out.println(getNum3(i));
		System.out.println("===================");
	}

}
```





## 目录打印

给你一个字符串类型的数组arr，譬如： 

String[] arr = { "b\\cst", "d\\", "a\\d\\e", "a\\b\\c" }; 

你把这些路径中蕴含的目录结构给画出来，子目录直接列在父目录下面，并比父目录 

向右进两格，就像这样: 

![image-20220326013638750](C:\Users\lzj\AppData\Roaming\Typora\typora-user-images\image-20220326013638750.png)

同一级的需要按字母顺序排列，不能乱。

```java 
public class Code04 {


    public static void main(String[] args) {
        String[] strs = {"a\\b\\c", "a\\d\\e"};
        Node head = generateFolderTree(strs);
        print(head, "-");
    }

    public static class Node {
        public String name;
        public TreeMap<String, Node> nextMap;
        public Node(String name) {
            this.name = name;
            this.nextMap = new TreeMap<String, Node>();
        }
    }



    public static Node generateFolderTree(String[] folderPaths) {
        Node head = new Node("");
        for (String folderPath : folderPaths) {
            String[] paths = folderPath.split("\\\\");
            Node cur = head;
            for (int i = 0; i < paths.length; i++) {
                if (!cur.nextMap.containsKey(paths[i])) {
                    cur.nextMap.put(paths[i], new Node(paths[i]));
                }
                cur = cur.nextMap.get(paths[i]);
            }
        }
        return head;
    }
    public static void print(Node head, String space) {
        if (head == null) {
            return;
        }
        System.out.println(space + head.name);
        for (Map.Entry<String, Node> entry : head.nextMap.entrySet()) {
            print(entry.getValue(), space + "-");
        }
    }
}
```





## 双向链表节点结构和二叉树节点结构是一样的，如果你把last认为是left， next认为是next的话。 给定一个搜索二叉树的头节点head，请转化成一条有序的双向链表，并返回链表的头节点。

```java 
public static class Node {
    public int val;
    public Node left;
    public Node right;
}

public static class Info {
    public Node start;
    public Node end;

    public Info(Node start, Node end) {
        this.start = start;
        this.end = end;
    }
}


public static Info process(Node x) {
    if (x == null) {
        return new Info(null, null);
    }
    Info leftHeadEnd = process(x.left);
    Info rightHeadEnd = process(x.right);
    if (leftHeadEnd.end != null) {
        leftHeadEnd.end.right = x;
    }
    x.left = leftHeadEnd.end;
    x.right = rightHeadEnd.start;
    if (rightHeadEnd.start != null) {
        rightHeadEnd.start.left = x;
    }
    return new Info(leftHeadEnd.start != null ? leftHeadEnd.start : x,
            rightHeadEnd.end != null ? rightHeadEnd.end : x);
}
```



## 找到一棵二叉树中，最大的搜索二叉子树，返回最大搜索二叉子树的节点个数。

```java 
public static class Node {
    public int val;
    public Node left;
    public Node right;
}

public static class Info {
    public Node maxBSTHead;
    public boolean isBST;
    public int min;
    public int max;
    public int maxBSTSize;

    public Info(Node maxBSTHead, boolean isBST, int min, int max, int maxBSTSize) {
        this.maxBSTHead = maxBSTHead;
        this.isBST = isBST;
        this.min = min;
        this.max = max;
        this.maxBSTSize = maxBSTSize;
    }
}

public static Info process(Node x) {
    if (x == null) {
        return null;
    }
    Info leftInfo = process(x.left);
    Info rightInfo = process(x.right);
    int min = x.val;
    int max = x.val;
    if (leftInfo != null) {
        min = Math.min(min, leftInfo.min);
        max = Math.max(max, leftInfo.max);
    }
    if (rightInfo != null) {
        min = Math.min(min, rightInfo.min);
        max = Math.max(max, rightInfo.max);
    }
    int maxBSTSize = 0;
    Node maxBSTHead = null;
    boolean isBST = false;
    if (leftInfo != null) {
        maxBSTSize = leftInfo.maxBSTSize;
        maxBSTHead = leftInfo.maxBSTHead;
    }
    if (rightInfo != null && rightInfo.maxBSTSize > maxBSTSize) {
        maxBSTSize = rightInfo.maxBSTSize;
        maxBSTHead = rightInfo.maxBSTHead;
    }
    if ((leftInfo == null || leftInfo.isBST) && (rightInfo == null || rightInfo.isBST)) {
        if ((leftInfo == null || leftInfo.max < x.val) && (rightInfo == null || rightInfo.min > x.val)) {
            isBST = true;
            maxBSTHead = x;
            int leftSize = leftInfo == null ? 0 : leftInfo.maxBSTSize;
            int rightSize = rightInfo == null ? 0 : rightInfo.maxBSTSize;
            maxBSTSize = leftSize + 1 + rightSize;
        }
    }
    return new Info(maxBSTHead, isBST, min, max, maxBSTSize);
}
```



## 子数组的最大累加和

为了保证招聘信息的质量问题，公司为每个职位设计了打分系统，打分可以为正数，也可以为负数，正数表示用户认可帖子质量，负数表示用户不认可帖子质量．打分的分数根据评价用户的等级大小不定，比如可以为 -1分，10分，30分，-10分等。假设数组A记录了一条帖子所有打分记录，现在需要找出帖子曾经得到过最高的分数是多少，用于后续根据最高分数来确认需要对发帖用户做相应的惩罚或奖励．其中，最高分的定义为： 用户所有打分记录中，连续打分数据之和的最大值即认为是帖子曾经获得的最高分。例如：帖子10001010近期的打分记录为[1,1,-1,-10,11,4,-6,9,20,-10,-2],那么该条帖子曾经到达过的最高分数为 11+4+(-6)+9+20=38。请实现一段代码，输入为帖子近期的打分记录，输出为当前帖子得到的最高分数。

```java
public static int maxSum(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int max = Integer.MIN_VALUE;
    int sum = 0;
    for (int num : arr) {
        sum += num;
        max = Math.max(max, sum);
        sum = Math.max(sum, 0);
    }
    return max;
}
public static int maxSum1(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int max = arr[0];
    for (int i = 1; i < arr.length; i++) {
        arr[i] = arr[i - 1] + arr[i];
        max = Math.max(max, arr[i]);
        arr[i] = Math.max(0, arr[i]);
    }
    return max;
}
```





## 给定一个整型矩阵，返回子矩阵的最大累计和。

压缩数组 + 子数组的最大累加和

```java 
public static int maxSum(int[][] m) {
    if (m == null || m.length == 0 || m[0].length == 0) {
        return 0;
    }
    int max = Integer.MIN_VALUE;
    int cur = 0;
    int[] s = null;
    for (int i = 0; i < m.length; i++) {
        s = new int[m[0].length];
        for (int j = i; j < m.length; j++) {
            cur = 0;
            for (int k = 0; k < s.length; k++) {
                s[k] += m[j][k];
                cur += s[k];
                max = Math.max(max, cur);
                cur = Math.max(cur, 0);
            }
        }
    }
    return max;
}
```





## 小Q正在给一条长度为n的道路设计路灯安置方案。 

为了让问题更简单,小Q把道路视为n个方格,需要照亮的地方用'.'表示, 不需要照亮的障碍物格子用'X'表示。小Q现在要在道路上设置一些路灯, 对于安置在pos位置的路灯, 这盏路灯可以照亮pos - 1, pos, pos + 1这三个位置。 

小Q希望能安置尽量少的路灯照亮所有'.'区域, 希望你能帮他计算一下最少需要多少盏路灯。 

输入描述： 

输入的第一行包含一个正整数t(1 <= t <= 1000), 表示测试用例数接下来每两行一个测试数据, 第一行一个正整数n(1 <= n <= 1000),表示道路的长度。第二行一个字符串s表示道路的构造,只包含'.'和'X'。 

输出描述： 对于每个测试用例, 输出一个正整数表示最少需要多少盏路灯

贪心：

```java
public static int minLight(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    char[] str = s.toCharArray();
    int ans = 0;
    int index = 0;
    while (index < str.length) {
        if (str[index] == 'X') {
            index++;
        } else {
            ans++;
            if (index + 1 == str.length) {
                break;
            }
            if (str[index + 1] == 'X') {
                index = index + 2;
            } else {
                index = index + 3;
            }
        }
    }
    return ans;
}
```





## 已知一棵二叉树中没有重复节点，并且给定了这棵树的中序遍历数组和先序遍历数组，返回后序遍历数组。 

比如给定： 

int[] pre = { 1, 2, 4, 5, 3, 6, 7 }; 

int[] in = { 4, 2, 5, 1, 6, 3, 7 }; 

返回： {4,5,2,6,7,3,1}

```java
public static int[] getPostArray(int[] pre, int[] in) {
    if (pre == null) {
        return null;
    }
    int N = pre.length;
    int[] pos = new int[N];
    set(pre, in, pos, 0, N - 1, 0, N - 1, 0, N - 1);
    return pos;
}

public static void set(int[] pre, int[] in, int[] pos,
                       int prei, int prej, int ini, int inj, int posi, int posj) {
    if (prei > prej) {
        return;
    }
    if (prei == prej) {
        pos[posi] = prei;
        return;
    }
    pos[posj] = prei;
    int find = ini;
    for (; find <= inj; find++) {
        if (in[find] == pre[prei]) {
            break;
        }
    }
    set(pre, in, pos, prei + 1, prei + find - ini, ini, find - 1, posi, posi + find - ini - 1);
    set(pre, in, pos, prei + find - ini + 1, prej, find + 1, inj, posi + find - ini, posj - 1);
}
```



## 求完全二叉树节点的个数

log(h) * log(h)

```java
static class Node {
    Node left;
    Node right;
    int val;

    public Node(int val) {
        this.val = val;
    }
}


public static int nodeNum(Node head) {
    if (head == null) {
        return 0;
    }
    return bs(head,1,  mostLeftLevel(head, 1));
}
public static int bs(Node node, int level, int h) {
    if (level == h) {
        return 1;
    }
    if (mostLeftLevel(node.right, level + 1) == h) {
        return (1 << (h - level)) + bs(node.right, level + 1, h);
    } else {
        return (1 << (h - level - 1)) + bs(node.left, level + 1, h);
    }
}
public static int mostLeftLevel(Node node, int level) {
    while (node != null) {
        level++;
        node = node.left;
    }
    return level - 1;
}
```





## 给定一个整数数组A，长度为n，有 1 <= A[i] <= n，且对于[1,n]的整数，其中部分整数会重复出现而部分不会出现。 

实现算法找到[1,n]中所有未出现在A中的整数。 

提示：尝试实现O(n)的时间复杂度和O(1)的空间复杂度（返回值不计入空间复杂度）。 

> 输入描述： 
>
> 一行数字，全部为整数，空格分隔 
>
> A0 A1 A2 A3... 
>
> 输出描述： 
>
> 一行数字，全部为整数，空格分隔R0 R1 R2 R3... 
>
> 示例1: 
>
> 输入
>
> 1 3 4 3 
>
> 输出
>
> 2

```java
public static void printNumberNoInArray(int[] arr) {
    if (arr == null || arr.length == 0) {
        return;
    }
    for (int value : arr) {
        modify(arr, value);
    }
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] != i + 1) {
            System.out.println(i + 1);
        }
    }
}
public static void modify(int[] arr, int value) {
    while (arr[value - 1] != value) {
        int temp = arr[value - 1];
        arr[value - 1] = value;
        value = temp;
    }
}
```



## 最少花费金币数

CC里面有一个土豪很喜欢一位女直播Kiki唱歌，平时就经常给她点赞、送礼、私聊。最近CC直播平台在举行中秋之星主播唱歌比赛，假设一开始该女主播的初始人气值为start， 能够晋升下一轮人气需要刚好达到end， 土豪给主播增加人气的可以采取的方法有： 

a. 点赞 花费x C币，人气 + 2 

b. 送礼 花费y C币，人气 * 2 

c. 私聊 花费z C币，人气 - 2 

其中 end 远大于start且end为偶数， 请写一个程序帮助土豪计算一下，最少花费多少C币就能帮助该主播Kiki将人气刚好达到end，从而能够晋级下一轮？ 

> 输入描述： 
>
> 第一行输入5个数据，分别为：x y z start end，每项数据以空格分开。 
>
> 其中：0＜x, y, z＜＝10000， 0＜start, end＜＝1000000 
>
> 输出描述： 
>
> 需要花费的最少C币。 
>
> 示例1: 
>
> 输入
>
> 3 100 1 2 6 
>
> 输出
>
> 6

```java
public static int minCcoins1(int add, int times, int del, int start, int end) {
		if (start > end) {
			return -1;
		}
		return process(0, end, add, times, del, start, end * 2, ((end - start) / 2) * add);
	}

public static int process(int pre, int aim, int add, int times, int del, int finish, int limitAim, int limitCoin) {
	if (pre > limitCoin) {
		return Integer.MAX_VALUE;
	}
	if (aim < 0) {
		return Integer.MAX_VALUE;
	}
	if (aim > limitAim) {
		return Integer.MAX_VALUE;
	}
	if (aim == finish) {
		return pre;
	}
	int min = Integer.MAX_VALUE;
	int p1 = process(pre + add, aim - 2, add, times, del, finish, limitAim, limitCoin);
	if (p1 != Integer.MAX_VALUE) {
		min = p1;
	}
	int p2 = process(pre + del, aim + 2, add, times, del, finish, limitAim, limitCoin);
	if (p2 != Integer.MAX_VALUE) {
		min = Math.min(min, p2);
	}
	if ((aim & 1) == 0) {
		int p3 = process(pre + times, aim / 2, add, times, del, finish, limitAim, limitCoin);
		if (p3 != Integer.MAX_VALUE) {
			min = Math.min(min, p3);
		}
	}
	return min;
}


public static int minCcoins2(int add, int times, int del, int start, int end) {
		if (start > end) {
			return -1;
		}
		int limitCoin = ((end - start) / 2) * add;
		int limitAim = end * 2;
		int[][] dp = new int[limitCoin + 1][limitAim + 1];
		for (int pre = 0; pre <= limitCoin; pre++) {
			for (int aim = 0; aim <= limitAim; aim++) {
				if (aim == start) {
					dp[pre][aim] = pre;
				} else {
					dp[pre][aim] = Integer.MAX_VALUE;
				}
			}
		}
		for (int pre = limitCoin; pre >= 0; pre--) {
			for (int aim = 0; aim <= limitAim; aim++) {
				if (aim - 2 >= 0 && pre + add <= limitCoin) {
					dp[pre][aim] = Math.min(dp[pre][aim], dp[pre + add][aim - 2]);
				}
				if (aim + 2 <= limitAim && pre + del <= limitCoin) {
					dp[pre][aim] = Math.min(dp[pre][aim], dp[pre + del][aim + 2]);
				}
				if ((aim & 1) == 0) {
					if (aim / 2 >= 0 && pre + times <= limitCoin) {
						dp[pre][aim] = Math.min(dp[pre][aim], dp[pre + times][aim / 2]);
					}
				}
			}
		}
		return dp[0][end];
	}
```





## 没有重复字符的最长子串

在一个字符串中找到没有重复字符子串中最长的长度。 

例如： 

abcabcbb没有重复字符的最长子串是abc，长度为3 

bbbbb，答案是b，长度为1 

pwwkew，答案是wke，长度是3 

要求：答案必须是子串，"pwke" 是一个子字符序列但不是一个子字符串。

```java
public static int noRepeatMaxLen(String str) {
    if (str == null || str.length() == 0) {
        return 0;
    }
    char[] chars = str.toCharArray();
    int[] map = new int[256];
    for (int i = 0; i < 256; i++) {
        map[i] = -1;
    }
    int len = 0;
    int pre = -1;
    for (int i = 0; i < chars.length; i++) {
        pre = Math.max(pre, map[chars[i]]);
        len = Math.max(len, i - pre);
        map[chars[i]] = i;
    }
    return len;
}
```

如果需要求该子串

```java
public static String noRepeatMaxSubStr(String str) {
    if (str == null || str.equals("")) {
        return "";
    }
    char[] chars = str.toCharArray();
    int[] map = new int[256];
    for (int i = 0; i < 256; i++) {
        map[i] = -1;
    }
    int pre = -1;
    int len = -1;
    int end = -1;
    int cur = 0;
    for (int i = 0; i < chars.length; i++) {
        pre = Math.max(pre, map[chars[i]]);
        cur = i - pre;
        if (cur > len) {
            end = i;
            len = cur;
        }
        map[chars[i]] = i;
    }
    return str.substring(end - len + 1, end + 1);
}
```





给定一个只由 0(假)、1(真)、&(逻辑与)、|(逻辑或)和^(异或)五种字符组成的字符串express，再给定一个布尔值 desired。返回express能有多少种组合方式，可以达到desired的结果。 

【举例】 

express="1^0|0|1"，desired=false 

只有 1^((0|0)|1)和 1^(0|(0|1))的组合可以得到 false，返回 2。 

express="1"，desired=false 

无组合则可以得到false，返回0

```java
public static int num1(String express, boolean desired) {
    if (express == null || express.equals("")) {
        return 0;
    }
    char[] exp = express.toCharArray();
    if (!isValid(exp)) {
        return 0;
    }
    return p(exp, desired, 0, exp.length - 1);
}
public static int p(char[] exp, boolean desired, int L, int R) {
    if (L == R) {
        if (exp[L] == '1') {
            return desired ? 1 : 0;
        } else {
            return desired ? 0 : 1;
        }
    }
    int res = 0;
    if (desired) {
        for (int i = L + 1; i < R; i += 2) {
            switch (exp[i]) {
                case '&':
                    res += p(exp, true, L, i - 1) * p(exp, true, i + 1, R);
                    break;
                case '|':
                    res += p(exp, true, L, i - 1) * p(exp, false, i + 1, R);
                    res += p(exp, false, L, i - 1) * p(exp, true, i + 1, R);
                    res += p(exp, true, L, i - 1) * p(exp, true, i + 1, R);
                    break;
                case '^':
                    res += p(exp, true, L, i - 1) * p(exp, false, i + 1, R);
                    res += p(exp, false, L, i - 1) * p(exp, true, i + 1, R);
                    break;
            }
        }
    } else {
        for (int i = L + 1; i < R; i += 2) {
            switch (exp[i]) {
                case '&':
                    res += p(exp, false, L, i - 1) * p(exp, true, i + 1, R);
                    res += p(exp, true, L, i - 1) * p(exp, false, i + 1, R);
                    res += p(exp, false, L, i - 1) * p(exp, false, i + 1, R);
                    break;
                case '|':
                    res += p(exp, false, L, i - 1) * p(exp, false, i + 1, R);
                    break;
                case '^':
                    res += p(exp, true, L, i - 1) * p(exp, true, i + 1, R);
                    res += p(exp, false, L, i - 1) * p(exp, false, i + 1, R);
                    break;
            }
        }
    }
    return res;
}
public static boolean isValid(char[] exp) {
    if ((exp.length & 1) == 0) {
        return false;
    }
    for (int i = 0; i < exp.length; i += 2) {
        if ((exp[i] != '1') && (exp[i] != '0')) {
            return false;
        }
    }
    for (int i = 1; i < exp.length; i += 2) {
        if ((exp[i] != '&') && (exp[i] != '|') && (exp[i] != '^')) {
            return false;
        }
    }
    return true;
}
```

改dp

```java
public static int num2(String express, boolean desired) {
		if (express == null || express.equals("")) {
			return 0;
		}
		char[] exp = express.toCharArray();
		if (!isValid(exp)) {
			return 0;
		}
		int[][] t = new int[exp.length][exp.length];
		int[][] f = new int[exp.length][exp.length];
		t[0][0] = exp[0] == '0' ? 0 : 1;
		f[0][0] = exp[0] == '1' ? 0 : 1;
		for (int i = 2; i < exp.length; i += 2) {
			t[i][i] = exp[i] == '0' ? 0 : 1;
			f[i][i] = exp[i] == '1' ? 0 : 1;
			for (int j = i - 2; j >= 0; j -= 2) {
				for (int k = j; k < i; k += 2) {
					if (exp[k + 1] == '&') {
						t[j][i] += t[j][k] * t[k + 2][i];
						f[j][i] += (f[j][k] + t[j][k]) * f[k + 2][i] + f[j][k] * t[k + 2][i];
					} else if (exp[k + 1] == '|') {
						t[j][i] += (f[j][k] + t[j][k]) * t[k + 2][i] + t[j][k] * f[k + 2][i];
						f[j][i] += f[j][k] * f[k + 2][i];
					} else {
						t[j][i] += f[j][k] * t[k + 2][i] + t[j][k] * f[k + 2][i];
						f[j][i] += f[j][k] * f[k + 2][i] + t[j][k] * t[k + 2][i];
					}
				}
			}
		}
		return desired ? t[0][t.length - 1] : f[0][f.length - 1];
```





## 设计LRU缓存结构，该结构在构造时确定大小，假设大小为K，并有如下两个功能。 

set(key,value):将记录(key,value)插入该结构。 

get(key):返回key对应的value值。 

【要求】 

1.set和get方法的时间复杂度为O(1) 

2.某个key的set或get操作一旦发生，认为这个key的记录成了最常使用的 

3.当缓存的大小超过K时，移除最不经常使用的记录，即set或get最久远的 

【举例】 

假设缓存结构的实例是cache，大小为3，并依次发生如下行为 

1.cache.set("A",1)。最常使用的记录为("A",1) 

2.cache.set("B",2)。最常使用的记录为("B",2)，("A",1)变为最不常使用的 

3.cache.set("C",3)。最常使用的记录为("C",2)，("A",1)还是最不常使用的 

4.cache.get("A")。最常使用的记录为("A",1)，("B",2)变为最不常使用的 

5.cache.set("D",4)。大小超过了3，所以移除此时最不常使用的记录("B",2)，加入记录 

("D",4)，并且为最常使用的记录，然后("C",2)变为最不常使用的记录。

```java
public class LRUCache {

    class Node {
        int key;
        int value;
        Node prev;
        Node next;

        public Node() {
        }

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private int size;
    private int capacity;
    private Node head;
    private Node tail;
    private Map<Integer, Node> cache = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = new Node();
        this.tail = new Node();
        this.head.next = tail;
        this.tail.prev = head;
    }

    public int get(int key) {
        Node node = cache.get(key);
        if (node == null) {
            return -1;
        }
        moveToHead(node);
        return node.value;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(Node node) {
        node.next = head.next;
        head.next.prev = node;
        node.prev = head;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    public void put(int key, int value) {
        Node node = cache.get(key);
        if (node == null) {
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addToHead(newNode);
            size++;
            if (size > capacity) {
                removeNode(tail.prev);
                size--;
            }
        } else {
            node.value = value;
            moveToHead(node);
        }
    }
}
```





## 给出一组正整数，你从第一个数向最后一个数方向跳跃，每次至少跳跃1格，每个数的值表示你从这个位置可以跳跃的最大长度。计算如何以最少的跳跃次数跳到最后一个数。

```java
public static int jump(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int jump = 0;
    int cur = 0;
    int next = 0;
    for (int i = 0; i < arr.length; i++) {
        if (cur < i) {
            jump++;
            cur = next;
        }
        next = Math.max(next, i + arr[i]);
    }
    return jump;
}
```



## 给定两个有序数组arr1和arr2，再给定一个整数k，返回来自arr1和arr2的两个数相加和最大的前k个，两个数必须分别来自两个数组。 

【举例】 

arr1=[1,2,3,4,5]，arr2=[3,5,7,9,11]，k=4。 返回数组[16,15,14,14] 

【要求】 

时间复杂度达到 O(klogk)

```java
public static class Node {
    public int index1;
    public int index2;
    public int sum;

    public Node(int index1, int index2, int sum) {
        this.index1 = index1;
        this.index2 = index2;
        this.sum = sum;
    }
}

public static int[] topKSum(int[] arr1, int[] arr2, int topK) {
    if (arr1 == null || arr2 == null || topK < 1) {
        return null;
    }
    topK = Math.min(topK, arr1.length * arr2.length);
    int[] res = new int[topK];
    int resIndex = 0;
    PriorityQueue<Node> maxHeap = new PriorityQueue<>((o1, o2) -> o2.sum - o1.sum);
    boolean[][] set = new boolean[arr1.length][arr2.length];
    int i1 = arr1.length - 1;
    int i2 = arr2.length - 1;
    maxHeap.add(new Node(i1, i2, arr1[i1] + arr2[i2]));
    set[i1][i2] = true;
    while (resIndex != topK) {
        Node curNode = maxHeap.poll();
        res[resIndex++] = curNode.sum;
        i1 = curNode.index1;
        i2 = curNode.index2;
        if (i1 - 1 >= 0 && !set[i1 - 1][i2]) {
            set[i1 - 1][i2] = true;
            maxHeap.add(new Node(i1 - 1, i2, arr1[i1 - 1] + arr2[i2]));
        }
        if (i2 - 1 >= 0 && !set[i1][i2 - 1]) {
            set[i1][i2 - 1] = true;
            maxHeap.add(new Node(i1, i2 - 1, arr1[i1] + arr2[i2 - 1]));
        }
    }
    return res;
}
```





## 给定三个字符串str1、str2和aim，如果aim包含且仅包含来自str1和str2的所有字符， 而且在aim中属于str1的字符之间保持原来在str1中的顺序，属于str2的字符之间保持原来在str2中的顺序，那么称aim是str1和str2的交错组成。实现一个函数，判断aim是否是str1和str2交错组成 

【举例】 

   str1="AB"，str2="12"。那么"AB12"、"A1B2"、"A12B"、"1A2B"和"1AB2"等都是 str1和 str2 的 交错组成。

```java
public static boolean isCross(String str1, String str2, String aim) {
    if (str1 == null || str2 == null || aim == null) {
        return false;
    }
    char[] ch1 = str1.toCharArray();
    char[] ch2 = str2.toCharArray();
    char[] chaim = aim.toCharArray();
    if (chaim.length != ch1.length + ch2.length) {
        return false;
    }
    int N = ch1.length, M = ch2.length;
    boolean[][] dp = new boolean[N + 1][M + 1];
    dp[0][0] = true;
    for (int i = 1; i <= N; i++) {
        if (ch1[i - 1] != chaim[i - 1]) {
            break;
        }
        dp[i][0] = true;
    }
    for (int j = 1; j <= M; j++) {
        if (ch2[j - 1] != chaim[j - 1]) {
            break;
        }
        dp[0][j] = true;
    }
    for (int i = 1; i <= N; i++) {
        for (int j = 1; j <= M; j++) {
            if ((ch1[i - 1] == chaim[i + j - 1] && dp[i - 1][j])
                    || (ch2[j - 1] == chaim[i + j - 1] && dp[i][j - 1])
            ) {
                dp[i][j] = true;
            }
        }
    }
    return dp[N][M];
}
```



## 一个数的因子仅仅包括2，3，5的数称为丑数。 数字1特别对待也看作是丑数，所以从1開始的10个丑数分别为1、2、3、4、5、6、8、9、 10、12  返回第n个丑数

```java
public static int uglyNumber(int n) {
    int[] help = new int[n];
    help[0] = 1;
    int i2 = 0;
    int i3 = 0;
    int i5 = 0;
    int index = 1;
    while (index < n) {
        help[index] = Math.min(2 * help[i2], Math.min(3 * help[i3], 5 * help[i5]));
        if (help[index] == 2 * help[i2]) {
            i2++;
        }
        if (help[index] == 3 * help[i3]) {
            i3++;
        }
        if (help[index] == 5 * help[i5]) {
            i5++;
        }
        index++;
    }
    System.out.println(Arrays.toString(help));
    return help[n - 1];
}
```



## 给定一个无序数组arr，如果只能对一个子数组进行排序，但是想让数组整体都 有序，求需要排序的最短子数组长度。例如:arr = [1,5,3,4,2,6,7]返回4，因为只有[5,3,4,2]需要排序

```java
public static int maxSortLen(int[] arr) {
    if (arr == null || arr.length < 2) {
        return 0;
    }
    int max = Integer.MIN_VALUE;
    int len = arr.length;
    int right = 0;
    for (int i = 0; i < len; i++) {
        if (max > arr[i]) {
            right = i;
        }
        max = Math.max(max, arr[i]);
    }
    int min = Integer.MAX_VALUE;
    int left = 0;
    for (int i = len - 1; i >= 0; i--) {
        if (min < arr[i]) {
            left = i;
        }
        min = Math.min(min, arr[i]);
    }
    return right == left ? 0 : right - left + 1;
}
```





## 给定一个正数数组 arr，其中所有的值都为整数，以下是最小不可组成和的概念:把 arr 每个子集内的所有元素加起来会出现很多值，其中最小的记为 min，最大的记为max 在区间[min,max]上，如果有数不可以被arr某一个子集相加得到，那么其中最小的那个数是arr 的最小不可组成和 在区间[min,max]上，如果所有的数都可以被arr的某一个子集相加得到，那么max+1是arr的最小不可组成和

 请写函数返回正数数组 arr 的最小不可组成和。

【举例】
 arr=[3,2,5]。子集{2}相加产生 2 为 min，子集{3,2,5}相加产生 10 为 max。在区间[2,10] 上，4、 6 和 9 不能被任何子集相加得到，其中 4 是 arr 的最小不可组成和。 arr=[1,2,4]。子集{1}相加产生 1 为 min，子集{1,2,4}相加产生 7 为 max。在区间[1,7]上， 任何 数都可以被子集相加得到，所以 8 是 arr 的最小不可组成和。

```java
public static int unformedNum1(int[] arr) {
    int min = Integer.MAX_VALUE;
    int sum = 0;
    for (int j : arr) {
        min = Math.min(min, j);
        sum += j;
    }
    int N = arr.length;
    boolean[][] dp = new boolean[N][sum + 1];
    dp[0][arr[0]] = true;
    for (int i = 1; i < N; i++) {
        for (int j = 1; j < sum + 1; j++) {
            if (arr[i] == j) {
                dp[i][j] = true;
            }
            else if (dp[i - 1][j]) {
                dp[i][j] = true;
            }
            else if (j - arr[i] >= 0 && dp[i - 1][j - arr[i]]) {
                dp[i][j] = true;
            }
        }
    }
    int ans = min;
    while (ans <= sum) {
        if (!dp[N - 1][ans]) {
            return ans;
        }
        ans++;
    }
    return sum + 1;
}
```

【进阶】
 如果已知正数数组 arr 中肯定有 1 这个数，是否能更快地得到最小不可组成和?

```java
public static int unformedNum2(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int range = 1;
    for (int i = 1; i < arr.length; i++) {
        if (arr[i] > range + 1) {
            return range + 1;
        } else {
            range += arr[i];
        }
    }
    return range + 1;
}
```





## 给定一个有序的正数数组arr和一个正数range，如果可以自由选择arr中的数字，想累加得 到 1~range 范围上所有的数，返回arr最少还缺几个数。

【举例】
 arr = {1,2,3,7}，range = 15
 想累加得到 1~15 范围上所有的数，arr 还缺 14 这个数，所以返回1 arr = {1,5,7}，range = 15
 想累加得到 1~15 范围上所有的数，arr 还缺 2 和 4，所以返回2

```java
public static int minPatches(int[] arr, int range) {
    int patches = 0;
    long touch = 0;
    for (int i = 0; i < arr.length; i++) {
        while (arr[i] > touch + 1) {
            touch += touch + 1;
            patches++;
            if (touch >= range) {
                return patches;
            }
        }
        touch += arr[i];
        if (touch >= range) {
            return patches;
        }
    }
    while (range >= touch + 1) {
        touch += touch + 1;
        patches++;
    }
    return patches;
}
```



给定一个不含有1的正整数组arr，假设其中任意两个数a和b，如果a和b的最大公约数比1大，那么认为a和b之间有路相连；如果a和b的最大公约数比1大，那么a和b之间有路相连，如果是1，则没路，那么数组arr中所有的数字就可以组成一张图

1、求arr中有多少个连通区域

2、求arr中的最大的连通区域中有多少个数

```java
package com.qianlou.code;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Code02 {

    public static void main(String[] args) {
        int[] test = { 2, 3, 6, 7, 4, 12, 21, 39 };
        System.out.println(Arrays.toString(largestComponentSize2(test)));
    }

    public static int[] largestComponentSize2(int[] arr) {
        UnionFindSet unionFindSet = new UnionFindSet(arr.length);
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            int n = (int) Math.sqrt(arr[i]);
            for (int j = 1; j <= n; j++) {
                if (arr[i] % j == 0) {
                    if (j != 1) {
                        if (!map.containsKey(j)) {
                            map.put(j, i);
                        }
                        else {
                            unionFindSet.union(i, map.get(j));
                        }
                    }
                    int other = arr[i] / j;
                    if (other != 1) {
                        if (!map.containsKey(other)) {
                            map.put(other, i);
                        }
                        else {
                            unionFindSet.union(i, map.get(other));
                        }
                    }
                }
            }
        }
        return new int[] {unionFindSet.size(), unionFindSet.maxSize()};
    }
}


class UnionFindSet {
    private int[] parent;
    private int[] size;

    public UnionFindSet(int len) {
        parent = new int[len];
        size = new int[len];
        for (int i = 0; i < len; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int size() {
        int count = 0;
        for (int i = 0; i < size.length; i++) {
            if (size[i] != 0) {
                count++;
            }
        }
        return count;
    }

    public int maxSize() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < size.length; i++) {
            max = Math.max(max, size[i]);
        }
        return max;
    }

    public int findHead(int index) {
        Stack<Integer> path = new Stack<>();
        while (index != parent[index]) {
            path.push(index);
            index = parent[index];
        }
        while (!path.isEmpty()) {
            parent[path.pop()] = index;
        }
        return index;
    }

    public void union(int index1, int index2) {
        int head1 = findHead(index1);
        int head2 = findHead(index2);
        if (head1 != head2) {
            int big = size[head1] >= size[head2] ? head1 : head2;
            int small = big == head2 ? head1 : head2;
            parent[small] = big;
            size[big] += size[small];
            size[small] = 0;
        }
    }

}
```



# 先给出可整合数组的定义:如果一个数组在排序之后，每相邻两个数差的绝对值 都为 1， 则该数组为可整合数组。例如，[5,3,4,6,2]排序之后为[2,3,4,5,6]， 符合每相邻两个数差的绝对值 都为 1，所以这个数组为可整合数组。 给定一个整型数组 arr，请返回其中最大可整合子数组的长度。例如， [5,5,3,2,6,4,3]的最大 可整合子数组为[5,3,2,6,4]，所以返回 5。

```java
public static int getLIT(int[] arr) {
    if (arr == null || arr.length == 0) {
        return 0;
    }
    int min = 0;
    int max = 0;
    int ans = 0;
    HashSet<Integer> set = new HashSet<>();
    for (int i = 0; i < arr.length; i++) {
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
        for (int j = i; j < arr.length; j++) {
            if (set.contains(arr[j])) {
                break;
            }
            set.add(arr[j]);
            max = Math.max(max, arr[j]);
            min = Math.min(min, arr[j]);
            if (max - min == j - i) {
                ans = Math.max(ans, j - i + 1);
            }
        }
        set.clear();
    }
    return ans;
}
```





# 一个矩阵可以由左上角点的坐标和右下角点的坐标代表。给定一些矩阵，请返回这些矩阵全拼在一起，能否拼出完美的矩形?完美的矩形指，内部没有重叠、没有突出和凹陷、所有矩形正好拼成一块。

```java
public boolean isRectangleCover(int[][] rectangles) {
    if (rectangles == null || rectangles.length == 0) {
        return false;
    }
    int x1 = Integer.MAX_VALUE;
    int x2 = Integer.MIN_VALUE;
    int y1 = Integer.MAX_VALUE;
    int y2 = Integer.MIN_VALUE;
    int area = 0;
    HashSet<String> set = new HashSet<>();
    for (int[] rects : rectangles) {
        x1 = Math.min(rects[0], x1);
        y1 = Math.min(rects[1], y1);
        x2 = Math.max(rects[2], x2);
        y2 = Math.max(rects[3], y2);
        area += (rects[2] - rects[0]) * (rects[3] - rects[1]);
        String s1 = rects[0] + "_" + rects[1];
        String s2 = rects[0] + "_" + rects[3];
        String s3 = rects[2] + "_" + rects[1];
        String s4 = rects[2] + "_" + rects[3];
        if (!set.add(s1)) set.remove(s1);
        if (!set.add(s2)) set.remove(s2);
        if (!set.add(s3)) set.remove(s3);
        if (!set.add(s4)) set.remove(s4);
    }
    if (!set.contains(x1 + "_" + y1) || !set.contains(x1 + "_" + y2)
    || !set.contains(x2 + "_" + y1) || !set.contains(x2 + "_" + y2)
            || set.size() != 4
    ) {
        return false;
    }
    return area == (y2 - y1) * (x2 - x1);
}
```



# 一种消息接收并打印的结构设计

已知一个消息流会不断地吐出整数 1~N，但不一定按照顺序吐出。如果上次打印的数为 i， 那么当 i+1 出现时，请打印 i+1 及其之后接收过的并且连续的所有数，直到 1~N 全部接收 并打印完，请设计这种接收并打印的结构。初始时默认i==0

```java
public class ReceiveAndPrintOrderLine {

    public static void main(String[] args) {
        MessageBox box = new MessageBox();
        int[] arr = new int[] {2, 3, 1, 4, 5,  6, 8, 7, 10, 9, 15, 11, 12, 13, 14};
        for (int num : arr) {
            box.receive(num);
        }
    }

    public static class Node {
        public int num;
        public Node next;

        public Node(int num) {
            this.num = num;
        }
    }

    public static class MessageBox {
        private HashMap<Integer, Node> headMap;
        private HashMap<Integer, Node> tailMap;

        private int lastPrint;

        public MessageBox() {
            this.headMap = new HashMap<>();
            this.tailMap = new HashMap<>();
            this.lastPrint = 1;
        }

        public void receive(int num) {
            if (num < 1) {
                return;
            }
            Node cur = new Node(num);
            tailMap.put(num, cur);
            headMap.put(num, cur);
            if (tailMap.containsKey(num - 1)) {
                tailMap.get(num - 1).next = cur;
                tailMap.remove(num - 1);
                headMap.remove(num);
            }
            if (headMap.containsKey(num + 1)) {
                cur.next = headMap.get(num + 1);
                headMap.remove(num + 1);
                tailMap.remove(num);
            }
            if (headMap.containsKey(lastPrint)) {
                print();
            }
        }
        public void print() {
            Node cur = headMap.get(lastPrint);
            while (cur != null) {
                System.out.print(cur.num + " ");
                cur = cur.next;
                lastPrint++;
            }
            tailMap.remove(lastPrint - 1);
        }
    }
}
```





# 给定k个有序链表的头节点，怎么把他们merge成一个有序的链表。

```java
public class Code03 {

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    public static class ListNodeComparators implements Comparator<ListNode> {

        @Override
        public int compare(ListNode o1, ListNode o2) {
            return o1.val - o2.val;
        }
    }

    public static ListNode mergeLists(ListNode[] lists) {
        PriorityQueue<ListNode> queue = new PriorityQueue<>(new ListNodeComparators());
        queue.addAll(Arrays.asList(lists));
        ListNode newHead = new ListNode(-1);
        ListNode cur = newHead;
        while (!queue.isEmpty()) {
            ListNode node = queue.poll();
            cur.next = node;
            if (node.next != null) {
                queue.add(node.next);
            }
            cur = cur.next;
        }
        return newHead.next;
    }
    public static void print(ListNode head) {
        ListNode cur = head;
        while (cur != null) {
            System.out.print(cur.val + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node6 = new ListNode(6);
        ListNode node7 = new ListNode(7);
        ListNode node8 = new ListNode(8);
        ListNode node9 = new ListNode(9);
        node1.next = node4;
        node4.next = node7;
        node2.next = node5;
        node5.next = node8;
        node3.next = node6;
        node6.next = node9;
        print(node1);
        print(node2);
        print(node3);
        print(mergeLists(new ListNode[]{node1, node2, node3}));
    }
}
```



# 有k个有序的数组，请找到一个最小的数字范围。使得这k个有序数组中，每个数组都至少有一个数字在该范围中。

```java
public class Code04 {

    public static class Node {
        public int val;
        public int arrNo;
        public int index;

        public Node(int val, int arrNo, int index) {
            this.val = val;
            this.arrNo = arrNo;
            this.index = index;
        }
    }
    public static int[] minRange(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return new int[] {};
        }
        TreeSet<Node> set = new TreeSet<>(Comparator.comparingInt(o -> o.val));
        for (int arrNo = 0; arrNo < matrix.length; arrNo++) {
            set.add(new Node(matrix[arrNo][0], arrNo, 0));
        }
        int begin = -1, end = -1;
        boolean isSet = false;
        while (true) {
            Node minNode = set.first();
            Node maxNode = set.last();
            if (!isSet) {
                begin = minNode.val;
                end = maxNode.val;
                isSet = true;
            } else if (end - begin > maxNode.val - minNode.val){
                begin = minNode.val;
                end = maxNode.val;
            }
            set.pollFirst();
            if (minNode.index == matrix[minNode.arrNo].length - 1) {
                break;
            }
            set.add(new Node(matrix[minNode.arrNo][minNode.index + 1], minNode.arrNo, minNode.index + 1));
        }
        return new int[] {begin, end};
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][] {{4, 10, 15, 24, 26}, {0, 9, 12, 20}, {5, 18, 22, 30}};
        System.out.println(Arrays.toString(minRange(matrix)));
    }
}
```





# 给定一个无序整型数组 arr，找到数组中未出现的最小正整数。要求时间复杂度O(N)，额 外空间复杂度O(1)

```java
public static int missNum(int[] arr) {
   int left = 0;
   int right = arr.length;
   while (left < right) {
       if (arr[left] == left + 1) {
           left++;
       } else if (arr[left] <= left || arr[left] > right || arr[arr[left] - 1] == arr[left]) {
           swap(arr, left, --right);
       } else {
           swap(arr, left, arr[left] - 1);
       }
   }
   return left + 1;
}

public static void swap(int[] arr, int index1, int index2) {
    int tmp = arr[index1];
    arr[index1] = arr[index2];
    arr[index2] = tmp;
}
```



# 给定一个有序数组arr，和一个整数aim，请不重复打印arr中所有累加和为aim的二元组。 给定一个有序数组arr，和一个整数aim，请不重复打印arr中所有累加和为aim的三元组。

```java
public static void printUniquePair(int[] arr, int target) {
    if (arr == null || arr.length < 2) {
        return;
    }
    int left = 0, right = arr.length - 1;
    while (left < right) {
        if (arr[left] + arr[right] > target) {
            right--;
        } else if (arr[left] + arr[right] < target) {
            left++;
        } else {
            if (left == 0 || arr[left] != arr[left - 1]) {
                System.out.println("left=" + arr[left] + ", right=" + arr[right]);
            }
            left++;
            right--;
        }
    }
}
```

```java
public static void printUniqueTriad(int[] arr, int target) {
    if (arr == null || arr.length < 3) {
        return;
    }
    for (int i = 0; i < arr.length - 2; i++) {
        if (i == 0 || arr[i - 1] != arr[i]) {
            printRest(arr, i, i + 1, arr.length - 1, target - arr[i]);
        }
    }
}

public static void printRest(int[] arr, int f, int left, int right, int target) {
    while (left < right) {
        if (arr[left] + arr[right] > target) {
            right--;
        } else if (arr[left] + arr[right] < target) {
            left++;
        } else {
            if (left == f + 1 || arr[left] != arr[left - 1]) {
                System.out.println("f=" + arr[f] + ", left=" + arr[left] + ", right=" + arr[right]);
            }
            left++;
            right--;
        }
    }
}
```





# 给定一个整型矩阵nums，每个位置都可以走向左、右、上、下四个方向，找到 其中最长的递增路径

 【例子】
 nums =

[ [9,9,4], [6,6,8],

] [2,1,1]

输出:4 最长的递增路径为:[1, 2, 6, 9] nums =

[ [3,4,5], [3,2,6],

] [2,2,1]
 输出:4 最长的递增路径为:[3, 4, 5, 6]

```java
public static int longestIncreasingPath(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return 0;
    }
    int N = matrix.length;
    int M = matrix[0].length;
    int[][] cache = new int[N][M];
    int ans = 0;
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            ans = Math.max(ans, process(matrix, i + 1, j, matrix[i][j], cache) + 1);
            ans = Math.max(ans, process(matrix, i - 1, j, matrix[i][j], cache) + 1);
            ans = Math.max(ans, process(matrix, i, j + 1, matrix[i][j], cache) + 1);
            ans = Math.max(ans, process(matrix, i, j - 1, matrix[i][j], cache) + 1);
        }
    }
    return ans;
}
public static int process(int[][] matrix, int row, int col, int lastValue, int[][] cache) {
    if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[0].length || matrix[row][col] <= lastValue) {
        return 0;
    }
    if (cache[row][col] == 0) {
        cache[row][col] = process(matrix, row - 1, col, matrix[row][col], cache) + 1;
        cache[row][col] = Math.max(cache[row][col], process(matrix, row + 1, col, matrix[row][col], cache) + 1);
        cache[row][col] = Math.max(cache[row][col], process(matrix, row, col - 1, matrix[row][col], cache) + 1);
        cache[row][col] = Math.max(cache[row][col], process(matrix, row, col + 1, matrix[row][col], cache) + 1);
    }
    return cache[row][col];
}
```





# 给定一个整型矩阵nums和一个整数K，找到不大于K的最大子矩阵累加和 

```java
public static int maxSumSubmatrix(int[][] matrix, int k) {
		if (matrix == null || matrix[0] == null)
			return 0;
		int row = matrix.length, col = matrix[0].length, res = Integer.MIN_VALUE;
		TreeSet<Integer> sumSet = new TreeSet<>();
		for (int s = 0; s < row; s++) {
			int[] colSum = new int[col];
			for (int e = s; e < row; e++) {
				sumSet.add(0);
				int rowSum = 0;
				for (int c = 0; c < col; c++) {
					colSum[c] += matrix[e][c];
					rowSum += colSum[c];
					Integer it = sumSet.ceiling(rowSum - k);
					if (it != null) {
						res = Math.max(res, rowSum - it);
					}
					sumSet.add(rowSum);
				}
				sumSet.clear();
			}
		}
		return res;
	}
```



# 克隆图

无向图节点类型如下

```java
class UndirectedGraphNode { int label;

List<UndirectedGraphNode> neighbors;

UndirectedGraphNode(int x) { label = x;

neighbors = new ArrayList<UndirectedGraphNode>(); }

} 
```

由以上的节点结构组成了一张无向图，给定一个出发点node，请你克隆整张图

```java
public static UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
    if (node == null) {
        return null;
    }
    UndirectedGraphNode head = new UndirectedGraphNode(node.label);
    LinkedList<UndirectedGraphNode> queue = new LinkedList<>();
    HashMap<UndirectedGraphNode, UndirectedGraphNode> map = new HashMap<>();
    queue.add(node);
    map.put(node, head);
    while (!queue.isEmpty()) {
        UndirectedGraphNode cur = queue.poll();
        for (UndirectedGraphNode next : cur.neighbors) {
            if (!map.containsKey(next)) {
                UndirectedGraphNode copy = new UndirectedGraphNode(next.label);
                map.put(next, copy);
                queue.add(next);
            }
            
        }
    }
    for (Map.Entry<UndirectedGraphNode, UndirectedGraphNode> entry : map.entrySet()) {
        UndirectedGraphNode cur = entry.getKey();
        UndirectedGraphNode copy = entry.getValue();
        for (UndirectedGraphNode next : cur.neighbors) {
            copy.neighbors.add(map.get(next));
        }
    }
    return head;
}
```





# 给定一个由字符组成的矩阵board，还有一个字符串数组words，里面有很多字符串(word)。 在board中找word是指，从board某个位置出发，每个位置都可以走向左、右、上、下四个方 向，但不能重复经过一个位置。返回在board中能找到哪些word。

 【例子】

board =

[ ['o','a','a','n'], ['e','t','a','e'], ['i','h','k','r'],

] ['i','f','l','v']

words = ["oath","pea","eat","rain"]
 输出: ["eat","oath"] 从第2行最右边的'e'出发，向左找到'a'，再向左找到't'，就搞定了"eat" 从第1行最左边的'o'出发，向右找到'a'，向下找到't'，再向下找到'h'，就搞定了"oath"

```java
public class Code08 {

    public class TrieNode {
        public TrieNode[] nexts;
        public int pass;
        public int end;

        public TrieNode() {
            nexts = new TrieNode[26];
            pass = 0;
            end = 0;
        }
    }

    public String generatePath(LinkedList<Character> path) {
        char[] str = new char[path.size()];
        int index = 0;
        for (Character cha : path) {
            str[index++] = cha;
        }
        return String.valueOf(str);
    }

    public List<String> findWords(char[][] board, String[] words) {
        HashSet<String> set = new HashSet<>();
        TrieNode head = new TrieNode();
        for (String word : words) {
            if (!set.contains(word)) {
                fillWord(head, word);
                set.add(word);
            }
        }
        List<String> res = new ArrayList<>();
        LinkedList<Character> path = new LinkedList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                process(board, row, col, path, head, res);
            }
        }
        return res;
    }

    public int process(char[][] board, int row, int col, LinkedList<Character> path, TrieNode cur, List<String> res) {
        char cha = board[row][col];
        if (cha == '0') {
            return 0;
        }
        int index = cha - 'a';
        if (cur.nexts[index] == null || cur.nexts[index].pass == 0) {
            return 0;
        }
        cur = cur.nexts[index];
        path.addLast(cha);
        int fix = 0;
        if (cur.end > 0) {
            res.add(generatePath(path));
            cur.end--;
            fix++;
        }
        board[row][col] = 0;
        if (row > 0) {
            fix += process(board, row - 1, col, path, cur, res);
        }
        if (row < board.length - 1) {
            fix += process(board, row + 1, col, path, cur, res);
        }
        if (col > 0) {
            fix += process(board, row, col - 1, path, cur, res);
        }
        if (col < board[0].length - 1) {
            fix += process(board, row, col + 1, path, cur, res);
        }
        board[row][col] = cha;
        path.pollLast();
        cur.pass -= fix;
        return fix;
    }

    public void fillWord(TrieNode node, String word) {
        node.pass++;
        char[] chs = word.toCharArray();
        int index = 0;
        for (int i = 0; i < chs.length; i++) {
            index = chs[i] - 'a';
            if (node.nexts[index] == null) {
                node.nexts[index] = new TrieNode();
            }
            node = node.nexts[index];
            node.pass++;
        }
        node.end++;
    }
}
```





# 给定一个数组arr，从左到右表示昨天从早到晚股票的价格。作为一个事后诸葛亮，你想知道如 果只做一次交易，且每次交易只买卖一股，返回能挣到的最大钱数

```java
public int maxProfit(int[] prices) {
    if (prices == null || prices.length == 0) {
        return 0;
    }
    int min = prices[0];
    int ans = 0;
    for (int i = 1; i < prices.length; i++) {
        ans = Math.max(ans, prices[i] - min);
        min = Math.min(min, prices[i]);
    }
    return ans;
}
```



# 给定一个数组arr，从左到右表示昨天从早到晚股票的价格。作为一个事后诸葛 亮，你想知道如果随便交易，且每次交易只买卖一股，返回能挣到的最大钱数

```java
public int maxProfit(int[] prices) {
    if (prices == null || prices.length == 0) {
        return 0;
    }
    int ans = 0;
    int min = prices[0];
    int max = prices[0];
    for (int i = 1; i < prices.length; i++) {
        if (prices[i] >= prices[i - 1]) {
            max = prices[i];
        } else {
            ans += max - min;
            min = prices[i];
            max = prices[i];
        }
    }
    return ans + max - min;
}
```





# 给定一个数组arr，从左到右表示昨天从早到晚股票的价格。作为一个事后诸葛亮，你想知 道如果交易次数不超过K次，且每次交易只买卖一股，返回能挣到的最大钱数

```java
public static int maxProfit(int K, int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}
		int N = prices.length;
		if (K >= N / 2) {
			return allTrans(prices);
		}
		int[] dp = new int[N];
		int ans = 0;
		for (int tran = 1; tran <= K; tran++) {
			int pre = dp[0];
			int best = pre - prices[0];
			for (int index = 1; index < N; index++) {
				pre = dp[index];
				dp[index] = Math.max(dp[index - 1], prices[index] + best);
				best = Math.max(best, pre - prices[index]);
				ans = Math.max(dp[index], ans);
			}
		}
		return ans;
	}
```





# 给定两个字符串S和T，返回S子序列等于T的不同子序列个数有多少个? 如果得到子序列A删除的位置与得到子序列B删除的位置不同，那么认为A和B就是不同的。

【例子】

S = "rabbbit", T = "rabbit"
 返回: 3 是以下三个S的不同子序列，没有^的位置表示删除的位置，因为删除的位置不同，所以这三 个子序列是不一样的

rabbbit

^^^^ ^^

rabbbit

^^ ^^^^ 

rabbbit 

^^^ ^^^

```java
public static int numDistinct(String S, String T) {
    char[] s = S.toCharArray();
    char[] t = T.toCharArray();
    int[][] dp = new int[s.length + 1][t.length + 1];
    for (int j = 0; j <= t.length; j++) {
        dp[0][j] = 0;
    }
    for (int i = 0; i <= s.length; i++) {
        dp[i][0] = 1;
    }
    for (int i = 1; i <= s.length; i++) {
        for (int j = 1; j <= t.length; j++) {
            dp[i][j] = dp[i - 1][j] + s[i - 1] == t[j - 1] ? dp[i - 1][j - 1] : 0;
        }
    }
    return dp[s.length][t.length];
}

public static int numDistinct2(String S, String T) {
    char[] s = S.toCharArray();
    char[] t = T.toCharArray();
    int[] dp = new int[t.length + 1];
    for (int j = 0; j <= t.length; j++) {
        dp[j] = 0;
    }
    for (int i = 1; i <= s.length; i++) {
        for (int j = 1; j <= t.length; j++) {
            dp[j] += s[i - 1] == t[j - 1] ? dp[j - 1] : 0;
        }
    }
    return dp[t.length];
}
```





# 给定一个数组，求如果排序之后，相邻两数的最大差值。要求时间复杂度O(N)，且要求不能用非基于比较的排序。

```java
public static int maxGap(int[] nums) {
    if (nums == null || nums.length < 2) {
        return 0;
    }
    int len = nums.length;
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (int num : nums) {
        min = Math.min(min, num);
        max = Math.max(max, num);
    }
    if (max == min) {
        return 0;
    }
    boolean[] hasNum = new boolean[len + 1];
    int[] maxs = new int[len + 1];
    int[] mins = new int[len + 1];
    int bid = 0;
    for (int num : nums) {
        bid = bucket(num, len, min, max);
        mins[bid] = hasNum[bid] ? Math.min(mins[bid], num) : num;
        maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], num) : num;
        hasNum[bid] = true;
    }
    int res = 0;
    int lastMax = maxs[0];
    for (int i = 1; i <= len; i++) {
        if (hasNum[i]) {
            res = Math.max(res, mins[i] - lastMax);
            lastMax = maxs[i];
        }
    }
    return res;
}

public static int bucket(long num, long len, long min, long max) {
    return (int) ((num - min) * len / (max - min));
}
```





# 给出n个数字 a_1,...,a_n，问最多有多少不重叠的非空区间，使得每个区间内数字的 xor都等于0。

```java
public static int mostEOR(int[] nums) {
    int xor = 0;
    int N = nums.length;
    int[] dp = new int[N];
    HashMap<Integer, Integer> map = new HashMap<>();
    map.put(0, -1);
    for (int i = 0; i < N; i++) {
        xor = xor ^ nums[i];
        if (map.containsKey(xor)) {
            int pre = map.get(xor);
            dp[i] = pre == -1 ? 1 : (dp[pre] + 1);
        }
        if (i > 0) {
            dp[i] = Math.max(dp[i - 1], dp[i]);
        }
        map.put(xor, i);
    }
    return dp[N - 1];
}
```
