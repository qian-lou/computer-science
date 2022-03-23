### 链表

#### **BM1** **反转链表**

给定一个单链表的头结点pHead(该头节点是有值的，比如在下图，它的val是1)，长度为n，反转该链表后，返回新链表的表头。

数据范围： 0≤*n*≤1000

要求：空间复杂度 O(1)*O*(1) ，时间复杂度 O(n)*O*(*n*) 。

如当输入链表{1,2,3}时，经反转后，原链表变为{3,2,1}，所以对应的输出为{3,2,1}。

以上转换过程如下图所示：

![img](https://uploadfiles.nowcoder.com/images/20211014/423483716_1634206291971/4A47A0DB6E60853DEDFCFDF08A5CA249)

示例1

输入：

```
{1,2,3}
```

返回值：

```
{3,2,1}
```

示例2

输入：

```
{}
```

返回值：

```
{}
```

说明：

```
空链表则输出空      
```

**递归解法**：

```java
/*
public class ListNode {
    int val;
    ListNode next = null;

    ListNode(int val) {
        this.val = val;
    }
}*/
public class Solution {
    public ListNode ReverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = ReverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }
}
```

**迭代解法**：

```java
/*
public class ListNode {
    int val;
    ListNode next = null;

    ListNode(int val) {
        this.val = val;
    }
}*/
public class Solution {
    public ListNode ReverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
}
```



#### **BM2** **链表内指定区间反转**

将一个节点数为 size 链表 m 位置到 n 位置之间的区间反转，要求时间复杂度 O(n)，空间复杂度 O(1)。
例如：
给出的链表为 1→2→3→4→5→*N**U**LL*, m=2,n=4
返回1→4→3→2→5→*N**U**LL*.

数据范围： 链表长度 0<*s**i**ze*≤1000，0<*m*≤*n*≤*s**i**ze*，链表中每个节点的值满足 ∣*v**a**l*∣≤1000

要求：时间复杂度 O(n)*O*(*n*) ，空间复杂度 O(n)*O*(*n*)

进阶：时间复杂度 O(n)*O*(*n*)，空间复杂度 O(1)*O*(1)

示例1

输入：

```
{1,2,3,4,5},2,4
```

返回值：

```
{1,4,3,2,5}
```

示例2

输入：

```
{5},1,1
```

返回值：

```
{5}
```



**递归解法**：

```java 
public class Solution {
    /**
     * 
     * @param head ListNode类 
     * @param m int整型 
     * @param n int整型 
     * @return ListNode类
     */
    private ListNode next = null;
    public ListNode reverseBetween (ListNode head, int m, int n) {
        ListNode newHead = new ListNode(-1);
        newHead.next = head;
        ListNode pre = newHead;
        for (int i = 0; i < m - 1; i++) {
            pre = pre.next;
        }
        ListNode virHead = reverse(pre.next, m, n);
        pre.next.next = next;
        pre.next = virHead;
        return newHead.next;
    }
    public ListNode reverse(ListNode head, int m, int n) {
        if (m == n) {
            next = head.next;
            return head;
        }
        ListNode newHead = reverse(head.next, m + 1, n);
        head.next.next = head;
        head.next = null;
        return newHead;
    }
}
```

**迭代解法**：

```java
/*
 * public class ListNode {
 *   int val;
 *   ListNode next = null;
 * }
 */

public class Solution {
    /**
     * 
     * @param head ListNode类 
     * @param m int整型 
     * @param n int整型 
     * @return ListNode类
     */
    public ListNode reverseBetween (ListNode head, int m, int n) {
        ListNode newHead = new ListNode(-1);
        newHead.next = head;
        ListNode pre = newHead;
        for (int i = 0; i < m - 1; i++) {
            pre = pre.next;
        }
        ListNode cur = pre.next;
        ListNode preHead = pre;
        while (m <= n) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
            m++;
        }
        preHead.next.next = cur;
        preHead.next = pre;
        return newHead.next;
    }
}
```



#### **BM3** **链表中的节点每k个一组翻转**

将给出的链表中的节点每 k 个一组翻转，返回翻转后的链表，如果链表中的节点数不是 k 的倍数，将最后剩下的节点保持原样
你不能更改节点中的值，只能更改节点本身。

数据范围：0≤*n*≤2000 ，1≤*k*≤2000 ，链表中每个元素都满足 0≤*v**a**l*≤1000
要求空间复杂度 *O*(1)，时间复杂度 O*(*n)

例如：

给定的链表是 1→2→3→4→5

对于 k*=2 , 你应该返回2→1→4→3→5

对于*k*=3 , 你应该返回3→2→1→4→5

```java
import java.util.*;

/*
 * public class ListNode {
 *   int val;
 *   ListNode next = null;
 * }
 */

public class Solution {
    /**
     * 
     * @param head ListNode类 
     * @param k int整型 
     * @return ListNode类
     */
    public ListNode reverseKGroup (ListNode head, int k) {
        ListNode newHead = new ListNode(-1);
        newHead.next = head;
        ListNode pre = newHead;
        ListNode cur = pre.next;
        ListNode next = pre;
        while (next != null) {
            int count = 0;
            while (count <= k) {
                if (next == null) {
                    return newHead.next;
                }
                next = next.next;
                count++;
            }
            ListNode preHead = pre;
            while (cur != next) {
                ListNode tmp = cur.next;
                cur.next = pre;
                pre = cur;
                cur = tmp;
            }
            ListNode tmp = preHead.next;
            preHead.next.next = next;
            preHead.next = pre;
            pre = tmp;
            next = pre;
        }
        return newHead.next;
    }
}
```



#### **BM4** **合并两个排序的链表**

输入两个递增的链表，单个链表的长度为n，合并这两个链表并使新链表中的节点仍然是递增排序的。

数据范围： 0≤*n*≤1000，−1000≤节点值≤1000
要求：空间复杂度 O(1)，时间复杂度 *O*(*n*)

如输入{1,3,5},{2,4,6}时，合并后的链表为{1,2,3,4,5,6}，所以对应的输出为{1,2,3,4,5,6}，转换过程如下图所示：

![img](https://uploadfiles.nowcoder.com/images/20211014/423483716_1634208575589/09DD8C2662B96CE14928333F055C5580)

或输入{-1,2,4},{1,3,4}时，合并后的链表为{-1,1,2,3,4,4}，所以对应的输出为{-1,1,2,3,4,4}，转换过程如下图所示：

![img](https://uploadfiles.nowcoder.com/images/20211014/423483716_1634208729766/8266E4BFEDA1BD42D8F9794EB4EA0A13)

示例1

输入：

```
{1,3,5},{2,4,6}
```

返回值：

```
{1,2,3,4,5,6}
```

示例2

输入：

```
{},{}
```

返回值：

```
{}
```

示例3

输入：

```
{-1,2,4},{1,3,4}
```

返回值：

```
{-1,1,2,3,4,4}
```

**题解**：

```java
/*
public class ListNode {
    int val;
    ListNode next = null;

    ListNode(int val) {
        this.val = val;
    }
}*/
public class Solution {
    public ListNode Merge(ListNode list1,ListNode list2) {
        ListNode cur1 = list1;
        ListNode cur2 = list2;
        ListNode head = new ListNode(-1);
        ListNode cur = head;
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                cur.next = cur1;
                cur1 = cur1.next;
                cur = cur.next;
                continue;
            }
            cur.next = cur2;
            cur2 = cur2.next;
            cur = cur.next;
        }
        cur.next = cur1 != null ? cur1 : cur2;
        return head.next;
    }
}
```



#### **BM5** **合并k个已排序的链表**

合并 k 个升序的链表并将结果作为一个升序的链表返回其头节点。

数据范围：节点总数 0≤*n*≤5000，每个节点的val满足∣*v**a**l*∣<=1000

要求：时间复杂度 O*(*n**l**o**g**n*)

示例1

输入：

```
[{1,2,3},{4,5,6,7}]
```

返回值：

```
{1,2,3,4,5,6,7}
```

示例2

输入：

```
[{1,2},{1,4,5},{6}]
```

返回值：

```
{1,1,2,4,5,6}
```

**优先队列**：

```java
 public ListNode mergeKLists(ArrayList<ListNode> lists) {
        if (lists == null || lists.size() == 0) {
            return null;
        }
        PriorityQueue<ListNode> queue = new PriorityQueue<>(lists.size(), Comparator.comparingInt(o -> o.val));
        for (ListNode node : lists) {
            if (node == null) continue;
            queue.offer(node);
        }
        ListNode head = new ListNode(-1);
        ListNode cur = head;
        while (!queue.isEmpty()) {
            ListNode node = queue.poll();
            cur.next = node;
            cur = cur.next;
            if (node.next != null) {
                node = node.next;
                queue.offer(node);
            }
        }
        return head.next;
        
    }
```

**归并**：

```java
public ListNode mergeKLists(ArrayList<ListNode> lists) {
        return mergeRange(lists, 0, lists.size() - 1);
    }
    public ListNode mergeRange(ArrayList<ListNode> list, int left, int right) {
        if (left == right) {
            return list.get(left);
        }
        if (left > right) {
            return null;
        }
        int mid = left + ((right - left) >> 1);
        return merge(mergeRange(list, left, mid), mergeRange(list, mid + 1, right));
    }

    public ListNode merge(ListNode head1, ListNode head2) {
        ListNode newHead = new ListNode(-1);
        ListNode cur1 = head1;
        ListNode cur2 = head2;
        ListNode cur = newHead;
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                cur.next = cur1;
                cur1 = cur1.next;
            } else {
                cur.next = cur2;
                cur2 = cur2.next;
            }
            cur = cur.next;
        }
        cur.next = cur1 != null ? cur1 : cur2;
        return newHead.next;
    }
```



#### **BM6** **判断链表中是否有环**

判断给定的链表中是否有环。如果有环则返回true，否则返回false。

数据范围：链表长度 0≤*n*≤10000，链表中任意节点的值满足∣*v**a**l*∣<=100000

要求：空间复杂度 *O*(1)，时间复杂度 *O*(*n*)

输入分为两部分，第一部分为链表，第二部分代表是否有环，然后将组成的head头结点传入到函数里面。-1代表无环，其它的数字代表有环，这些参数解释仅仅是为了方便读者自测调试。实际在编程时读入的是链表的头节点。

例如输入{3,2,0,-4},1时，对应的链表结构如下图所示：

![img](https://uploadfiles.nowcoder.com/images/20220110/423483716_1641800950920/0710DD5D9C4D4B11A8FA0C06189F9E9C)

可以看出环的入口结点为从头结点开始的第1个结点（注：头结点为第0个结点），所以输出true。

示例1

输入：

```
{3,2,0,-4},1
```

返回值：

```
true
```

说明：

```
第一部分{3,2,0,-4}代表一个链表，第二部分的1表示，-4到位置1（注：头结点为位置0），即-4->2存在一个链接，组成传入的head为一个带环的链表，返回true           
```

示例2

输入：

```
{1},-1
```

返回值：

```
false
```

说明：

```
第一部分{1}代表一个链表，-1代表无环，组成传入head为一个无环的单链表，返回false           
```

示例3

输入：

```
{-1,-7,7,-4,19,6,-9,-5,-2,-5},6
```

返回值：

```
true
```

**双指针**

```java
/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode fast = head.next;
        ListNode slow = head;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            fast = fast.next.next;
            slow = slow.next;
        }
        return true;
    }
}
```





#### **BM7** **链表中环的入口结点**

给一个长度为n链表，若其中包含环，请找出该链表的环的入口结点，否则，返回null。

数据范围： *n*≤10000，1<=结点值<=10000

要求：空间复杂度 *O*(1)，时间复杂度 *O*(*n*)

例如，输入{1,2},{3,4,5}时，对应的环形链表如下图所示：

![img](https://uploadfiles.nowcoder.com/images/20211025/423483716_1635154005498/DA92C945EF643F1143567935F20D6B46)

可以看到环的入口结点的结点值为3，所以返回结点值为3的结点。

输入描述：

输入分为2段，第一段是入环前的链表部分，第二段是链表环的部分，后台会根据第二段是否为空将这两段组装成一个无环或者有环单链表

返回值描述：

返回链表的环的入口结点即可，我们后台程序会打印这个结点对应的结点值；若没有，则返回对应编程语言的空结点即可。

示例1

输入：

```
{1,2},{3,4,5}
```

返回值：

```
3
```

说明：

```
返回环形链表入口结点，我们后台程序会打印该环形链表入口结点对应的结点值，即3   
```

示例2

输入：

```
{1},{}
```

返回值：

```
"null"
```

说明：

```
没有环，返回对应编程语言的空结点，后台程序会打印"null"   
```

示例3

输入：

```
{},{2}
```

返回值：

```
2
```

说明：

```
环的部分只有一个结点，所以返回该环形链表入口结点，后台程序打印该结点对应的结点值，即2   
```

```java
/*
 public class ListNode {
    int val;
    ListNode next = null;

    ListNode(int val) {
        this.val = val;
    }
}
*/
public class Solution {

    public ListNode EntryNodeOfLoop(ListNode pHead) {
        ListNode fast = pHead.next;
        ListNode slow = pHead;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        slow = pHead;
        fast = fast.next;
        while (fast != slow) {
            slow = slow.next;
            fast = fast.next;
        }
        return fast;
    }
}
```





#### **BM8** **链表中倒数最后k个结点**

#### **BM9** **删除链表的倒数第n个节点**

#### **BM10** **两个链表的第一个公共结点**

#### **BM11** **链表相加(二)**

#### **BM12** **单链表的排序**

#### **BM13** **判断一个链表是否为回文结构**

给定一个链表，请判断该链表是否为回文结构。

回文是指该字符串正序逆序完全一致。

数据范围： 链表节点数 0≤*n*≤105，链表中每个节点的值满足 ∣*v**a**l*∣≤10^7

示例1

输入：

```
{1}
```

返回值：

```
true
```

示例2

输入：

```
{2,1}
```

返回值：

```
false
```

说明：

```
2->1     
```

示例3

输入：

```
{1,2,2,1}
```

返回值：

```
true
```

说明：

```
1->2->2->1     
```

```java
import java.util.*;

/*
 * public class ListNode {
 *   int val;
 *   ListNode next = null;
 * }
 */

public class Solution {
    /**
     * 
     * @param head ListNode类 the head
     * @return bool布尔型
     */
    public boolean isPail (ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode rightHead = reverse(slow.next);
        ListNode left = head;
        ListNode right = rightHead;
        ListNode end = fast == null ? slow : slow.next;
        while (left != end) {
            if (left.val != right.val) {
                return false;
            } 
            left = left.next;
            right = right.next;
        }
        return true;
    }
    
    public ListNode reverse(ListNode head) {
        ListNode pre = head;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
}
```





#### **BM14** **链表的奇偶重排**

#### **BM15** **删除有序链表中重复的元素-I**

#### **BM16** **删除有序链表中重复的元素-II**





### 二分查找/排序

#### **BM17** **二分查找-I**

#### **BM18** **二维数组中的查找**

#### **BM19** **寻找峰值**

#### **BM20** **数组中的逆序对**

#### **BM21** **旋转数组的最小数字**

#### **BM22** **比较版本号**



### 二叉树

#### **BM23** **二叉树的前序遍历**

#### **BM24** **二叉树的中序遍历**

#### **BM25** **二叉树的后序遍历**

#### **BM26** **求二叉树的层序遍历**

#### **BM27** **按之字形顺序打印二叉树**

#### **BM28** **二叉树的最大深度**

#### **BM29** **二叉树中和为某一值的路径(一)**

#### **BM30** **二叉搜索树与双向链表**

#### **BM31** **对称的二叉树**

#### **BM32** **合并二叉树**

#### **BM33** **二叉树的镜像**

#### **BM34** **判断是不是二叉搜索树**

#### **BM35** **判断是不是完全二叉树**

#### **BM36** **判断是不是平衡二叉树**

#### **BM37** **二叉搜索树的最近公共祖先**

#### **BM38** **在二叉树中找到两个节点的最近公共祖先**

#### **BM39** **序列化二叉树**

#### **BM40** **重建二叉树**

#### **BM41** **输出二叉树的右视图**



### 堆/栈/队列

#### **BM42** **用两个栈实现队列**

#### **BM43** **包含min函数的栈**

#### **BM44** **有效括号序列**

#### **BM45** **滑动窗口的最大值**

#### **BM46** **最小的K个数**

#### **BM47** **寻找第K大**

#### **BM48** **数据流中的中位数**

#### **BM49** **表达式求值**



### 哈希

#### **BM50** **两数之和**

#### **BM51** **数组中出现次数超过一半的数字**

#### **BM52** **数组中只出现一次的两个数字**

#### **BM53** **缺失的第一个正整数**

#### **BM54** **三数之和**



### 递归/回溯

#### **BM55** **没有重复项数字的全排列**

#### **BM56** **有重复项数字的全排列**

#### **BM57** **岛屿数量**

#### **BM58** **字符串的排列**

#### **BM59** **N皇后问题**

#### **BM60** **括号生成**

#### **BM61** **矩阵最长递增路径**



### 动态规划

#### **BM62** **斐波那契数列**

#### **BM63** **跳台阶**

#### **BM64** **最小花费爬楼梯**

#### **BM65** **最长公共子序列(二)**

#### **BM66** **最长公共子串**

#### **BM67** **不同路径的数目(一)**

#### **BM68** **矩阵的最小路径和**

#### **BM69** **把数字翻译成字符串**

#### **BM70** **兑换零钱(一)**

#### **BM71** **最长上升子序列(一)**

#### **BM72** **连续子数组的最大和**

#### **BM73** **最长回文子串**

#### **BM74** **数字字符串转化成IP地址**

#### **BM75** **编辑距离(一)**

#### **BM76** **正则表达式匹配**

#### **BM77** **最长的括号子串**

#### **BM78** **打家劫舍(一)**

#### **BM79** **打家劫舍(二)**

#### **BM80** **买卖股票的最好时机(一)**

#### **BM81** **买卖股票的最好时机(二)**

#### **BM82** **买卖股票的最好时机(三)**



### 字符串

#### **BM83** **字符串变形**

#### **BM84** **最长公共前缀**

#### **BM85** **验证IP地址**

#### **BM86** **大数加法**



### 双指针

#### **BM87** **合并两个有序的数组**

#### **BM88** **判断是否为回文字符串**

#### **BM89** **合并区间**

#### **BM90** **最小覆盖子串**

#### **BM91** **反转字符串**

#### **BM92** **最长无重复子数组**

#### **BM93** **盛水最多的容器**

#### **BM94** **接雨水问题**



### 贪心算法

#### **BM95** **分糖果问题**

#### **BM96** **主持人调度**



### 模拟

#### **BM97** **旋转数组**

#### **BM98** **螺旋矩阵**

#### **BM99** **顺时针旋转矩阵**

#### **BM100** **设计LRU缓存结构**

#### **BM101** **设计LFU缓存结构**