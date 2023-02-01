package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：CollectionStuff Tester
 *
 * @author Benedict Jin
 * @since 2016/3/15
 */
public class StringTest {

    private static final Logger _log = LoggerFactory.getLogger(StringTest.class);

    @Test
    public void testSplitNothing() {

        String s1 = "1,2,3";
        String s2 = "1";
        String[] r1 = s1.split(",");
        String[] r2 = s2.split(",");
        assertEquals(3, r1.length);
        assertEquals(1, r2.length);

        assertEquals("2", r1[1]);
        assertEquals("1", r1[0]);
    }

    /*
    JVM: -ea -Xmx512M -Xms512M -Xmn128M -XX:+AlwaysPreTouch

    Time: 0.023948 ms
    Time: 0.012752 ms
      */
    @Test
    public void testStringBuilder() {
        Set<String> uniqAddr = new HashSet<>();
        StringBuilder addrs = new StringBuilder();
        long begin, performanceTime;
        {
            uniqAddr.add("a");
            uniqAddr.add("b");
            uniqAddr.add("c");
            begin = System.nanoTime();
            int count = 0, addrLen = uniqAddr.size();
            for (String addr : uniqAddr) {
                addrs.append(addr);
                count++;
                if (count != addrLen) {
                    addrs.append(", ");
                }
            }
            String withoutTail = addrs.toString();
            performanceTime = System.nanoTime() - begin;
            _log.info("Time: {} ms", performanceTime * Math.pow(10, -6));
            assertEquals("a, b, c", withoutTail);
        }
        {
            addrs = new StringBuilder();
            uniqAddr.clear();
            uniqAddr.add("a");
            uniqAddr.add("b");
            uniqAddr.add("c");
            begin = System.nanoTime();
            for (String addr : uniqAddr) {
                addrs.append(addr).append(", ");
            }
            String withoutTail = uniqAddr.size() > 0 ? addrs.substring(0, addrs.length() - 2) : addrs.toString();
            performanceTime = System.nanoTime() - begin;
            _log.info("Time: {} ms", performanceTime * Math.pow(10, -6));
            assertEquals("a, b, c", withoutTail);
            assertEquals("a, b, c", uniqAddr.size() > 0 ?
                    addrs.delete(addrs.length() - 2, addrs.length()).toString() : addrs.toString());
        }
    }
}