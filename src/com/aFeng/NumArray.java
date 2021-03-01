package com.aFeng;

/**
 * @author ykf
 * @version 2021/3/1
 */
public class NumArray {

    private int[] field;

    public NumArray(int[] nums) {
        field = nums;
    }

    public int sumRange(int i, int j) {
        int result = 0;
        for (;i<=j;i++){
            result += field[i];
        }
        return result;
    }
}
