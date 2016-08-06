package com.yuzhouwan.hacker.algorithms.leetcode;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Counting Bits Solution
 *
 * @author Benedict Jin
 * @since 2016/8/6
 */
class CountingBitsSolution {

    static int[] countBits(int num) {
        if (num < 0) return null;

        int[] result = new int[num + 1];
        String binary;
        int count;
        //[0, 1, 10, 11, 100, 101]
        for (int i = 0; i <= num; i++) {
            binary = Integer.toBinaryString(i);
            count = 0;
            for (char c : binary.toCharArray()) {
                if (c == '1')
                    count++;
            }
            result[i] = count;
        }
        return result;
    }

    //f[i] = f[i / 2] + i % 2
    static int[] countBitsWisdom(int num) {
        if (num < 0) return null;

        int[] f = new int[num + 1];
        //0, 1, 1, 2, 1, 2
        for (int i = 1; i <= num; i++) f[i] = f[i >> 1] + (i & 1);
        return f;
    }
}
