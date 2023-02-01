package com.yuzhouwan.hacker.algorithms.compression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：ZigZag Test
 *
 * @author Benedict Jin
 * @since 2019-02-15
 */
public class ZigZagTest {

    @Test
    public void testZigZagInt() {
        int a = -1;
        int b = 0;
        int c = 1;
        assertEquals(a, ZigZag.decodeInt(ZigZag.encodeInt(a)));
        assertEquals(b, ZigZag.decodeInt(ZigZag.encodeInt(b)));
        assertEquals(c, ZigZag.decodeInt(ZigZag.encodeInt(c)));
    }

    @Test
    public void testZigZagLong() {
        long a = -1;
        long b = 0;
        long c = 1;
        assertEquals(a, ZigZag.decodeLong(ZigZag.encodeLong(a)));
        assertEquals(b, ZigZag.decodeLong(ZigZag.encodeLong(b)));
        assertEquals(c, ZigZag.decodeLong(ZigZag.encodeLong(c)));
    }
}
