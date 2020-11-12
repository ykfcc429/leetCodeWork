package com.aFeng;

/**
 * @author ykf
 * @version 2020/11/12
 */
public class 按奇偶排序数组II {

    public int[] sortArrayByParityII(int[] A) {
        for (int i = 0; i < A.length; i++)
            if(i%2!=A[i]%2)
                for (int i1 = i+1; i1 < A.length; i1++) {
                    int temp = A[i1];
                    A[i1] = A[i];
                    A[i] = temp;
                    if(i%2==A[i]%2)
                        break;
                }
        return A;
    }
}
