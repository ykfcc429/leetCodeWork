package com.aFeng;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author ykf
 * @version 2020/10/27
 */
public class 二叉树的前序遍历 {

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.empty()){
            TreeNode treeNode = stack.pop();
            if(treeNode!=null) {
                if (treeNode.right != null)
                    stack.push(treeNode.right);
                if (treeNode.left != null)
                    stack.push(treeNode.left);
                ans.add(treeNode.val);
            }
        }
        return ans;
    }
}
