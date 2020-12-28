package com.aFeng;

import java.util.Arrays;

/**
 * @author ykf
 * @version 2020/12/25
 */
public class 分发饼干 {

    public int findContentChildren(int[] g, int[] s) {
        int result = 0;
        Arrays.sort(g);
        Arrays.sort(s);
        for (int i = 0; i < s.length; i++) {
            for (int i1 = 0; i1 < g.length; i1++) {
                if(g[i1]!=0 && s[i]>=g[i1]){
                    result++;
                    g[i1] = 0;
                    break;
                }
            }
        }
        return result;
    }
}
