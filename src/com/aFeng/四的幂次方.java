package com.aFeng;

/**
 * @author ykf
 * @version 2020/10/16
 */
public class 四的幂次方 {

    public boolean isPowerOfFour(int num) {
        while (num>1)
            if(num%4==0)
                num = num>>2;
            else return false;
        return num == 1;
    }
}