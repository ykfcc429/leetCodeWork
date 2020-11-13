package com.aFeng;

/**
 * @author ykf
 * @version 2020/11/13
 */
public class 奇偶链表 {

    public ListNode oddEvenList(ListNode head) {
        if(head==null)
            return null;
        ListNode dummy = new ListNode(0);
        ListNode dummyHead = null;
        ListNode head_ = head;
        while (true) {
            if (head.next != null) {
                dummy.next = head.next;
                if (dummyHead == null)
                    dummyHead = dummy.next;
                dummy = dummy.next;
            }else
                break;
            if(dummy.next!=null){
                head.next = dummy.next;
                dummy.next = null;
                head = head.next;
            }else
                break;
        }
        head.next = dummyHead;
        return head_;
    }
}
