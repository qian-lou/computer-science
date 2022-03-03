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

