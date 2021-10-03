package com.leetcode.problem0002_两数相加;

public class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode cur1 = l1;
        ListNode pre1 = new ListNode(0, cur1);
        ListNode cur2 = l2;
        int c = 0;
        while (cur1 != null && cur2 != null) {
            cur1.val = cur1.val + cur2.val + c;
            c = cur1.val / 10;
            cur1.val = cur1.val % 10;
            pre1 = cur1;
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        if (cur1 == null) {
            pre1.next = cur2;
            cur1 = pre1.next;
        }
        while (cur1 != null) {
            cur1.val = cur1.val + c;
            c = cur1.val / 10;
            cur1.val = cur1.val % 10;
            pre1 = cur1;
            cur1 = cur1.next;
        }
        if (c != 0) {
            pre1.next = new ListNode(c);
        }
        return l1;
    }
}


class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
