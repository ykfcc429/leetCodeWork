package com.aFeng;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ykf
 * @version 2021/1/5
 */
public class 较大分组的位置 {

    public List<List<Integer>> largeGroupPositions(String s) {
        List<List<Integer>> result = new ArrayList<>();
        if(s.length()<3)
            return new ArrayList<>();
        int count = 1;
        for (int i = 1; i < s.length(); i++) {
            if(s.charAt(i)==s.charAt(i-1))
                count++;
            else {
                if(count>2){
                    List<Integer> list = new ArrayList<>();
                    list.add(i-count);
                    list.add(i-1);
                    result.add(list);
                }
                count = 1;
            }
        }
        if(count>2){
            List<Integer> list = new ArrayList<>();
            list.add(s.length()-count);
            list.add(s.length()-1);
            result.add(list);
        }
        return result;
    }
}
