package com.aFeng;

import java.util.HashMap;
import java.util.Stack;

/**
 * @author ykf
 * @version 2020/10/23
 */
public class 下一个更大元素I {


    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Stack<Integer> stack = new Stack<>();
        HashMap<Integer,Integer> map = new HashMap<>();
        for (int i : nums2) {
            while (!stack.empty() && i>stack.peek())
                map.put(stack.pop(),i);
            stack.push(i);
        }
        for (int i = 0; i < nums1.length; i++)
            nums1[i] = map.getOrDefault(nums1[i],-1);
        return nums1;
    }
}
