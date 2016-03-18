package com.yuzhouwan.hacker.commons;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：CollectionStuff Tester
 *
 * @author Benedict Jin
 * @since 2016/3/15 0030
 */
public class StringTest {

    @Test
    public void testSplitNothing() {

        String s1 = "1,2,3";
        String s2 = "1";
        String[] r1 = s1.split(",");
        String[] r2 = s2.split(",");
        assertEquals(3, r1.length);
        assertEquals(1, r2.length);

        assertEquals("2", r1[1]);
        assertEquals("1", r1[0]);
    }

}
