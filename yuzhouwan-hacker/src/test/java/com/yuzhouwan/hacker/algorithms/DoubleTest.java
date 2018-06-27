package com.yuzhouwan.hacker.algorithms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Double Test
 *
 * @author Benedict Jin
 * @since 2018/6/27
 */
public class DoubleTest {

    @Test
    public void count2One() {
        double d = 0;
        d += 0.1;
        assertEquals("0.1", String.valueOf(d));
        d += 0.1;
        assertEquals("0.2", String.valueOf(d));
        d += 0.1;
        assertEquals("0.30000000000000004", String.valueOf(d));
        d += 0.1;
        assertEquals("0.4", String.valueOf(d));
        d += 0.1;
        assertEquals("0.5", String.valueOf(d));
        d += 0.1;
        assertEquals("0.6", String.valueOf(d));
        d += 0.1;
        assertEquals("0.7", String.valueOf(d));
        d += 0.1;
        assertEquals("0.7999999999999999", String.valueOf(d));
        d += 0.1;
        assertEquals("0.8999999999999999", String.valueOf(d));
        d += 0.1;
        assertEquals("0.9999999999999999", String.valueOf(d));
    }
}
