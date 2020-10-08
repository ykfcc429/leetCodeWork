package com.aFeng;

import org.jsoup.internal.StringUtil;

public class 宝石与石头 {

    public static void main(String[] args) {
        String J = "aA";
        String S = "aAAbbbb";
        System.out.println(numJewelsInStones(J, S));
    }

    public static int numJewelsInStones(String J, String S) {
        if(isBlank(J) || isBlank(S))
            return 0;
        char[] j = J.toCharArray();
        char[] s = S.toCharArray();
        int count = 0;
        for (char c : j) {
            for (char c1 : s) {
                if(c1 == c)
                    ++count;
            }
        }
        return count;
    }

    static boolean isBlank(String str){
        return str == null || str.trim().equals("");
    }
}
