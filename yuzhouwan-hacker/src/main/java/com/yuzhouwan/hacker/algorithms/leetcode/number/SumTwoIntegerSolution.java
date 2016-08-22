package com.yuzhouwan.hacker.algorithms.leetcode.number;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Sum of Two Integers Solution
 *
 * @author Benedict Jin
 * @since 2016/8/8
 */
class SumTwoIntegerSolution {

    /**
     * 371. Sum of Two Integers
     *
     * https://leetcode.com/problems/sum-of-two-integers/
     *
     * Calculate the sum of two integers a and b, but you are not allowed to use the operator + and -.
     *
     * Example:
     *      Given a = 1 and b = 2, return 3.
     */
    // 1 + 3 = 0001 + 0011 => &: 0001, ^: 0011, &<<1: 0010 => &: 0010, ^: 0011, &<<1: 0100 => &: 0000, ^: 0111, &<<1: 0000 = 4
    static int sum(int a, int b) {
        if (a == 0) return b;
        if (b == 0) return a;
        int temp;
        while (b != 0) {
            temp = a & b;
            a = a ^ b;
            b = temp << 1;
        }
        return a;
    }
}
