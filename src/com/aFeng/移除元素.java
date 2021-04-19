package com.aFeng;

/**
 * @author yankaifeng
 * 创建日期 2021/4/19
 */
public class 移除元素 {
    public int removeElement(int[] nums, int val) {
        int result = 0;
        int left = 0, right = nums.length-1;
        while (left<right) {
            if (nums[right]==val){
                right--;
                continue;
            }
            if(nums[left]==val){
                int temp = nums[right];
                nums[right] = nums[left];
                nums[left] = temp;
            }
            left++;
        }
        for (int num : nums) {
            if(num!=val)
                result++;
            else break;
        }
        return result;
    }
}
