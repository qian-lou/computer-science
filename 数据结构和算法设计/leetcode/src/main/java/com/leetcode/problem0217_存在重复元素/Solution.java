package com.leetcode.problem0217_存在重复元素;

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