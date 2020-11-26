package com.aFeng;

import java.util.Arrays;

/**
 * @author ykf
 * @version 2020/11/26
 */
public class 最大间距 {

    public int maximumGap(int[] nums) {
        int result = 0;
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++)
            result = Math.max(result,nums[i]-nums[i-1]);
        return result;
    }
}
