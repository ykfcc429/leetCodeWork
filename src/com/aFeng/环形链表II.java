package com.aFeng;

/**
 * @author ykf
 * @version 2020/10/10
 */
public class 环形链表II {
    public ListNode detectCycle(ListNode head) {
        HashSet<ListNode> hashSet = new HashSet<>();
        hashSet.add(head);
        while (head != null && (head = head.next)!=null)
            if(!hashSet.add(head))
                return head;
        return null;
    }
}
