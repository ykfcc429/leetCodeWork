package com.aFeng;

public class 两数相加 {

    public static void main(String[] args) {
        addTwoNumbers(new ListNode(0),new ListNode(0));
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int num = 0;
        int remainder;
        ListNode result = null;
        ListNode root = null;
        ListNode dummy = new ListNode(0);
        while (true) {
            int sum = l1.val + l2.val + num;
            num = sum/10;
            remainder = sum%10;
            if(result==null) {
                result = new ListNode(remainder);
                root = result;
            }else {
                root.next = new ListNode(remainder);
                root = root.next;
            }
            if (l1.next == null && l2.next == null && num == 0)
                break;
            if(l1.next!=null)
                l1 = l1.next;
            else
                l1 = dummy;
            if(l2.next!=null)
                l2 = l2.next;
            else
                l2 = dummy;
        }
        return result;
    }
}
