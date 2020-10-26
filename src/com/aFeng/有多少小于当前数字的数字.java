package com.aFeng;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author ykf
 * @version 2020/10/26
 */
public class 有多少小于当前数字的数字 {

    public int[] smallerNumbersThanCurrent(int[] nums) {
        int[] result = Arrays.copyOf(nums,nums.length);
        Arrays.sort(nums);
        HashMap<Integer,Integer> map = new HashMap<>();
        map.put(nums[0],0);
        for (int i = 1; i < nums.length; i++)
            if(nums[i]>nums[i-1])
                map.put(nums[i],i);
        for (int i = 0; i < result.length; i++)
            result[i] = map.get(result[i]);
        return result;
    }
}
