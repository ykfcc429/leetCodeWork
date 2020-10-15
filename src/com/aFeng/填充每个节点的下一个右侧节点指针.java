package com.aFeng;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author ykf
 * @version 2020/10/15
 */
public class 填充每个节点的下一个右侧节点指针 {


    public Node connect(Node root) {
        if(root==null || root.left==null)
            return root;
        Node result = root;
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.empty()){
            Node node = stack.pop();
            if(node!=null && node.left!=null) {
                stack.push(node.left);
                stack.push(node.right);
                node.left.next = node.right;
                if(node.left.right!=null) {
                    node.left.right.next = node.right.left;
                    if (node.right.next != null && node.right.right.next == null)
                        node.right.right.next = node.right.next.left;
                }
            }
        }
        return result;
    }

    public Node connect_(Node root) {
        if(root==null || root.left==null)
            return root;
        Queue<Node> stack = new LinkedList<>();
        stack.offer(root);
        while (!stack.isEmpty()){
            int size = stack.size();
            for (int i = 0; i < size; i++) {
                Node node = stack.poll();
                if(i < size-1){
                    node.next = stack.peek();
                }
                if(node.left!=null){
                    stack.add(node.left);
                    stack.add(node.right);
                }
            }

        }
        return root;
    }
}
