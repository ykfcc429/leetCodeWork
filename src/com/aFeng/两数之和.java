package com.aFeng;

import com.sun.xml.internal.ws.spi.db.RepeatedElementBridge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * 给定一个整数数组和一个目标值target,请你在该数组中找出和为目标值的那两个整数
 * 并返回他们的数组下标.
 *
 * 你可以假设每种输入只会对应一个答案,但是,数组中同一个元素不能使用两遍
 * @author ykfcc
 */
public class 两数之和 {

    public static void main(String[] args) {
        int[] nums = {3,6,1,11,9,2};
        int target = 3 ;
        int[] result = method(nums, target);
        for (int i : result) {
            System.out.println(i+"\t"+nums[i]);
        }
    }

    /**
     * 感觉不是特别好的解决方法,
     * 因为想通过数组排序后双指针的方法,但是排序就会丢失下标,所以必须要一个新建一个list对象来专门存储下标,这里是一大败笔
     * 接下来的解题思路就非常清晰了,两个指针分别指向排序后数组的头尾,如果两数之和小于target那么就把左边的指针右移,反之移动
     * 右边的指针,这样的空间复杂度就是O(2);因为我遍历了两次.........
     */
    static int[] method(int[] nums,int target){
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        Arrays.sort(nums);
        int l = 0;
        int r = nums.length-1;
        while (l<r){
            int num = nums[l]+nums[r];
            if(num == target){
                return new int[]{list.indexOf(nums[l]),list.indexOf(nums[r])};
            }else if(num>target){
                r--;
            }else {
                l++;
            }
        }
        return new int[]{0,0};
    }
}
