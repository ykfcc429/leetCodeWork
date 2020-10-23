package com.aFeng;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ykf
 * @version 2020/10/23
 */
public class 回文链表 {


    public boolean isPalindrome(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head!=null) {
            list.add(head.val);
            head = head.next;
        }
        int size = list.size();
        int count = size/2;

        for (int i = 0; i < count; i++)
            if(!list.get(i).equals(list.get(size-i-1)))
                return false;
        return true;
    }
}
