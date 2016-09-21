package com.yuzhouwan.hacker.algorithms.leetcode.array;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Intersection of Two Arrays Solution Tester
 *
 * @author Benedict Jin
 * @since 2016/9/20
 */
public class IntersectionTwoArraysSolutionTest {

    @Test
    public void intersection() throws Exception {

        assertEquals(2,
                IntersectionTwoArraysSolution.intersection(new int[]{1, 2, 1, 2}, new int[]{2, 2})[0]);
        assertEquals(true,
                IntersectionTwoArraysSolution.intersection(new int[]{}, new int[]{2, 2}).length == 0);
        int[] result = IntersectionTwoArraysSolution.intersection(new int[]{1, 2, 3}, new int[]{3, 3, 1});
        assertEquals(true, result[0] == 1 && result[1] == 3);
    }
}
