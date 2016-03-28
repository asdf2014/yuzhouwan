package com.yuzhouwan.hacker.zookeeper.curator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：CuratorNodeCache Tester
 *
 * @author Benedict Jin
 * @since 2015/12/22 0022
 */
public class CuratorNodeCacheTest {

    private CuratorNodeCache curatorNodeCache;
    private final static String PATH = "watchedNode";
    private final static String DATA = "1234";

    @Before
    public void before() throws Exception {
        curatorNodeCache = new CuratorNodeCache();
    }

    @After
    public void after() throws Exception {
        curatorNodeCache = null;
    }

    /**
     * Method: addNodeCacheListener(String path)
     */
    @Test
    public void testAddNodeCacheListener() throws Exception {
        curatorNodeCache.addNodeCacheListener(PATH);
        curatorNodeCache.setData(PATH, DATA.getBytes());
        Thread.sleep(1000);
        assertEquals(DATA, curatorNodeCache.getData(PATH));
    }

}
