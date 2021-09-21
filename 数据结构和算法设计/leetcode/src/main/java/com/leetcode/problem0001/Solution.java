package com.leetcode.problem0001;

import java.util.HashMap;
import java.util.Map;

public class Solution {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> index = new HashMap<>();
        int[] result = new int[2];
        int len = nums.length;
        Integer temp;
        for (int i = 0; i < len; i++) {
            temp = index.get(target - nums[i]);
            if ( temp != null) {
                result[0] = i;
                result[1] = temp;
                return result;
            }
            index.put(target - nums[i], i);
        }
        return result;
    }
}
