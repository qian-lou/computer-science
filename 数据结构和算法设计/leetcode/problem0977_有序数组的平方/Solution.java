class Solution {
    public int[] sortedSquares(int[] nums) {
        int len = nums.length;
        int left = 0;
        for (int i = 0; i < len; i++) {
            if(nums[i] < 0) {
                left = i;
            } else {
                break;
            }
        }
        int[] res = new int[len];
        int right = left + 1;
        int idx = 0;
        while (left >= 0 && right < len) {
            if (nums[left] + nums[right] > 0) {
                    res[idx++] = nums[left] * nums[left];
                    left--;
                } else {
                    res[idx++] = nums[right] * nums[right];
                    right++;
                }
        }
        if (left < 0) {
            while (right < len) {
                res[idx++] = nums[right] * nums[right];
                right++;
            }
        }
        if (right == len) {
            while(left >= 0) {
                res[idx++] = nums[left] * nums[left];
                left--;
            }
        }
        return res;
    }
}