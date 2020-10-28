package com.aFeng;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ykf
 * @version 2020/10/28
 */
public class 独一无二的出现次数 {

    public boolean uniqueOccurrences(int[] arr) {
        Arrays.sort(arr);
        Set<Integer> set = new HashSet<>();
        int num = 1;
        for (int i = 1; i < arr.length; i++) {
            int size = set.size();
            if(arr[i]!=arr[i-1]){
                if(arr.length==2)
                    return false;
                set.add(num);
                if(set.size() == size && size!=0)
                    return false;
                num = 1;
            }else
                num++;
        }
        return true;
    }
}
