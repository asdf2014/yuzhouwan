package com.yuzhouwan.hacker.algorithms.leetcode.number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Sum of Two Integers Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/8
 */
public class SumTwoIntegerSolutionTest {

    @Test
    public void sum() throws Exception {

        assertEquals(0, SumTwoIntegerSolution.sum(0, 0));
        assertEquals(1, SumTwoIntegerSolution.sum(1, 0));
        assertEquals(2, SumTwoIntegerSolution.sum(0, 2));
        assertEquals(6, SumTwoIntegerSolution.sum(3, 3));
        assertEquals(Integer.MAX_VALUE, SumTwoIntegerSolution.sum(Integer.MAX_VALUE - 10, 10));
    }
}
