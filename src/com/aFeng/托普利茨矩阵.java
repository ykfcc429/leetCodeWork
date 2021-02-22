package com.aFeng;

/**
 * @author ykf
 * @version 2021/2/22
 */
public class 托普利茨矩阵 {

    public boolean isToeplitzMatrix(int[][] matrix) {
        int x = matrix.length,y = matrix[0].length;
        for(int i=0;i<x-1;i++){
            for(int j=0;j<y-1;j++){
                if(matrix[i][j]!=matrix[i+1][j+1])
                    return false;
            }
        }
        return true;
    }
}
