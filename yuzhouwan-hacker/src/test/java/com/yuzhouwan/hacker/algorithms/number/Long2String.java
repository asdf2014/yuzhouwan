package com.yuzhouwan.hacker.algorithms.number;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Long 2 String with thinking too big value.
 *
 * @author Benedict Jin
 * @since 2016/4/12
 */
public class Long2String {

    @Test
    public void test() {
        long tooBig = 99_9999_9999_9999_9999L;
        assertEquals("999999999999999999", tooBig + "");

        DecimalFormat decimalFormat = new DecimalFormat("0");
        assertEquals("999999999999999999", decimalFormat.format(tooBig));
    }

    @Test
    public void bigNumber2Str() {
        int v = 5000_0000;
        assertEquals("16666666", "" + v / 3);
        assertEquals("2.5E7", "" + (Double.valueOf(v + "") / 2));
        assertEquals("16666667", "" + new BigDecimal(v).divide(BigDecimal.valueOf(3), new MathContext(8))
                .toPlainString());
    }
}
