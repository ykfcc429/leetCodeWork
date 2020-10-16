package com.aFeng;

import java.util.Arrays;

/**
 * @author ykf
 * @version 2020/10/16
 */
public class 有序数组的平方 {


    public int[] sortedSquares(int[] A) {
        int[] result = new int[A.length];
        for (int i = 0; i < A.length; i++) {
            double a = A[i];
            result[i] = (int)Math.pow(a,2.0);
        }
        Arrays.sort(result);
        return result;
    }
}
