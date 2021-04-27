package com.aFeng;

/**
 * @author ykf
 * @version 2021/4/27
 */
public class 二叉搜索树的范围和 {

    int result = 0;

    public int rangeSumBST(TreeNode root, int low, int high) {
        def(root,low,high);
        return result;
    }

    void def(TreeNode treeNode, int low, int high){
        if(treeNode!=null) {
            if (treeNode.val >= low && treeNode.val <= high)
                result += treeNode.val;
            def(treeNode.left, low, high);
            def(treeNode.right, low, high);
        }
    }
}
