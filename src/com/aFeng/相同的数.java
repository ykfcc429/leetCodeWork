package com.aFeng;

/**
 * 给定两个二叉树，编写一个函数来检验它们是否相同。
 *
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 *
 * 示例 1:
 *
 * 输入:       1         1
 *           / \       / \
 *          2   3     2   3
 *
 *         [1,2,3],   [1,2,3]
 *
 * 输出: true
 * 示例 2:
 *
 * 输入:      1          1
 *           /           \
 *          2             2
 *
 *         [1,2],     [1,null,2]
 *
 * 输出: false
 * 示例 3:
 *
 * 输入:       1         1
 *           / \       / \
 *          2   1     1   2
 *
 *         [1,2,1],   [1,1,2]
 *
 * 输出: false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/same-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class 相同的数 {

    static boolean flag = true;

    public static void main(String[] args) {

    }

    static boolean isSameTree(TreeNode p, TreeNode q){
        equals(p,q);
        return flag;
    }

    /*
    太简单,不解释
     */
    static void equals(TreeNode p, TreeNode q){
        if(p==null&&q==null){
            return;
        }
        if(p == null || q == null || p.val!=q.val){
            flag = false;
        }else{
            equals(p.left,q.left);
            equals(p.right,q.right);
        }
    }

}


