package com.yuzhouwan.bigdata.zookeeper;

import org.junit.Test;

import static org.apache.zookeeper.server.util.ZxidUtils.makeZxid;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：ZxidUtil Test
 *
 * @author Benedict Jin
 * @since 2017/5/24
 */
public class ZxidUtilTest {

    @Test
    public void normal() {
        System.out.println(makeZxid(0, 0));        // 0
        System.out.println(makeZxid(0, 1));        // 1
        System.out.println(makeZxid(1, 0) == Math.pow(2, 32));        // true
        System.out.println(makeZxid(1, 1) == Math.pow(2, 32) + 1);    // true

        System.out.println(makeZxid(0, 0) >> 32L);    // 0
        System.out.println(makeZxid(0, 1) >> 32L);    // 1
        System.out.println(makeZxid(1, 0) >> 32L);    // 0
        System.out.println(makeZxid(1, 1) >> 32L);    // 1

        System.out.println(0xffffffffL == Math.pow(2, 32) - 1);    // true (2^31 + 2^30 + ... + 2^0)
        System.out.println(makeZxid(0, 0) & 0xffffffffL);          // 0
        System.out.println(makeZxid(0, 1) & 0xffffffffL);          // 1
        System.out.println(makeZxid(1, 0) & 0xffffffffL);          // 0
        System.out.println(makeZxid(1, 1) & 0xffffffffL);          // 1
        System.out.println((makeZxid(1, 1) & 0xffffffffL) == (makeZxid(1, 1) & 0x00000000ffffffffL));  // true

        char[] chars = Long.toBinaryString(Long.valueOf("FFFFFFFF", 16)).toCharArray();
        Long counter = 0L;
        for (int i = 0; i < chars.length; i++) {
            counter += chars[i] == '1' ? (long) Math.pow(2, i) : 0;
        }
        System.out.println(0xffffffffL == counter);     // true
    }

    @Test
    public void concurrent() {
        long zxid;
        System.out.println(zxid = ((1L << 40L) | (2L & 0xffffffffffL)));  // 1099511627778
        System.out.println((zxid >> 40L) == 1);                     // true
        System.out.println((zxid & 0xffffffffffL) == 2);            // true

        System.out.println(zxid = ((1L << 40L) | (2L & 0xffffffffffL)));  // 1099511627778
        System.out.println((zxid >> 40L) == 1);                     // true
        System.out.println(zxid = (((zxid >> 40L) + 2) << 40L));    // 3298534883328
        System.out.println((zxid >> 40L) == 3);                     // true
        System.out.println((zxid & 0xffffffffffL) == 0);            // true
    }

    @Test
    public void initZxid() {
        long zxid = ((1L << 40L) | (2L & 0xffffffffffL));
        System.out.println((zxid & 0xffffff0000000000L));
        System.out.println(((zxid & 0xffffff0000000000L) >> 40L));                  // 1
        System.out.println(((zxid & 0xffffff0000000000L) & 0xffffffffffL));         // 0
        System.out.println((zxid & 0x000000ffffffffffL));
        System.out.println(((zxid & 0x000000ffffffffffL) >> 40L));                  // 0
        System.out.println(((zxid & 0x000000ffffffffffL) & 0xffffffffffL));         // 2
        System.out.println(((zxid & 0xffffff0000000000L) | zxid));
        System.out.println(((zxid & 0xffffff0000000000L) | zxid) >> 40L);           // 1
        System.out.println(((zxid & 0xffffff0000000000L) | zxid) & 0xffffffffffL);  // 2
    }
}
