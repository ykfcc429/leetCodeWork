package com.aFeng;

import java.util.HashSet;

/**
 * @author ykf
 * @version 2020/10/9
 */
public class 环形链表 {

    public boolean hasCycle(ListNode head) {
        HashSet<ListNode> hashSet = new HashSet<>();
        hashSet.add(head);
        while (head != null && (head = head.next)!=null)
            if(!hashSet.add(head))
                return true;
        return false;
    }
}