package com.yuzhouwan.site.service.restful;

import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Copyright @ 2019 yuzhouwan.com
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
        assertNull(chm.get(""));
        assertNull(chm.put("a", "a"));
        assertEquals("a", chm.put("a", "b"));
        assertEquals("b", chm.put("a", "c"));
        assertEquals("c", chm.remove("a"));
    }
}
