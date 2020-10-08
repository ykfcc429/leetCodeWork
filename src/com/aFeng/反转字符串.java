package com.aFeng;

public class 反转字符串 {

    public static void main(String[] args) {
        char[] s = {'h','e','l','l','o'};
        reverseString(s);
        System.out.println(s);
    }

    public static void reverseString(char[] s) {
        int length = s.length;
        if (length == 0)
            return;
        int count = length%2==0?length/2:(length-1)/2;
        for (int index=0;index<count;++index){
            char temp = s[index];
            s[index] = s[length-1-index];
            s[length-1-index] = temp;
        }
    }
}
