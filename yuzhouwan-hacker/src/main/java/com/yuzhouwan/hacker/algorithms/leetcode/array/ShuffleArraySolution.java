package com.yuzhouwan.hacker.algorithms.leetcode.array;

import java.util.Random;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Shuffle Array Solution
 *
 * @author Benedict Jin
 * @since 2016/9/6
 */
class ShuffleArraySolution {

    private final int[] nums;
    private final Random r;

    /**
     * https://leetcode.com/problems/shuffle-an-array
     * <p>
     * 384. Shuffle an Array
     * <p>
     * Shuffle a set of numbers without duplicates.
     */
    ShuffleArraySolution(int[] nums) {
        this.nums = nums;
        r = new Random();
    }

    /**
     * Resets the array to its original configuration and return it.
     */
    int[] reset() {
        return this.nums;
    }

    /**
     * Returns a random shuffling of the array.
     */
    int[] shuffle() {
        int len;
        if (nums == null || (len = nums.length) < 2) return nums;
        int[] clone = nums.clone();
        int temp;
        int index;
        for (int i = 0; i < len; i++) {
            index = r.nextInt(i + 1);
            temp = clone[index];
            clone[index] = clone[i];
            clone[i] = temp;
        }
        return clone;
    }
}
