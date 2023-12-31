package com.yuzhouwan.hacker.effective;

import org.apache.avro.util.ByteBufferInputStream;

import java.io.IOException;
import java.util.Collections;

import static com.yuzhouwan.common.util.DecimalUtils.byteArray2byteBuffer;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Stream Close
 *
 * @author Benedict Jin
 * @since 2017/3/23
 */
public class StreamClose {

    public static void main(String[] args) throws Exception {
        int count = 1000_0000;
        while (count > 0) {
            streamRelease(count);
            count--;
            if (count % 100_0000 == 0) {
                System.out.println("JMAP");
                Thread.sleep(1000);
            }
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static void streamRelease(int count) throws IOException {
        try (ByteBufferInputStream bb = new ByteBufferInputStream(Collections.singletonList(
                byteArray2byteBuffer((count + "\"VM Thread\" os_prio=2 tid=0x00000000177bf000 nid=0x24f4 runnable"
                ).getBytes())))) {
            bb.readBuffer(1000);
        }
    }
}
