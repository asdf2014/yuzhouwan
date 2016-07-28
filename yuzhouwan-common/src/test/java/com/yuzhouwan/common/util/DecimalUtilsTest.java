package com.yuzhouwan.common.util;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Decimal Utils Tester
 *
 * @author Benedict Jin
 * @since 2016/5/9 0030
 */
public class DecimalUtilsTest {

    @Test
    public void convertInto16() throws Exception {
        assertEquals(1, DecimalUtils.convert(1));
        assertEquals(22, DecimalUtils.convert(16));
        assertEquals(23, DecimalUtils.convert(17));
    }

    @Test
    public void BooleanTest() throws Exception {
        Boolean ifCase = null;
        if (ifCase != null && ifCase) {
            System.out.println(ifCase);
        }
        ifCase = new Boolean(true);
        if (ifCase != null && ifCase) {
            System.out.println(ifCase);
        }
    }

    @Test
    public void double2BigIntTest() throws Exception {
        assertEquals(new BigInteger("2"), DecimalUtils.double2BigInt(2d));
        assertEquals(new BigInteger("2"), DecimalUtils.double2BigInt(2.1d));
        assertEquals(new BigInteger("0"), DecimalUtils.double2BigInt(0.1d));
    }
}
