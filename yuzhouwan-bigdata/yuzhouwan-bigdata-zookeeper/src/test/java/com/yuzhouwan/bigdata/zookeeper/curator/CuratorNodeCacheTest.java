package com.yuzhouwan.bigdata.zookeeper.curator;

import com.yuzhouwan.common.util.TimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 suning.com
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

    @Before
    public void before() throws Exception {
        cache = new CuratorNodeCache();
    }

//    @Test
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

    @After
    public void after() throws Exception {
        assertEquals(true, cache.delPath(path));
    }
}
