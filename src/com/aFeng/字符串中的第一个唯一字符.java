package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/23
 */
public class 字符串中的第一个唯一字符 {

    public int firstUniqChar(String s) {
        int result = -1;
        char[] strs = s.toCharArray();
        for (int i = 0; i < strs.length; i++) {
            String a = strs[i]+"";
            if(s.indexOf(a)==s.lastIndexOf(a))
                return i;
        }
        return result;
    }
}
