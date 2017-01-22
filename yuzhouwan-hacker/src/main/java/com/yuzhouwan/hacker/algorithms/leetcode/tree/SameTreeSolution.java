package com.yuzhouwan.hacker.algorithms.leetcode.tree;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Same Tree Solution
 *
 * @author Benedict Jin
 * @since 2016/9/22
 */
class SameTreeSolution {

    /**
     * https://leetcode.com/problems/same-tree/
     *
     * 100. Same Tree
     *
     * Given two binary trees, write a function to check if they are equal or not.
     *
     * Two binary trees are considered equal if they are structurally identical and the nodes have the same value.
     */
    static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q != null || p != null && q == null) return false;
        else if (p == null) return true;
        TreeNode pLeft = p.left,
                qLeft = q.left,
                pRight = p.right,
                qRight = q.right;
        if (pLeft == null && qLeft != null || pRight != null && qRight == null) return false;
        if (pLeft != null && qLeft != null && pLeft.val != qLeft.val
                || pRight != null && pRight.val != qRight.val) return false;
        if (pLeft == null && pRight == null && qRight == null)
            return p.val == q.val;
        boolean isSame = isSameTree(pLeft, qLeft);
        isSame &= isSameTree(pRight, qRight);
        return isSame;
    }

    static boolean wisdom(TreeNode p, TreeNode q) {
        return p == null && q == null || !(p == null || q == null) && p.val == q.val && wisdom(p.left, q.left) && wisdom(p.right, q.right);
    }
}
