package com.aFeng;

import java.util.HashSet;

/**
 * @author ykf
 * @version 2020/10/10
 */
public class 无重复字符的最长子串 {

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("au"));
    }

    public static int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();
        if(s==null||s.equals(""))
            return 0;
        if(s.length()==1)
            return 1;
        char[] chars = s.toCharArray();
        int max = 0;
        int round = 1;
        for (int index = 0; index<chars.length;++index)
            if (!set.add(chars[index])) {
                max = Integer.max(max, set.size());
                set = new HashSet<>();
                if(round==chars.length-1)
                    break;
                index=round;
                round++;
            }
        return Integer.max(max,set.size());
    }
}