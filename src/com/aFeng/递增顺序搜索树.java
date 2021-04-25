package com.aFeng;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ykf
 * @version 2021/4/25
 */
public class 递增顺序搜索树 {

    public TreeNode increasingBST(TreeNode root) {
        TreeNode dummy = new TreeNode(-1);
        List<Integer> list = new ArrayList<>();
        order(root,list);

        TreeNode result = dummy;
        for (Integer integer : list) {
            dummy.right = new TreeNode(integer);
            dummy = dummy.right;
        }
        return result.right;
    }

    private void order(TreeNode treeNode, List<Integer> list){
        if(treeNode==null)
            return;
        order(treeNode.left, list);
        list.add(treeNode.val);
        order(treeNode.right, list);
    }
}
