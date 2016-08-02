package com.yuzhouwan.site.restful;

import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: FullRestMethod Tester
 *
 * @author Benedict Jin
 * @since 2016/8/2
 */
public class FullRestMethodTest {

    @Test
    public void concurrentHashMap() throws Exception {
        ConcurrentHashMap<String, String> chm = new ConcurrentHashMap<>();
        assertEquals(null, chm.get(""));
        assertEquals(null, chm.put("a", "a"));
        assertEquals("a", chm.put("a", "b"));
        assertEquals("b", chm.put("a", "c"));
        assertEquals("c", chm.remove("a"));
    }
}
