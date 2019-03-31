package com.yuzhouwan.hacker.algorithms.leetcode.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.hacker.algorithms.leetcode.array
 *
 * @author Benedict Jin
 * @since 2016/9/6
 */
public class ShuffleArraySolutionTest {

    @Test
    public void operate() {

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
