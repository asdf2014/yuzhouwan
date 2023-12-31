package com.yuzhouwan.hacker.algorithms.leetcode.tree;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Maximum Depth of Binary Tree Solution
 *
 * @author Benedict Jin
 * @since 2016/8/15
 */
class MaximumDepthBinaryTreeSolution {

    /**
     * https://leetcode.com/problems/maximum-depth-of-binary-tree/
     * <p>
     * 104. Maximum Depth of Binary Tree
     * <p>
     * Given a binary tree, find its maximum depth.
     * <p>
     * The maximum depth is the number of nodes along the longest path from the root node
     * down to the farthest leaf node.
     */
    static int maxDepthFirstDepth(TreeNode root) {
        return root == null ? 0 : Math.max(maxDepthFirstDepth(root.left), maxDepthFirstDepth(root.right)) + 1;
    }

    /**
     * 二叉树的非递归遍历.
     */
    static int maxDepthFirstBreadth(TreeNode root) {
        if (root == null)
            return 0;
        Queue<TreeNode> q = new LinkedBlockingDeque<>();
        q.add(root);
        int res = 0;
        TreeNode p;
        while (q.size() != 0) {
            ++res;
            for (int i = 0; i < q.size(); ++i) {
                p = q.poll();
                if (p.left != null)
                    q.add(p.left);
                if (p.right != null)
                    q.add(p.right);
            }
        }
        return res;
    }
}
