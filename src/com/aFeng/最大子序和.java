package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/16
 */
public class 最大子序和 {

    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int pre = nums[0];
        for (int i = 1; i < nums.length; i++) {
            pre = Math.max(nums[i],nums[i]+pre);
            max = Math.max(pre,max);
        }
        return max;
    }
}
