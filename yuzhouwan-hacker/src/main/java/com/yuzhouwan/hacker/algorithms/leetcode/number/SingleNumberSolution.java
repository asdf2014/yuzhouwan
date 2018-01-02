package com.yuzhouwan.hacker.algorithms.leetcode.number;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Single Number Solution
 *
 * @author Benedict Jin
 * @since 2016/8/11
 */
class SingleNumberSolution {

    /**
     * https://leetcode.com/problems/single-number/
     *
     * 136. Single Number
     *
     * Given an array of integers, every element appears twice except for one. Find that single one.
     *
     * Note:
     * Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?
     *
     *
     * 1^1 ^ 2^2 ^ 3 = 0 ^ 0 ^ 3 = 3
     */
    static int singleNumber(int[] nums) {
        if (nums == null) return -1;
        if (nums.length == 1) return nums[0];

        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
}
