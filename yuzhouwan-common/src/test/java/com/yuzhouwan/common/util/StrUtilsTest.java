package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: String Stuff Tester
 *
 * @author Benedict Jin
 * @since 2016/3/23 0030
 */
public class StrUtilsTest {

    @Test
    public void fillTest() throws Exception {
        assertEquals("00000010", StrUtils.fillWitchZero(10, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.0d, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.01d, 8));
    }
}
