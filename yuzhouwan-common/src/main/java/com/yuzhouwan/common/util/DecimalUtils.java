package com.yuzhouwan.common.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Decimal Utils
 *
 * @author Benedict Jin
 * @since 2016/5/9
 */
public final class DecimalUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0");
    private static final int HEX_LENGTH = 16;

    private DecimalUtils() {
    }

    public static int convert2Hex(int n) {
        return Integer.valueOf(String.valueOf(n), HEX_LENGTH);
    }

    public static BigInteger double2BigInt(double d) {
        return new BigInteger(decimalFormat.format(d));
    }

    public static String saveTwoPoint(double d) {
        return savePoint(d, 2);
    }

    public static String savePoint(double d, int point) {
        return String.format("%.".concat(point + "f"), d);
    }

    public static byte[] byteBuffer2byteArray(ByteBuffer bb) {
        bb.clear();
        byte[] ba = new byte[bb.remaining()];
        bb.get(ba, 0, ba.length);
        return ba;
    }

    public static ByteBuffer byteArray2byteBuffer(byte[] ba) {
        return ByteBuffer.wrap(ba);
    }
}
