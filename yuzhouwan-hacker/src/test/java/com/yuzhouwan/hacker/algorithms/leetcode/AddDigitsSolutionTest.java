package com.yuzhouwan.hacker.algorithms.leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Add Digits Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/15
 */
public class AddDigitsSolutionTest {

    @Test
    public void addDigitsTest() {

        assertEquals(-1, AddDigitsSolution.addDigits(-1));
        assertEquals(0, AddDigitsSolution.addDigits(0));
        assertEquals(1, AddDigitsSolution.addDigits(100));
        assertEquals(2, AddDigitsSolution.addDigits(38));
        assertEquals(3, AddDigitsSolution.addDigits(579));
    }

    @Test
    public void addDigitsWisdomTest() {

        assertEquals(-1, AddDigitsSolution.addDigitsWisdom(-1));
        assertEquals(0, AddDigitsSolution.addDigitsWisdom(0));
        assertEquals(1, AddDigitsSolution.addDigitsWisdom(100));
        assertEquals(2, AddDigitsSolution.addDigitsWisdom(38));
        assertEquals(3, AddDigitsSolution.addDigitsWisdom(579));
    }
}
