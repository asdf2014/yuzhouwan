package com.yuzhouwan.hacker.algorithms.leetcode.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Product of Array Except Self Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/29
 */
public class ProductArrayExceptSelfSolutionTest {

    @Test
    public void productExceptSelf() {

        int[] origin = {1, 2, 3, 4};
        int[] aim = {24, 12, 8, 6};
        origin = ProductArrayExceptSelfSolution.productExceptSelf(origin);
        for (int i = 0; i < origin.length; i++) {
            assertEquals(aim[i], origin[i]);
        }
    }
}
