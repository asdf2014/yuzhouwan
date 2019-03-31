package com.yuzhouwan.hacker.algorithms.leetcode.number;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Single Number III Solution
 *
 * @author Benedict Jin
 * @since 2016/8/22
 */
class SingleNumberIIISolution {

    /**
     * https://leetcode.com/problems/single-number-iii/
     * <p>
     * 260. Single Number III
     * <p>
     * Given an array of numbers nums, in which exactly two elements appear only once
     * and all the other elements appear exactly twice. Find the two elements that appear only once.
     * <p>
     * For example:
     * <p>
     * Given nums = [1, 2, 1, 3, 2, 5], return [3, 5].
     */
    static int[] singleNumber(int[] nums) {
        if (nums == null || nums.length == 1) return nums;

        int sum = 0;    // 6: 0110
        for (int num : nums) {
            sum ^= num;
        }
        //找出(两个不同的数 异或的)结果 bit中任意一个 1，方便后面的分组(这里直接取最右边的)，因为异或必然会有一个是 1，另一个为 0
        sum &= -sum;    // 2: 0010

        int[] result = {0, 0};
        for (int num : nums) {
            if ((num & sum) == 0) {
                result[0] ^= num;   // 1,1,5 : 0001,0001,0101 : 0101
            } else {
                result[1] ^= num;   // 2,3,2 : 0010,0010,0010 : 0010
            }
        }
        return result;
    }
}
