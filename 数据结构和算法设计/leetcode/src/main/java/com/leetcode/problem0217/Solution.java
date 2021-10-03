package com.leetcode.problem0217;

import java.util.HashSet;
import java.util.Set;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> d = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (!d.add(nums[i])) {
                return true;
            }
        }
        return false;
    }
}