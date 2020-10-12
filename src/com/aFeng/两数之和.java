package com.aFeng;

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

    static int[] method(int[] nums,int target){
        for(int index=0;index<nums.length-2;++index){
            for(int j=index+1;j<nums.length-1;++j){
                if(nums[index]+nums[j]==target)
                    return new int[]{index,j};
            }
        }
        return new int[]{};
    }
}
