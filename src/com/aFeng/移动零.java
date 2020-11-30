package com.aFeng;

/**
 * @author ykf
 * @version 2020/11/19
 */
public class 移动零 {

    public static void main(String[] args) {
        int[] nums = new int[]{0,1,0,3,12};
        moveZeroes(nums);
    }

    public static void moveZeroes(int[] nums) {
        int z = 0,c = 0;
        while (z<nums.length){
            if(nums[z]==0) {
                if (nums[c] != 0) {
                    int temp = nums[c];
                    nums[c] = nums[z];
                    nums[z] = temp;
                    z++;
                } else {
                    if(c<nums.length-1)
                        c++;
                    else
                        z++;
                }
            }else {
                z++;
            }
        }
    }
}