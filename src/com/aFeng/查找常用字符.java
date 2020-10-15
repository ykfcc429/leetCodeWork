package com.aFeng;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ykf
 * @version 2020/10/14
 */
public class 查找常用字符 {

    public static void main(String[] args) {
        String[] list = new String[]{"cool","lock","cook"};
        for (String s : commonChars(list)) {
            System.out.println(s);
        }
    }

    public static List<String> commonChars(String[] A) {
        if(A.length<2)
            return new ArrayList<String>();
        char[] a = A[0].toCharArray();
        boolean flag = false;
        List<String> result = new ArrayList<>();
        for (char c : a) {
            for (int i = 1; i < A.length; i++)
                if(!(flag = A[i].contains(c + "")))
                    break;
            if(flag){
                result.add(c+"");
                for (int i = 0; i < A.length; i++) {
                    A[i] = A[i].replaceFirst(c+"","");
                }
            }
        }
        return result;
    }
}
