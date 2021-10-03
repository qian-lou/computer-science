package com.leetcode.problem0007_整数反转;

class Solution {
    public int reverse(int x) {
        long sum = 0;
        while (x != 0) {
            sum = sum * 10 + (x % 10);
            x /= 10;
        }
        return sum > Integer.MAX_VALUE || sum < Integer.MIN_VALUE ? 0 : (int)sum;
    }
}