package com.yuzhouwan.hacker.algorithms.leetcode.str;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.hacker.algorithms.leetcode.str
 *
 * @author Benedict Jin
 * @since 2016/9/19
 */
public class RansomNoteSolutionTest {

    @Test
    public void canConstruct() {

        assertFalse(RansomNoteSolution.canConstruct("a", "b"));
        assertFalse(RansomNoteSolution.canConstruct("aa", "ab"));
        assertTrue(RansomNoteSolution.canConstruct("aa", "aba"));
        assertTrue(RansomNoteSolution.canConstruct("aa", "aab"));
    }
}
