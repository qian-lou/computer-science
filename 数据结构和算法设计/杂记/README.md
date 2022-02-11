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

