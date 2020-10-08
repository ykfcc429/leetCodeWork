
package com.aFeng;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
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
