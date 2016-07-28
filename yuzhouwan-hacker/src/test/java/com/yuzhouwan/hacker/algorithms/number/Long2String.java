package com.yuzhouwan.hacker.algorithms.number;

import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Long 2 String with thinking too big value.
 *
 * @author Benedict Jin
 * @since 2016/4/12 0030
 */
public class Long2String {

    @Test
    public void test() {
        long tooBig = 999999999999999999l;
        assertEquals("999999999999999999", tooBig + "");

        DecimalFormat decimalFormat = new DecimalFormat("0");
        assertEquals("999999999999999999", decimalFormat.format(tooBig));
    }
}
