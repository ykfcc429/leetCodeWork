package com.aFeng;

/**
 * @author ykf
 * @version 2021/3/26
 */
public class 删除排序链表中的重复元素 {

    public ListNode deleteDuplicates(ListNode head) {
        if(head==null)
            return null;
        ListNode result = head;
        ListNode next = head.next;
        while (next!=null){
            if(head.val==next.val)
                head.next = next.next;
            else {
                head = next;
            }
            next = next.next;
        }
        return result;
    }
}
