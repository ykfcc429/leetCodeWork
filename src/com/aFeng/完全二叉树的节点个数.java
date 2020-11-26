package com.aFeng;

/**
 * @author ykf
 * @version 2020/11/24
 */
public class 完全二叉树的节点个数 {

    int count = 0;

    public int countNodes(TreeNode root) {
        sdf(root);
        return count;
    }

    void sdf(TreeNode treeNode){
        if(treeNode!=null) {
            count++;
            sdf(treeNode.left);
            sdf(treeNode.right);
        }
    }
}
