package com.aFeng;

/**
 * @author ykf
 * @version 2020/10/29
 */
public class 求根到叶子节点数字之和 {

    int result=0;
    int num;
    public int sumNumbers(TreeNode root) {
        if(root!=null) {
            num = root.val;
            dfs(root);
        }
        return result;
    }

    void dfs(TreeNode treeNode){
        if(treeNode.left!=null) {
            num = num * 10 + treeNode.left.val;
            dfs(treeNode.left);
        }
        if(treeNode.right!=null) {
            num = num * 10 + treeNode.right.val;
            dfs(treeNode.right);
        }else if(treeNode.left==null){
            result += num;
        }
        num/=10;
    }
}