package com.yuzhouwan.hacker.algorithms.leetcode.tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Same Tree Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/9/22
 */
public class SameTreeSolutionTest {

    @Test
    public void isSameTree() throws Exception {

        TreeNode t0 = new TreeNode(0);
        assertEquals(false, SameTreeSolution.isSameTree(t0, new TreeNode(1)));
        TreeNode n0 = new TreeNode(0);
        assertEquals(true, SameTreeSolution.isSameTree(t0, n0));
        TreeNode t11 = new TreeNode(11);
        TreeNode t12 = new TreeNode(12);
        t0.left = t11;
        t0.right = t12;
        assertEquals(true, SameTreeSolution.isSameTree(t0, t0));
        TreeNode n11 = new TreeNode(11);
        TreeNode n12 = new TreeNode(12);
        TreeNode n21 = new TreeNode(21);
        n0.left = n11;
        n0.right = n12;
        n11.left = n21;
        assertEquals(false, SameTreeSolution.isSameTree(t0, n0));
        TreeNode m0 = new TreeNode(0);
        TreeNode m11 = new TreeNode(11);
        TreeNode m12 = new TreeNode(12);
        TreeNode m22 = new TreeNode(21);
        m0.left = m11;
        m0.right = m12;
        m11.right = m22;
        assertEquals(false, SameTreeSolution.isSameTree(t0, m0));
        TreeNode t21 = new TreeNode(21);
        TreeNode t23 = new TreeNode(23);
        t11.left = t21;
        t12.left = t23;
        n12.left = new TreeNode(23);
        assertEquals(true, SameTreeSolution.isSameTree(t0, n0));
    }

    @Test
    public void wisdom() throws Exception {
        TreeNode t0 = new TreeNode(0);
        assertEquals(false, SameTreeSolution.wisdom(t0, new TreeNode(1)));
        TreeNode n0 = new TreeNode(0);
        assertEquals(true, SameTreeSolution.wisdom(t0, n0));
        TreeNode t11 = new TreeNode(11);
        TreeNode t12 = new TreeNode(12);
        t0.left = t11;
        t0.right = t12;
        assertEquals(true, SameTreeSolution.wisdom(t0, t0));
        TreeNode n11 = new TreeNode(11);
        TreeNode n12 = new TreeNode(12);
        TreeNode n21 = new TreeNode(21);
        n0.left = n11;
        n0.right = n12;
        n11.left = n21;
        assertEquals(false, SameTreeSolution.wisdom(t0, n0));
        TreeNode m0 = new TreeNode(0);
        TreeNode m11 = new TreeNode(11);
        TreeNode m12 = new TreeNode(12);
        TreeNode m22 = new TreeNode(21);
        m0.left = m11;
        m0.right = m12;
        m11.right = m22;
        assertEquals(false, SameTreeSolution.wisdom(t0, m0));
        TreeNode t21 = new TreeNode(21);
        TreeNode t23 = new TreeNode(23);
        t11.left = t21;
        t12.left = t23;
        n12.left = new TreeNode(23);
        assertEquals(true, SameTreeSolution.wisdom(t0, n0));
    }
}
