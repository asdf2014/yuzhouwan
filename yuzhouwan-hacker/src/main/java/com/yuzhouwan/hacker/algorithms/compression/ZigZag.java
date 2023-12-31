package com.yuzhouwan.hacker.algorithms.compression;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：ZigZag
 *
 * @author Benedict Jin
 * @since 2019-02-15
 */
public final class ZigZag {

    private ZigZag() {
    }

    public static long encodeLong(long l) {
        return (l << 1) ^ (l >> 63);
    }

    public static long decodeLong(long l) {
//        return (l >>> 1) ^ ((l << 63) >> 63);
        return (l >> 1) ^ -(l & 1);
    }

    public static int encodeInt(int i) {
        return (i << 1) ^ (i >> 31);
    }

    public static int decodeInt(int i) {
//        return (i >>> 1) ^ ((i << 31) >> 31);
        return (i >> 1) ^ -(i & 1);
    }
}
