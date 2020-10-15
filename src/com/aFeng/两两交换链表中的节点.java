package com.aFeng;

/**
 * @author ykf
 * @version 2020/10/13
 */
public class 两两交换链表中的节点 {

    public static void main(String[] args) {
        ListNode head = new ListNode(1,new ListNode(2,new ListNode(3,new ListNode(4,null))));
        swapPairs(head);
    }

    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode newHead = head.next;
        head.next = swapPairs(newHead.next);
        newHead.next = head;
        return newHead;
    }
}
