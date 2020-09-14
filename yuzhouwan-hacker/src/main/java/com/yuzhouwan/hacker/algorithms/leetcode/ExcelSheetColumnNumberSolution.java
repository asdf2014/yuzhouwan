package com.yuzhouwan.hacker.algorithms.leetcode;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Excel Sheet Column Number Solution
 *
 * @author Benedict Jin
 * @since 2016/10/13
 */
class ExcelSheetColumnNumberSolution {

    /**
     * https://leetcode.com/problems/excel-sheet-column-number/
     * <p>
     * 171. Excel Sheet Column Number
     * <p>
     * Related to question Excel Sheet Column Title
     * <p>
     * Given a column title as appear in an Excel sheet, return its corresponding column number.
     * <p>
     * For example:
     * <p>
     * A -> 1
     * B -> 2
     * C -> 3
     * ...
     * Z -> 26
     * AA -> 27
     * AB -> 28
     */
    static int titleToNumber(String s) {

        char[] chars = s.toCharArray();
        int sum = 0;
        int level = 1;
        for (int i = chars.length - 1; i >= 0; i--) {
            sum += (chars[i] - 64) * level;
            level *= 26;
        }
        return sum;
    }

    static int simple(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); result = result * 26 + (s.charAt(i) - 'A' + 1), i++) ;
        return result;
    }
}
