package com.leetcode.problem0070_爬楼梯;

class Solution {
    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        } 
        int two = 1;
        int one = 2;
        int sum = 0;
        for (int i = 3; i <= n; i++) {
            sum = two + one;
            two = one;
            one = sum;
        }
        return one;
    }
}