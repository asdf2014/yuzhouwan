package com.yuzhouwan.bigdata.zookeeper.zxid;

import org.apache.zookeeper.client.StaticHostProvider;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import static org.apache.zookeeper.server.util.ZxidUtils.makeZxid;

/**
 * Copyright @ 2020 yuzhouwan.com
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
        long counter = 0L;
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

    @Test
    public void testShuffle() throws Exception {
        LinkedList<InetSocketAddress> inetSocketAddressesList = new LinkedList<>();
        inetSocketAddressesList.add(new InetSocketAddress(0));
        inetSocketAddressesList.add(new InetSocketAddress(1));
        inetSocketAddressesList.add(new InetSocketAddress(2));
        /*
        1442045361
        currentTime: 1499253530044, currentTime ^ hashCode: 1500143845389, Result: 1 2 0
        currentTime: 1499253530044, currentTime ^ hashCode: 1500143845389, Result: 2 0 1
        currentTime: 1499253530045, currentTime ^ hashCode: 1500143845388, Result: 0 1 2
        currentTime: 1499253530045, currentTime ^ hashCode: 1500143845388, Result: 1 2 0
        currentTime: 1499253530046, currentTime ^ hashCode: 1500143845391, Result: 1 2 0
        currentTime: 1499253530046, currentTime ^ hashCode: 1500143845391, Result: 1 2 0
        currentTime: 1499253530046, currentTime ^ hashCode: 1500143845391, Result: 1 2 0
        currentTime: 1499253530046, currentTime ^ hashCode: 1500143845391, Result: 1 2 0
        currentTime: 1499253530047, currentTime ^ hashCode: 1500143845390, Result: 1 2 0
        currentTime: 1499253530047, currentTime ^ hashCode: 1500143845390, Result: 1 2 0
         */
        internalShuffleMillis(inetSocketAddressesList);
        /*
        146611050
        currentTime: 22618159623770, currentTime ^ hashCode: 22618302559536, Result: 2 1 0
        currentTime: 22618159800738, currentTime ^ hashCode: 22618302085832, Result: 0 1 2
        currentTime: 22618159967442, currentTime ^ hashCode: 22618302248888, Result: 1 0 2
        currentTime: 22618160135080, currentTime ^ hashCode: 22618302013634, Result: 2 1 0
        currentTime: 22618160302095, currentTime ^ hashCode: 22618301535077, Result: 2 1 0
        currentTime: 22618160490260, currentTime ^ hashCode: 22618301725822, Result: 1 0 2
        currentTime: 22618161566373, currentTime ^ hashCode: 22618300303823, Result: 1 0 2
        currentTime: 22618161745518, currentTime ^ hashCode: 22618300355844, Result: 2 1 0
        currentTime: 22618161910357, currentTime ^ hashCode: 22618291603775, Result: 2 1 0
        currentTime: 22618162079549, currentTime ^ hashCode: 22618291387479, Result: 0 1 2
         */
        internalShuffleNano(inetSocketAddressesList);

        inetSocketAddressesList.clear();
        inetSocketAddressesList.add(new InetSocketAddress(0));
        inetSocketAddressesList.add(new InetSocketAddress(1));

        /*
        415138788
        currentTime: 1499253530050, currentTime ^ hashCode: 1499124456998, Result: 0 1
        currentTime: 1499253530050, currentTime ^ hashCode: 1499124456998, Result: 0 1
        currentTime: 1499253530050, currentTime ^ hashCode: 1499124456998, Result: 0 1
        currentTime: 1499253530050, currentTime ^ hashCode: 1499124456998, Result: 0 1
        currentTime: 1499253530050, currentTime ^ hashCode: 1499124456998, Result: 0 1
        currentTime: 1499253530050, currentTime ^ hashCode: 1499124456998, Result: 0 1
        currentTime: 1499253530053, currentTime ^ hashCode: 1499124456993, Result: 0 1
        currentTime: 1499253530055, currentTime ^ hashCode: 1499124456995, Result: 0 1
        currentTime: 1499253530055, currentTime ^ hashCode: 1499124456995, Result: 0 1
        currentTime: 1499253530055, currentTime ^ hashCode: 1499124456995, Result: 0 1
         */
        internalShuffleMillis(inetSocketAddressesList);
        /*
        13326370
        currentTime: 22618168292396, currentTime ^ hashCode: 22618156149774, Result: 1 0
        currentTime: 22618168416181, currentTime ^ hashCode: 22618156535703, Result: 1 0
        currentTime: 22618168534056, currentTime ^ hashCode: 22618156432394, Result: 0 1
        currentTime: 22618168666548, currentTime ^ hashCode: 22618155774358, Result: 0 1
        currentTime: 22618168818946, currentTime ^ hashCode: 22618155623712, Result: 0 1
        currentTime: 22618168936821, currentTime ^ hashCode: 22618156011863, Result: 1 0
        currentTime: 22618169056251, currentTime ^ hashCode: 22618155893721, Result: 1 0
        currentTime: 22618169611103, currentTime ^ hashCode: 22618157370237, Result: 1 0
        currentTime: 22618169744528, currentTime ^ hashCode: 22618156713138, Result: 1 0
        currentTime: 22618171273170, currentTime ^ hashCode: 22618184562672, Result: 1 0
         */
        internalShuffleNano(inetSocketAddressesList);
    }

    private void internalShuffleMillis(LinkedList<InetSocketAddress> inetSocketAddressesList) throws Exception {
        int hashCode = new StaticHostProvider(inetSocketAddressesList).hashCode();
        System.out.println(hashCode);
        int count = 10;
        Random r;
        while (count > 0) {
            long currentTime = System.currentTimeMillis();
            r = new Random(currentTime ^ hashCode);
            System.out.print(String.format("currentTime: %s, currentTime ^ hashCode: %s, Result: ",
                    currentTime, currentTime ^ hashCode));
            Collections.shuffle(inetSocketAddressesList, r);
            for (InetSocketAddress inetSocketAddress : inetSocketAddressesList) {
                System.out.print(String.format("%s ", inetSocketAddress.getPort()));
            }
            System.out.println();
            count--;
        }
    }

    private void internalShuffleNano(LinkedList<InetSocketAddress> inetSocketAddressesList) throws Exception {
        int hashCode = new StaticHostProvider(inetSocketAddressesList).hashCode();
        System.out.println(hashCode);
        int count = 10;
        Random r;
        while (count > 0) {
            long currentTime = System.nanoTime();
            r = new Random(currentTime ^ hashCode);
            System.out.print(String.format("currentTime: %s, currentTime ^ hashCode: %s, Result: ",
                    currentTime, currentTime ^ hashCode));
            Collections.shuffle(inetSocketAddressesList, r);
            for (InetSocketAddress inetSocketAddress : inetSocketAddressesList) {
                System.out.print(String.format("%s ", inetSocketAddress.getPort()));
            }
            System.out.println();
            count--;
        }
    }
}
