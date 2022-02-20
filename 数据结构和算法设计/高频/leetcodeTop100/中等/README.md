#### 两数相加

给你两个 **非空** 的链表，表示两个非负的整数。它们每位数字都是按照 **逆序** 的方式存储的，并且每个节点只能存储 **一位** 数字。

请你将两个数相加，并以相同形式返回一个表示和的链表。

你可以假设除了数字 0 之外，这两个数都不会以 0 开头。

**示例 1：**

![img](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2021/01/02/addtwonumber1.jpg)

```
输入：l1 = [2,4,3], l2 = [5,6,4]
输出：[7,0,8]
解释：342 + 465 = 807.
```

**示例 2：**

```
输入：l1 = [0], l2 = [0]
输出：[0]
```

**示例 3：**

```
输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
输出：[8,9,9,9,0,0,0,1]
```

 

**提示：**

- 每个链表中的节点数在范围 `[1, 100]` 内
- `0 <= Node.val <= 9`
- 题目数据保证列表表示的数字不含前导零

---

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int c = 0;
        ListNode head = new ListNode(-1);
        ListNode cur1 = l1;
        ListNode cur2 = l2;
        ListNode cur = head;
        while (cur1 != null || cur2 != null) {
            int sum = c;
            if (cur1 != null) {
                sum += cur1.val;
                cur1 = cur1.next;
            }
            if (cur2 != null) {
                sum += cur2.val;
                cur2 = cur2.next;
            }
            c = sum / 10;
            cur.next = new ListNode(sum % 10);
            cur = cur.next;
        }
        if (c != 0) {
            cur.next = new ListNode(c % 10);
        }
        return head.next;
    }
}
```

---

#### 无重复字符的最长子串

#### 最长回文子串

#### 盛最多水的容器

#### 三数之和

#### 电话号码的字母组合

#### 删除链表的倒数第N个结点

#### 括号生成

#### 下一个排列

#### 搜索旋转排序数组

#### 在排序数组中查找元素的第

#### 组合总和

#### 全排列

#### 旋转图像

#### 字母异位词分组

#### 合并区间

#### 不同路径

#### 最小路径和

#### 颜色分类

#### 子集

#### 单词搜索

#### 不同的二叉搜索树

#### 二叉树的层序遍历

#### 从前序与中序遍历序列构i

#### 二叉树展开为链表

#### 最长连续序列

#### 单词拆分

#### 环形链表Ⅱ

#### LRU缓存

#### 排序链表

#### 乘积最大子数组

#### 打家劫舍

#### 岛屿数量

#### 课程表

#### 实现Trie（前树）

#### 数组中的第K个最大元素

#### 最大正方形

#### 二叉树的最近公共祖先

#### 除自身以外数组的乘积

#### 搜索二维矩阵Ⅱ

#### 完全平方数

#### 寻找重复数

#### 最长递增子序列

#### 最佳买卖股票时机含冷冻期

#### 零钱兑换

#### 打家劫舍Ⅲ

#### 前K个高频元素

#### 字符串解码

#### 除法求值

#### 根据身高重建队列

#### 分割等和子集

#### 路径总和Ⅲ

#### 找到字符串中所有字母异位

#### 目标和

#### 把二叉搜索树转换为累加树

#### 和为K的子数组

#### 最短无序连续子数组

#### 任务调度器

#### 回文子串

#### 每日温度

