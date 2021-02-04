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
        double result = 0;
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        result = (double)sum/(double)k;
        for (int i = k; i < nums.length; i++) {
            sum = sum+nums[i]-nums[i-k];
            result = Double.max(result,(double)sum/(double)k);
        }
        return result;
    }
}
