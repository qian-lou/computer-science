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





### 二分查找/排序

### 二叉树

### 堆/栈/队列

### 哈希

### 递归/回溯

### 动态规划

### 字符串

### 双指针

### 贪心算法

### 模拟