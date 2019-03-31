package com.yuzhouwan.hacker.algorithms.leetcode;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Counting Bits Solution
 *
 * @author Benedict Jin
 * @since 2016/8/6
 */
class CountingBitsSolution {

    /**
     * https://leetcode.com/problems/counting-bits/
     * <p>
     * 338. Counting Bits
     * <p>
     * Given a non negative integer number num.
     * For every numbers i in the range 0 ≤ i ≤ num calculate the number of 1's
     * in their binary representation and return them as an array.
     * <p>
     * Example:
     * For num = 5 you should return [0,1,1,2,1,2].
     * <p>
     * Follow up:
     * <p>
     * It is very easy to come up with a solution with run time O(n*sizeof(integer)).
     * But can you do it in linear time O(n) /possibly in a single pass?
     * Space complexity should be O(n).
     * <p>
     * Can you do it like a boss? Do it without using any builtin function like __builtin_popcount in c++
     * or in any other language.
     */
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
