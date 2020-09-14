package com.yuzhouwan.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Decimal Utils Tester
 *
 * @author Benedict Jin
 * @since 2016/5/9
 */
public class DecimalUtilsTest {

    @Test
    public void convertInto16() {
        assertEquals(1, DecimalUtils.convert2Hex(1));
        assertEquals(22, DecimalUtils.convert2Hex(16));
        assertEquals(23, DecimalUtils.convert2Hex(17));
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void BooleanTest() {
        Boolean ifCase = null;
        if (ifCase != null && ifCase) {
            System.out.println("null: " + ifCase);
        }
        ifCase = new Boolean(true);
        if (ifCase != null && ifCase) {
            System.out.println("Boolean(true): " + ifCase);
        }
    }

    @Test
    public void double2BigIntTest() {
        assertEquals(new BigInteger("2"), DecimalUtils.double2BigInt(2d));
        assertEquals(new BigInteger("2"), DecimalUtils.double2BigInt(2.1d));
        assertEquals(new BigInteger("0"), DecimalUtils.double2BigInt(0.1d));
    }

    @Test
    public void charTest() {
        assertEquals(65, 'A');
    }

    @Test
    public void savePointTest() {
        assertEquals("0.23", DecimalUtils.saveTwoPoint(0.23456d));
        assertEquals("1.23", DecimalUtils.saveTwoPoint(1.23456d));
        assertEquals("11.23", DecimalUtils.saveTwoPoint(11.23456d));
        assertEquals("1.23", DecimalUtils.saveTwoPoint(01.23456d));
        assertEquals("10.23", DecimalUtils.saveTwoPoint(010.23456d));

        assertEquals("10", DecimalUtils.savePoint(010.23456d, 0));
        assertEquals("10.2", DecimalUtils.savePoint(010.23456d, 1));
        assertEquals("10.2345600000", DecimalUtils.savePoint(010.23456d, 10));
    }

    @Test
    public void byteBuffer2byteArrayTest() {
        String s = "yuzhouwan.com";
        byte[] bytes = s.getBytes();
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        assertEquals(s, new String(DecimalUtils.byteBuffer2byteArray(bb)));
        assertEquals(s, new String(DecimalUtils.byteBuffer2byteArray(
                DecimalUtils.byteArray2byteBuffer(bytes))));
    }
}