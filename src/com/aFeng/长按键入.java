package com.aFeng;

/**
 * @author ykf
 * @version 2020/10/21
 */
public class 长按键入 {

    public boolean isLongPressedName(String name, String typed) {
        if(typed.length()<name.length())
            return false;
        int nIndex = 0,tIndex = 0;
        while (tIndex < typed.length())
            if(nIndex<name.length()-1 && name.charAt(nIndex)==typed.charAt(tIndex)) {
                tIndex++;
                nIndex++;
            }
            else if(tIndex>0 && typed.charAt(tIndex)==name.charAt(nIndex-1))
                tIndex++;
            else
                return false;
        return nIndex==name.length();
    }
}
