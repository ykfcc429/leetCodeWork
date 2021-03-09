package com.aFeng;

/**
 * @author ykf
 * @version 2021/3/9
 */
public class 删除字符串中的所有相邻重复项 {

    public String removeDuplicates(String S) {
        for (int i = 0; i < S.length() - 1; i++) {
            if (S.charAt(i) == S.charAt(i + 1)) {
                S = S.replaceAll(S.substring(i, i + 2), "");
                i = -1;
            }
        }
        return S;
    }
}
