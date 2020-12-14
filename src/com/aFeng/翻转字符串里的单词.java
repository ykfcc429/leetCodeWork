package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/9
 */
public class 翻转字符串里的单词 {

    public static void main(String[] args) {
        reverseWords("a good   example");
    }

    public static String reverseWords(String s) {
        if(s.contains(" ")) {
            StringBuilder result = new StringBuilder();
            String[] strings = s.trim().split(" ");
            for (int i = strings.length - 1; i >= 0; i--) {
                if(!strings[i].equals(""))
                    if(i!=0)
                        result.append(strings[i].trim()).append(" ");
                    else result.append(strings[i].trim());
            }
            return result.toString();
        }
        return s;
    }
}
