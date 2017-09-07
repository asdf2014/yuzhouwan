package com.yuzhouwan.hacker.algorithms.leetcode.array;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Product of Array Except Self Solution
 *
 * @author Benedict Jin
 * @since 2016/8/29
 */
class ProductArrayExceptSelfSolution {

    /**
     * https://leetcode.com/problems/product-of-array-except-self/
     *
     * 238. Product of Array Except Self
     *
     * Given an array of n integers where n > 1, nums, return an array output
     * such that output[i] is equal to the product of all the elements of nums except nums[i].
     *
     * Solve it without division and in O(n).
     *
     * For example, given [1,2,3,4], return [24,12,8,6].
     */
    static int[] productExceptSelf(int[] nums) {

        int len = nums.length;
        int[] result = new int[len];
        result[0] = 1;
        for (int i = 1; i < len; i++) {
            result[i] = result[i - 1] * nums[i - 1];      //1, 1, 2, 6
        }
        int right = 1;
        for (int i = len - 1; i >= 0; i--) {
            result[i] *= right;
            right *= nums[i];       //1, 4, 12, 24
        }
        return result;
    }
}
