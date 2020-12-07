package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/7
 */
public class 翻转矩阵后的得分 {

    public int matrixScore(int[][] A) {
        int result = 0;
        for (int[] ints : A) {
            if(ints[0]==0) {
                ints[0] = 1;
                for (int i = 1; i < ints.length; i++) {
                    ints[i] = ints[i]==0?1:0;
                }
            }
        }
        if(A[0].length>1)
            for (int i = 1; i < A[0].length; i++) {
                int num = 0;
                for (int i1 = 0; i1 < A.length; i1++) {
                    if(A[i1][i]==1)
                        num--;
                    else
                        num++;
                }
                if(num>0){
                    for (int i1 = 0; i1 < A.length; i1++) {
                        A[i1][i] = A[i1][i]==0?1:0;
                    }
                }
            }
        for (int[] ints : A) {
            StringBuilder str = new StringBuilder();
            for (int anInt : ints) {
                str.append(anInt);
            }
            result += Integer.parseInt(str.toString(),2);
        }
        return result;
    }
}
