package com.yuzhouwan.hacker.algorithms.leetcode.number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Single Number III Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/22
 */
public class SingleNumberIIISolutionTest {

    @Test
    public void singleNumber() throws Exception {
        {
            int[] nums = {1, 2, 1, 3, 2, 5};
            nums = SingleNumberIIISolution.singleNumber(nums);
            int[] result = {5, 3};
            for (int i = 0; i < nums.length; i++) {
                assertEquals(result[i], nums[i]);
            }
        }
        {
            int[] nums = {1, 2, 1, 3, 2, 5, 5, 7};
            nums = SingleNumberIIISolution.singleNumber(nums);
            int[] result = {3, 7};
            for (int i = 0; i < nums.length; i++) {
                assertEquals(result[i], nums[i]);
            }
        }
        {
            int[] nums = {1, 2, 1, 3, 2, 5, 5, 7, 7, 9};
            nums = SingleNumberIIISolution.singleNumber(nums);
            int[] result = {9, 3};
            for (int i = 0; i < nums.length; i++) {
                assertEquals(result[i], nums[i]);
            }
        }
    }
}
