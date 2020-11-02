package com.aFeng;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author ykf
 * @version 2020/11/2
 */
public class 两个数组的交集 {

    public static void main(String[] args) {
        int[] a = new int[]{4,9,5},
                b = new int[]{9,4,9,8,4};
        intersection(a,b);
    }

    public static int[] intersection(int[] nums1, int[] nums2) {
        if(nums1.length<1 || nums2.length<1)
            return new int[]{};
        int[] result = new int[Math.min(nums1.length,nums2.length)];
        int count = 0;
        HashSet<Integer> set = new HashSet<>();
        for (int i : nums1) {
            set.add(i);
        }
        for (int i : nums2) {
            if(set.size()>0&&set.contains(i)){
                result[count++] = i;
                set.remove(i);
            }
        }
        result = Arrays.copyOf(result,count);
        return result;
    }
}
