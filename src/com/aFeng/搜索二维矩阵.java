package com.aFeng;

/**
 * @author ykf
 * @version 2021/3/30
 */
public class 搜索二维矩阵 {
    public boolean searchMatrix(int[][] matrix, int target) {
        boolean flag = false;
        for (int[] ints : matrix) {
            for (int i = 0; i < ints.length; i++) {
                if(ints[i]==target)
                    flag = true;
            }
        }
        return flag;
    }
}
