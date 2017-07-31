package com.yuzhouwan.hacker.algorithms.collection;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：CollectionStuff Tester
 *
 * @author Benedict Jin
 * @since 2016/3/15
 */
public class StringTest {

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

    @Test
    public void testStringBuilder() {
        Set<String> uniqAddr = new HashSet<>();
        StringBuilder addrs = new StringBuilder();
        uniqAddr.add("a");
        uniqAddr.add("b");
        uniqAddr.add("c");
        int count = 0, addrLen = uniqAddr.size();
        for (String addr : uniqAddr) {
            addrs.append(addr);
            count++;
            if (count != addrLen) {
                addrs.append(", ");
            }
        }
        assertEquals("a, b, c", addrs.toString());
    }
}