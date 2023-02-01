package com.yuzhouwan.hacker.algorithms.leetcode.number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Single Number Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/11
 */
public class SingleNumberSolutionTest {

    @Test
    public void singleNumber() {

        assertEquals(-1, SingleNumberSolution.singleNumber(null));
        assertEquals(0, SingleNumberSolution.singleNumber(new int[]{}));
        assertEquals(1, SingleNumberSolution.singleNumber(new int[]{1}));
        assertEquals(2, SingleNumberSolution.singleNumber(new int[]{1, 1, 2, 3, 5, 4, 4, 3, 5}));
    }
}
