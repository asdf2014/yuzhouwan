package com.yuzhouwan.hacker.algorithms.compression;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdException;
import com.yuzhouwan.common.util.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：ZSTD Test
 *
 * @author Benedict Jin
 * @since 2020-03-24
 */
public class ZSTDTest {

    @Test
    public void testSimple() {
        final String origin = "yuzhouwan.com, www.yuzhouwan.com, unknown.yuzhouwan.com";

        final byte[] originBytes = origin.getBytes(StandardCharsets.UTF_8);
        info(originBytes);

        final byte[] compressedBytes = Zstd.compress(originBytes);
        info(compressedBytes);

        final int size = compressSize(compressedBytes);
        final byte[] decompress = Zstd.decompress(compressedBytes, size);
        Assert.assertEquals(origin, new String(decompress, StandardCharsets.UTF_8));
    }

    @Test
    public void testMerge() {
        final String origin1 = "Benedict";
        final String origin2 = "Jin";

        final byte[] originBytes1 = origin1.getBytes(StandardCharsets.UTF_8);
        info(originBytes1);
        final byte[] originBytes2 = origin2.getBytes(StandardCharsets.UTF_8);
        info(originBytes2);

        final byte[] compressedBytes1 = Zstd.compress(originBytes1);
        info(compressedBytes1);
        final byte[] compressedBytes2 = Zstd.compress(originBytes2);
        info(compressedBytes2);

        final byte[] compressedBytes = ByteUtils.merge(compressedBytes1, compressedBytes2);
        info(compressedBytes);

        final long size = Zstd.decompressedSize(compressedBytes);
        System.out.println(size);
        System.out.println(compressedBytes.length);
        final byte[] decompress = Zstd.decompress(compressedBytes, (int) size * 10);
        Assert.assertEquals(origin1 + origin2, new String(decompress, StandardCharsets.UTF_8));
    }

    @Test
    public void testMixed() {
        final String origin1 = "compress";
        final String origin2 = "origin";

        final byte[] originBytes1 = origin1.getBytes(StandardCharsets.UTF_8);
        info(originBytes1);
        final byte[] originBytes2 = origin2.getBytes(StandardCharsets.UTF_8);
        info(originBytes2);

        final byte[] compressedBytes1 = Zstd.compress(originBytes1);
        info(compressedBytes1);

        final byte[] fixedBytes = ByteUtils.merge(compressedBytes1, originBytes2);
        info(fixedBytes);

        final long size = Zstd.decompressedSize(fixedBytes);
        System.out.println(size);
        System.out.println(fixedBytes.length);

        final ZstdException e = Assert.assertThrows(ZstdException.class, () -> {
            final byte[] decompress = Zstd.decompress(fixedBytes, compressSize(fixedBytes));
            System.out.println(new String(decompress, StandardCharsets.UTF_8));
        });
        Assert.assertEquals("Src size is incorrect", e.getMessage());
        System.err.println("Compressed and uncompressed arrays should not be mixed together.");
    }

    private static void info(byte[] bytes) {
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));
    }

    private static int compressSize(byte[] compressedBytes) {
        return (int) Math.max(compressedBytes.length, Zstd.decompressedSize(compressedBytes));
    }
}
