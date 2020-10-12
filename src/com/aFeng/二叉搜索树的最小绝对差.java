package com.aFeng;

/**
 * @author ykf
 * @version 2020/10/12
 */
public class 二叉搜索树的最小绝对差 {

    int pre = 0;
    int result = Integer.MAX_VALUE;

    public int getMinimumDifference(TreeNode root) {
        dfs(root);
        return result;
    }

    void dfs(TreeNode root){
        if (root==null)
            return;
        if(root.left!=null)
            dfs(root.left);
        if(result==-1)
            pre = root.val;
        else
            result = Integer.min(result,root.val-pre);
        if (root.right!=null)
            dfs(root.right);
    }
}
