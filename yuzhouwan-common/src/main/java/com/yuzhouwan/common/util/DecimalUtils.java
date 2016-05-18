package com.yuzhouwan.common.util;

import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Created by Benedict Jin on 2016/5/9.
 */
public class DecimalUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0");

    public static int convert(int n) {
        return Integer.valueOf(String.valueOf(n), 16);
    }

    public static BigInteger double2BigInt(double d) {
        return new BigInteger(decimalFormat.format(d));
    }
}
