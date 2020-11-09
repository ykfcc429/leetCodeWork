package com.aFeng;

import java.util.Arrays;

/**
 * @author ykf
 * @version 2020/11/9
 */
public class 最接近原点的K个点 {

    public int[][] kClosest(int[][] points, int K) {
        Arrays.sort(points, (o1,o2)-> o1[0]*o1[0]+o1[1]*o1[1]-o2[0]*o2[0]+o2[1]*o2[1]);
        return Arrays.copyOf(points,K);
    }


}
