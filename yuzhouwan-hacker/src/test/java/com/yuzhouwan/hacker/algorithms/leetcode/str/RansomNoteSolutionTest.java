package com.yuzhouwan.hacker.algorithms.leetcode.str;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: com.yuzhouwan.hacker.algorithms.leetcode.str
 *
 * @author Benedict Jin
 * @since 2016/9/19
 */
public class RansomNoteSolutionTest {

    @Test
    public void canConstruct() throws Exception {

        assertEquals(false, RansomNoteSolution.canConstruct("a", "b"));
        assertEquals(false, RansomNoteSolution.canConstruct("aa", "ab"));
        assertEquals(true, RansomNoteSolution.canConstruct("aa", "aba"));
        assertEquals(true, RansomNoteSolution.canConstruct("aa", "aab"));
    }
}
