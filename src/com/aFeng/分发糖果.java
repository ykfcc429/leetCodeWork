package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/24
 */
public class 分发糖果 {

    public static void main(String[] args) {
        int[] ints = {1,6,10,8,7,3,2};
        System.out.println(candy(ints));
    }

    public static int candy(int[] ratings) {
        int result = 0;
        int[] candy = new int[ratings.length];
        candy[0] = 1;
        for (int i = 0,j = i + 1; i < ratings.length -1; i++,++j) {
            if(candy[j]==0)
                candy[j] = 1;
            if(ratings[i]>ratings[j] && candy[i]<=candy[j]) {
                candy[i]++;
                if(i>0){
                    i-=2;
                    j-=2;
                }
                continue;
            }
            if(ratings[i]<ratings[j] && candy[i]>=candy[j]){
                candy[j] = candy[i]+1;
            }
        }
        for (int i = 0; i < candy.length; i++) {
            result += candy[i];
        }
        return result;
    }
}
