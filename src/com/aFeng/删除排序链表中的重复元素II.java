package com.aFeng;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ykf
 * @version 2021/3/25
 */
public class 删除排序链表中的重复元素II {

    public ListNode deleteDuplicates(ListNode head) {
        if(head==null)
            return null;

        Map<Integer,Integer> map = new HashMap<>();
        ListNode dummy = head;
        while (dummy.next!=null){
            map.put(dummy.val,map.getOrDefault(dummy.val,0)+1);
            dummy = dummy.next;
        }

        ListNode result = new ListNode(-1);
        result.next = head;
        ListNode pre = result;
        while (head.next!=null){
            if(map.get(head.val)>1){
                pre.next = head.next;
            }else {
                pre = head;
            }
            head = head.next;
        }
        return result.next;
    }
}
