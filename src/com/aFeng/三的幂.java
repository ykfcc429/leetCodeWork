package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/10
 */
public class 三的幂 {

    public static void main(String[] args) {
        System.out.println(isPowerOfThree(0));
    }


    public static boolean isPowerOfThree(int n) {
        if(n==1)
            return true;
        if(n<3)
            return false;
        while (n>1){
            if(n%3==0)
                n = n/3;
            else return false;
        }
        return n==1;
    }
}
