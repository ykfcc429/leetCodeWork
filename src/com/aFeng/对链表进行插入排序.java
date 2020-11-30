package com.aFeng;

/**
 * @author ykf
 * @version 2020/11/20
 */
public class 对链表进行插入排序 {

    public ListNode insertionSortList(ListNode head) {
        ListNode result = head;
        while (true) {

            if(head.val==3)
                break;
        }
        return result;
    }

    private void rec(ListNode head){
        if (head != null && head.next != null) {
            if (head.next.val < head.val) {
                head = head.next;
            }else {

            }
        }
    }
}
