package com.aFeng;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author ykf
 * @version 2020/10/23
 */
public class 每日温度 {
    public int[] dailyTemperatures(int[] T) {
        Stack<Integer> stack = new Stack<>();
        int[] result = new int[T.length];
        stack.push(0);
        for (int i = 1 ; i < T.length;i++) {
            while (!stack.isEmpty() && T[i] > T[stack.peek()]) {
                int pop = stack.pop();
                result[pop] = i - pop;
            }
            stack.push(i);
        }
        return result;
    }
}
