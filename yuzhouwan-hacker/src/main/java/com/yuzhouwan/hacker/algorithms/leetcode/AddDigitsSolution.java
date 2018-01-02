package com.yuzhouwan.hacker.algorithms.leetcode;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Add Digits Solution
 *
 * @author Benedict Jin
 * @since 2016/8/15
 */
class AddDigitsSolution {

    /**
     * https://leetcode.com/problems/add-digits/
     *
     * 258. Add Digits
     *
     * Given a non-negative integer num, repeatedly add all its digits until the result has only one digit.
     *
     * For example:
     *
     * Given num = 38, the process is like: 3 + 8 = 11, 1 + 1 = 2. Since 2 has only one digit, return it.
     *
     * Follow up:
     * Could you do it without any loop/recursion in O(1) runtime?
     *
     */
    static int addDigits(int num) {
        if (num < 10) return num;
        int i, count = 0;
        while ((i = num % 10) != 0 || num >= 10) {
            count += i;
            num -= i;
            num /= 10;
        }
        return addDigits(count);
    }

    static int addDigitsWisdom(int num) {
        return 1 + (num - 1) % 9;
    }
}
