package com.leetcode.problem0013_罗马数字转整数;

class Solution {
    public int romanToInt(String s) {
        int sum = 0, i = 0, a = 0, b = 0, len = s.length();
        while (i < len) {
            a = number(s.charAt(i));
            if (i == len - 1) {
                sum += a;
            } else {
                b = number(s.charAt(i + 1));
                sum += a >= b ? a : -a;
            }
            i++;
        }
        return sum;
    }

    public int number(char c) {
        switch (c) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: return 0;
        }
    }
}