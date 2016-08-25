package com.yuzhouwan.hacker.algorithms.leetcode.array;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Move Zeroes Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/25
 */
public class MoveZeroesSolutionTest {

    @Test
    public void moveZeroes() throws Exception {

        int[] nums = {0, 1, 0, 3, 12};
        int[] result = {1, 3, 12, 0, 0};
        MoveZeroesSolution.moveZeroes(nums);
        for (int i = 0; i < nums.length; i++) {
            assertEquals(nums[i], result[i]);
        }
    }

    @Test
    public void moveZeroesShift() throws Exception {

        int[] nums = {0, 5, 66, 0, 12};
        int[] result = {5, 66, 12, 0, 0};
        MoveZeroesSolution.moveZeroesShiftNonZero(nums);
        for (int i = 0; i < nums.length; i++) {
            assertEquals(nums[i], result[i]);
        }
    }
}
