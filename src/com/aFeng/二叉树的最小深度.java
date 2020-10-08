
package com.aFeng;

public class 二叉树的最小深度 {

    static int minDepth = 0;

    static int result = 0;

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2,new TreeNode(4),new TreeNode(5));
        treeNode.right = new TreeNode(3);
        System.out.println(minDepth(treeNode));
    }



    static int minDepth(TreeNode root) {
        dfs(root);
        return result;
    }

    static void dfs(TreeNode treeNode){
        if(treeNode != null){
            minDepth++;
            if(treeNode.left==null&&treeNode.right==null){
                result = result==0?minDepth:Math.min(minDepth==1?result:minDepth,result);
                minDepth = 0;
                return;
            }
            dfs(treeNode.left);
            dfs(treeNode.right);
        }
    }
}
