package com.yuzhouwan.hacker.algorithms.leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Reverse String Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/8/4
 */
public class ReverseStringSolutionTest {

    private static final int SUPPRESS_TEST_LIMITATION = 100_1000;

    @Test
    public void solution() throws Exception {

        assertEquals(null, ReverseStringSolution.reverseString(null));
        assertEquals("", ReverseStringSolution.reverseString(""));
        assertEquals("a", ReverseStringSolution.reverseString("a"));
        assertEquals("olleh", ReverseStringSolution.reverseString("hello"));
        assertEquals("!dlrow", ReverseStringSolution.reverseString("world!"));
        long b = System.currentTimeMillis();
        int count = SUPPRESS_TEST_LIMITATION;
        while (count > 0) {
            count--;
            ReverseStringSolution.reverseString("yuzhouwan.com");
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);      //1035
    }

    @Test
    public void solutionLitterSpace() throws Exception {

        assertEquals(null, ReverseStringSolution.reverseStringLitterSpace(null));
        assertEquals("", ReverseStringSolution.reverseStringLitterSpace(""));
        assertEquals("a", ReverseStringSolution.reverseStringLitterSpace("a"));
        assertEquals("olleh", ReverseStringSolution.reverseStringLitterSpace("hello"));
        assertEquals("!dlrow", ReverseStringSolution.reverseStringLitterSpace("world!"));

        long b = System.currentTimeMillis();
        int count = SUPPRESS_TEST_LIMITATION;
        while (count > 0) {
            count--;
            ReverseStringSolution.reverseStringLitterSpace("yuzhouwan.com");
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);      //43
    }

    @Test
    public void solutionRecursion() throws Exception {

        assertEquals(null, ReverseStringSolution.reverseStringRecursion(null));
        assertEquals("", ReverseStringSolution.reverseStringRecursion(""));
        assertEquals("a", ReverseStringSolution.reverseStringRecursion("a"));
        assertEquals("olleh", ReverseStringSolution.reverseStringRecursion("hello"));
        assertEquals("!dlrow", ReverseStringSolution.reverseStringRecursion("world!"));

        long b = System.currentTimeMillis();
        int count = SUPPRESS_TEST_LIMITATION;
        while (count > 0) {
            count--;
            ReverseStringSolution.reverseStringRecursion("yuzhouwan.com");
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);      //53
    }

    @Test
    public void solutionSimplest() throws Exception {

        assertEquals(null, ReverseStringSolution.reverseStringSimplest(null));
        assertEquals("", ReverseStringSolution.reverseStringSimplest(""));
        assertEquals("a", ReverseStringSolution.reverseStringSimplest("a"));
        assertEquals("olleh", ReverseStringSolution.reverseStringSimplest("hello"));
        assertEquals("!dlrow", ReverseStringSolution.reverseStringSimplest("world!"));

        long b = System.currentTimeMillis();
        int count = SUPPRESS_TEST_LIMITATION;
        while (count > 0) {
            count--;
            ReverseStringSolution.reverseStringSimplest("yuzhouwan.com");
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);      //73
    }
}
