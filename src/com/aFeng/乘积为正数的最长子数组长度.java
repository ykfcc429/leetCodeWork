package com.aFeng;

/**
 * @author ykf
 * @version 2020/10/16
 */
public class 乘积为正数的最长子数组长度 {

    public int getMaxLen(int[] nums) {
        if(nums.length<1)
            return 0;
        int pos = nums[0]>0?1:0;
        int nat = nums[0]<0?1:0;
        int maxLenth = pos;
        for (int i = 1; i < nums.length; i++) {

            if(nums[i]>0){
                pos++;
                nat = nat>0?nat+1:nat;
            }
            if(nums[i]<0){
                int temp = pos;
                pos = nat>0?nat+1:0;
                nat = temp+1;
            }
            if (nums[i]==0) {
                pos = 0;
                nat = 0;
            }
            maxLenth = Math.max(maxLenth,pos);
        }
        return maxLenth;
    }
}
