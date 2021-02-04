package com.aFeng;

/**
 * @author ykf
 * @version 2021/2/4
 */
public class 子数组最大平均数I {

    public static void main(String[] args) {
        findMaxAverage(new int[]{1,12,-5,-6,50,3},4);
    }

    public static double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        int max = sum;
        for (int i = k; i < nums.length; i++) {
            sum = sum+nums[i]-nums[i-k];
            max = Math.max(sum,max);
        }
        return (double)max/(double)k;
    }
}
