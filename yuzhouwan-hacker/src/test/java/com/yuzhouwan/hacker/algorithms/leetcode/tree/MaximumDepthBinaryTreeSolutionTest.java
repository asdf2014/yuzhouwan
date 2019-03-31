package com.yuzhouwan.hacker.algorithms.leetcode.tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Maximum Depth of Binary Tree Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/15
 */
public class MaximumDepthBinaryTreeSolutionTest {

    @Test
    public void maxDepthFirstDepth() {

        assertEquals(0, MaximumDepthBinaryTreeSolution.maxDepthFirstDepth(null));
        TreeNode root0 = new TreeNode(0);
        assertEquals(1, MaximumDepthBinaryTreeSolution.maxDepthFirstDepth(root0));
        TreeNode left1 = new TreeNode(1);
        left1.left = new TreeNode(2);
        root0.left = left1;
        assertEquals(3, MaximumDepthBinaryTreeSolution.maxDepthFirstDepth(root0));
    }

    @Test
    public void maxDepthFirstBreadth() {

        assertEquals(0, MaximumDepthBinaryTreeSolution.maxDepthFirstBreadth(null));
        TreeNode root0 = new TreeNode(0);
        assertEquals(1, MaximumDepthBinaryTreeSolution.maxDepthFirstBreadth(root0));
        TreeNode left1 = new TreeNode(1);
        left1.left = new TreeNode(2);
        root0.left = left1;
        assertEquals(3, MaximumDepthBinaryTreeSolution.maxDepthFirstBreadth(root0));
    }
}
