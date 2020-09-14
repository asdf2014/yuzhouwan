package com.yuzhouwan.hacker.algorithms.leetcode;

import org.junit.Test;

import static org.junit.Assert.assertNull;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Counting Bits Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/6
 */
public class CountingBitsSolutionTest {

    private static final int SUPPRESS_TEST_LIMITATION = 10_1000;

    @Test
    public void countingBits() {

        assertNull(CountingBitsSolution.countBits(-1));
        assertEquals(0, CountingBitsSolution.countBits(0)[0]);

        int[] result = CountingBitsSolution.countBits(5);
        int[] except = new int[]{0, 1, 1, 2, 1, 2};
        for (int i = 0; i < 5; i++) {
            assertEquals(except[i], result[i]);
        }
        long b = System.currentTimeMillis();
        CountingBitsSolution.countBits(SUPPRESS_TEST_LIMITATION);
        long e = System.currentTimeMillis();
        System.out.println(e - b);      //20
    }

    @Test
    public void countingBitsWisdom() {

        assertNull(CountingBitsSolution.countBitsWisdom(-1));
        assertEquals(0, CountingBitsSolution.countBitsWisdom(0)[0]);

        int[] result = CountingBitsSolution.countBitsWisdom(5);
        int[] except = new int[]{0, 1, 1, 2, 1, 2};
        for (int i = 0; i < 5; i++) {
            assertEquals(except[i], result[i]);
        }
        long b = System.currentTimeMillis();
        CountingBitsSolution.countBitsWisdom(SUPPRESS_TEST_LIMITATION);
        long e = System.currentTimeMillis();
        System.out.println(e - b);      //1
    }
}
