package com.yuzhouwan.hacker.algorithms;

import com.yuzhouwan.hacker.algorithms.array.JosephRing;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Joseph Ring Tester
 *
 * @author Benedict Jin
 * @since 2016/8/31
 */
public class JosephRingTest {

    @Test
    public void joseph() throws Exception {
        List<Integer> origin = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            origin.add(i);
        }
        StringBuilder stringBuilder = new StringBuilder();
        new JosephRing().joseph(origin, 3).forEach(e -> stringBuilder.append(e).append(" "));
        assertEquals("3 6 9 2 7 1 8 5 10 4", stringBuilder.toString().trim());
    }
}
