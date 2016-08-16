package com.yuzhouwan.hacker.algorithms.leetcode.tree;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Invert Binary Tree Solution
 *
 * @author Benedict Jin
 * @since 2016/8/16
 */
class InvertBinaryTreeSolution {

    /**
     * https://leetcode.com/problems/invert-binary-tree/
     *
     *Invert a binary tree.
     *
     *      4
     *    /   \
     *   2     7
     *  / \   / \
     * 1   3 6   9
     *
     * to
     *      4
     *    /   \
     *   7     2
     *  / \   / \
     * 9   6 3   1
     *
     * Google: 90% of our engineers use the software you wrote (Homebrew), but you can’t invert a binary tree on a whiteboard so fuck off.
     *
     */
    /* 递归 */
    static TreeNode invertTreeRecursion(TreeNode root) {
        if (root == null) return null;
        TreeNode left = root.left,
                right = root.right;
        root.left = invertTreeRecursion(right);
        root.right = invertTreeRecursion(left);
        return root;
    }

    /* 非递归 深度优先 */
    static TreeNode invertTreeDFS(TreeNode root) {

        if (root == null) return null;

        final Deque<TreeNode> stack = new LinkedList<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            final TreeNode node = stack.pop();
            final TreeNode left = node.left;
            node.left = node.right;
            node.right = left;

            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }
        return root;
    }

    /* 非递归 广度优先 */
    static TreeNode invertTreeBFS(TreeNode root) {

        if (root == null) return null;

        final Deque<TreeNode> stack = new LinkedList<>();
        stack.offer(root);

        while (!stack.isEmpty()) {
            final TreeNode node = stack.poll();
            final TreeNode left = node.left;
            node.left = node.right;
            node.right = left;

            if (node.left != null) {
                stack.offer(node.left);
            }
            if (node.right != null) {
                stack.offer(node.right);
            }
        }
        return root;
    }
}
