package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/15
 */
public class 单调递增的数字 {

    public int monotoneIncreasingDigits(int N) {
        char[] str = String.valueOf(N).toCharArray();
        int i = 1;
        while (i < str.length && str[i-1]<=str[i])
            i++;
        if(i < str.length){
            while (i > 0 && str[i-1]>str[i]) {
                str[i - 1]--;
                i--;
            }
            i++;
            for(;i < str.length;i++)
                str[i] = '9';
        }
        return Integer.parseInt(new String(str));
    }
}
