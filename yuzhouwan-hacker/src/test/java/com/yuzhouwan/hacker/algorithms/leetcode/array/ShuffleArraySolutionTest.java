package com.yuzhouwan.hacker.algorithms.leetcode.array;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.hacker.algorithms.leetcode.array
 *
 * @author Benedict Jin
 * @since 2016/9/6
 */
public class ShuffleArraySolutionTest {

    @Test
    public void operate() throws Exception {

        int[] nums = new int[]{1, 2, 3};
        ShuffleArraySolution shuffleArraySolution = new ShuffleArraySolution(nums);
        for (int shuffle : shuffleArraySolution.shuffle()) {
            System.out.print(shuffle);
        }
        int[] origin = shuffleArraySolution.reset();
        for (int i = 0; i < origin.length; i++) {
            assertEquals(nums[i], origin[i]);
        }
    }
}
