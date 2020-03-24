package com.yuzhouwan.common.util;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：Byte Utils
 *
 * @author Benedict Jin
 * @since 2020/3/24
 */
public final class ByteUtils {

    private ByteUtils() {
    }

    public static byte[] merge(byte[] left, byte[] right) {
        byte[] combined = new byte[left.length + right.length];
        System.arraycopy(left, 0, combined, 0, left.length);
        System.arraycopy(right, 0, combined, left.length, right.length);
        return combined;
    }
}
