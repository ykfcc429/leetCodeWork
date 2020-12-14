package com.aFeng;

import java.util.*;

/**
 * @author ykf
 * @version 2020/12/14
 */
public class 字母异位词分组 {

    public static void main(String[] args) {
        String[] strings = {"eat", "tea", "tan", "ate", "nat", "bat"};
        groupAnagrams(strings);
    }

    private static List<List<String>> groupAnagrams(String[] strs) {
        Map<String,List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String string = new String(chars);
            List<String> orDefault = map.getOrDefault(string, new ArrayList<>());
            orDefault.add(str);
            map.put(string,orDefault);
        }
        return new ArrayList<>(map.values());
    }
}
