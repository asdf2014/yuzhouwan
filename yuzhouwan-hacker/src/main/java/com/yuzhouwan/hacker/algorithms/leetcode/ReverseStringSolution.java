package com.yuzhouwan.hacker.algorithms.leetcode;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Reverse String Solution
 *
 * @author Benedict Jin
 * @since 2016/8/4
 */
class ReverseStringSolution {

    /**
     * https://leetcode.com/problems/reverse-string/
     *
     * 344. Reverse String
     *
     * Write a function that takes a string as input and returns the string reversed.
     *
     * Example:
     * Given s = "hello", return "olleh".
     */
    static String reverseString(String s) {
        int len;
        if (s == null || (len = s.length()) <= 1) {
            return s;
        }
        char[] chars = new char[len];
        char[] result = new char[len];
        s.getChars(0, len, chars, 0);
        for (int i = 0; i < len; i++) {
            result[len - i - 1] = chars[i];
        }
        return new String(result);
    }

    static String reverseStringLitterSpace(String s) {
        int len;
        if (s == null || (len = s.length()) <= 1) {
            return s;
        }
        char[] chars = new char[len];
        s.getChars(0, len, chars, 0);
        char c;
        for (int i = 0; i < len / 2; i++) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    static String reverseStringRecursion(String s) {
        int length;
        if (s == null || (length = s.length()) <= 1) return s;
        String leftStr = s.substring(0, length / 2);
        String rightStr = s.substring(length / 2, length);
        return reverseStringRecursion(rightStr) + reverseStringRecursion(leftStr);
    }

    static String reverseStringSimplest(String s) {
        if (s == null || s.length() <= 1) return s;
        return new StringBuilder(s).reverse().toString();
    }
}
