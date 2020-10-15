
package com.aFeng;

import redis.clients.jedis.Jedis;

import java.util.*;


/**
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Map<Integer,String > set = new TreeMap<>();
        set.put(2,"2");
        set.put(7,"7");
        set.put(1,"1123");
        set.put(5,"5");
        set.put(3,"3");
        Iterator<Integer> iterator = set.keySet().iterator();
        while (iterator.hasNext())
            System.out.println(set.get(iterator.next()));
    }

    public List<Double> averageOfLevels(TreeNode root) {

        return null;
    }

    int[][] a = new int[][]{};

    void handle(TreeNode treeNode,int floor){
        if(treeNode==null){
            return;
        }
        if(a[floor].length>0){
            a[floor][a[floor].length-1] = treeNode.val;
        }else{
            a[floor][a[floor].length] = treeNode.val;
        }
        handle(treeNode.left,floor++);
    }

}
