package com.yuzhouwan.hacker.algorithms.leetcode.number;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Count Numbers with Unique Digits Solution
 *
 * @author Benedict Jin
 * @since 2016/10/17
 */
class CountNumbersUniqueDigitsSolution {

    /**
     * https://leetcode.com/problems/count-numbers-with-unique-digits/
     *
     * 357. Count Numbers with Unique Digits
     *
     * Given a non-negative integer n, count all numbers with unique digits, x, where 0 ≤ x < 10^n.
     *
     * Example:
     * Given n = 2, return 91. (The answer should be the total numbers in the range of 0 ≤ x < 100,
     * excluding [11,22,33,44,55,66,77,88,99])
     *
     * Explain:
     * f(0) = 1
     * f(1) = 10
     * f(2) = 9 * 9
     * f(3) = f(2) * 8 =  9 * 9 * 8
     * f(4) = f(3) * 7 = 9 * 9 * 8 * 7
     * f(10) = 9 * 9 * 8 * 7 * 6 * ... * 1
     * f(11) = 0 = f(12) = f(13)....
     */
    static int countNumbersWithUniqueDigits(int n) {

        if (n == 0) return 1;

        int res = 10;
        int uniqueDigits = 9;
        int availableNumber = 9;
        while (n-- > 1 && availableNumber > 0) {
            uniqueDigits = uniqueDigits * availableNumber;
            res += uniqueDigits;
            availableNumber--;
        }
        return res;
    }
}
