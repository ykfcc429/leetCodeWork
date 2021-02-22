package com.aFeng;

/**
 * @author ykf
 * @version 2021/2/7
 */
public class 非递减数列 {

    public boolean checkPossibility(int[] nums) {
        int count = 0;
        for (int i = 1; i < nums.length; i++) {
            if(nums[i]<nums[i-1])
                count++;
            if(count>1)
                return false;
        }
        return true;
    }
}
