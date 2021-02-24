package com.aFeng;

/**
 * @author ykf
 * @version 2021/2/24
 */
public class 翻转图像 {

    public int[][] flipAndInvertImage(int[][] A) {
        for (int i = 0; i < A.length; i++) {
            A[i] = reserve(A[i]);
        }
        return A;
    }

    public int[] reserve( int[] arr ){
        int[] arr1 = new int[arr.length];
        for( int x=0;x<arr.length;x++ ){
            arr1[x] = arr[arr.length-x-1];
            if(arr1[x]==0)
                arr1[x] = 1;
            else arr1[x] = 0;

        }
        return arr1 ;
    }
}
