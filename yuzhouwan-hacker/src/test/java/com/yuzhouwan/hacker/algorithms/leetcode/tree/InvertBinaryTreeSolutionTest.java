package com.yuzhouwan.hacker.algorithms.leetcode.tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Invert Binary Tree Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/16
 */
public class InvertBinaryTreeSolutionTest {

    @Test
    public void invertTree() throws Exception {

        TreeNode root0 = new TreeNode(0);
        TreeNode left1 = new TreeNode(1);
        TreeNode right1 = new TreeNode(2);
        TreeNode left21 = new TreeNode(21);
        TreeNode right22 = new TreeNode(22);
        TreeNode left23 = new TreeNode(23);
        TreeNode right24 = new TreeNode(24);

        root0.left = left1;
        root0.right = right1;

        left1.left = left21;
        left1.right = right22;
        right1.left = left23;
        right1.right = right24;

        assertEquals(22, root0.left.right.val);
        assertEquals(23, InvertBinaryTreeSolution.invertTreeRecursion(root0).left.right.val);
    }

    @Test
    public void invertTreeDFS() throws Exception {

        TreeNode root0 = new TreeNode(0);
        TreeNode left1 = new TreeNode(1);
        TreeNode right1 = new TreeNode(2);
        TreeNode left21 = new TreeNode(21);
        TreeNode right22 = new TreeNode(22);
        TreeNode left23 = new TreeNode(23);
        TreeNode right24 = new TreeNode(24);

        root0.left = left1;
        root0.right = right1;

        left1.left = left21;
        left1.right = right22;
        right1.left = left23;
        right1.right = right24;

        assertEquals(24, root0.right.right.val);
        assertEquals(21, InvertBinaryTreeSolution.invertTreeDFS(root0).right.right.val);
    }

    @Test
    public void invertTreeBFS() throws Exception {

        TreeNode root0 = new TreeNode(0);
        TreeNode left1 = new TreeNode(1);
        TreeNode right1 = new TreeNode(2);
        TreeNode left21 = new TreeNode(21);
        TreeNode right22 = new TreeNode(22);
        TreeNode left23 = new TreeNode(23);
        TreeNode right24 = new TreeNode(24);

        root0.left = left1;
        root0.right = right1;

        left1.left = left21;
        left1.right = right22;
        right1.left = left23;
        right1.right = right24;

        assertEquals(21, root0.left.left.val);
        assertEquals(24, InvertBinaryTreeSolution.invertTreeBFS(root0).left.left.val);
    }
}
