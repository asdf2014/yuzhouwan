package com.yuzhouwan.bigdata.zookeeper.curator;

import com.yuzhouwan.common.util.TimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Curator Node Cache Test
 *
 * @author Benedict Jin
 * @since 2017/8/8
 */
public class CuratorNodeCacheTest {

    private static final String path = "/watch";
    private static final int millis = 100;
    private CuratorNodeCache cache;

    @Disabled
    @Before
    public void before() {
        cache = new CuratorNodeCache();
    }

    @Disabled
    @Test
    public void testWatch() throws Exception {
        cache.addNodeCacheListener(path);
        String now = TimeUtils.nowStr();
        cache.setData(path, now.getBytes());
        assertEquals(now, cache.getData(path));
        cache.setData(path, "1".getBytes());
        Thread.sleep(millis);
        cache.setData(path, "2".getBytes());
        Thread.sleep(millis);
        cache.setData(path, "3".getBytes());
        Thread.sleep(millis);
    }

    @Disabled
    @After
    public void after() throws Exception {
        assertTrue(cache.delPath(path));
    }
}
