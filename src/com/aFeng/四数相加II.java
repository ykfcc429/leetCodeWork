package com.aFeng;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ykf
 * @version 2020/11/27
 */
public class 四数相加II {

    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        int result = 0;
        Map<Integer,Integer> map = new HashMap<>();
        for (int i : A)
            for (int i1 : B)
                map.put(i + i1,map.getOrDefault(i + i1,0)+1);
        for (int i : C)
            for (int i1 : D)
                if (map.containsKey(-i - i1))
                    result += map.get(-i - i1);
        return result;
    }
}
