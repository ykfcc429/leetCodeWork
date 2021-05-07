package com.aFeng;

/**
 * @author ykf
 * @version 2021/5/7
 */
public class 数组异或操作 {

    public int xorOperation(int n, int start) {
        int tmp = start;
        for (int i = 1; i < n; i++) {
            tmp ^= start + 2*i;
        }
        return tmp;
    }
}
