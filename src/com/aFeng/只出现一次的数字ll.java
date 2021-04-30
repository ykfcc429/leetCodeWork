package com.aFeng;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ykf
 * @version 2021/4/30
 */
public class 只出现一次的数字ll {

    public int singleNumber(int[] nums) {
        int result = 0;
        Map<Integer,Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num,map.getOrDefault(num,0)+1);
        }
        for (Map.Entry<Integer, Integer> integerIntegerEntry : map.entrySet()) {
            if(integerIntegerEntry.getValue()==1) {
                result = integerIntegerEntry.getKey();
                break;
            }
        }
        return result;
    }
}
