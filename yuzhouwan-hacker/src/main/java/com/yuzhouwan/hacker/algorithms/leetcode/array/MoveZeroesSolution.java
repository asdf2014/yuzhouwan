package com.yuzhouwan.hacker.algorithms.leetcode.array;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Move Zeroes Solution
 *
 * @author Benedict Jin
 * @since 2016/8/25
 */
class MoveZeroesSolution {

    /**
     * https://leetcode.com/problems/move-zeroes/
     * <p>
     * 283. Move Zeroes
     * <p>
     * Given an array nums, write a function to move all 0's to the end of
     * it while maintaining the relative order of the non-zero elements.
     * <p>
     * For example, given nums = [0, 1, 0, 3, 12], after calling your function, nums should be [1, 3, 12, 0, 0].
     * <p>
     * Note:
     * You must do this in-place without making a copy of the array.
     * Minimize the total number of operations.
     */
    static void moveZeroes(int[] nums) {

        int len;
        if (nums == null || (len = nums.length) <= 1) return;

        boolean doSomething = false;
        int index;
        int indexFront;
        for (int i = 1; i < len; i++) {

            index = nums[len - i];
            indexFront = nums[len - i - 1];
            if (indexFront == 0 && index != 0) {
                nums[len - i] = indexFront;
                nums[len - i - 1] = index;
                doSomething = true;
            }
        }
        if (!doSomething) return;
        moveZeroes(nums);
    }

    static void moveZeroesShiftNonZero(int[] nums) {

        int len;
        if (nums == null || (len = nums.length) <= 1) return;

        int insertPos = 0;
        for (int num : nums) {
            if (num != 0) nums[insertPos++] = num;
        }

        while (insertPos < len) {
            nums[insertPos++] = 0;
        }
    }
}
