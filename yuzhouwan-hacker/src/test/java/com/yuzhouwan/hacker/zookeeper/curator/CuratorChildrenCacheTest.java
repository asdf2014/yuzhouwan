package com.yuzhouwan.hacker.zookeeper.curator;

import com.yuzhouwan.hacker.zookeeper.curator.CuratorChildrenCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：CuratorChildrenCache Tester
 *
 * @author Benedict Jin
 * @since 2015/12/22 0022
 */
public class CuratorChildrenCacheTest {

    private CuratorChildrenCache curatorChildrenCache;
    private final static String PATH = "children";
    private final static String SUB_PATH_1 = "children/sub1";
    private final static String SUB_PATH_2 = "children/sub2";
    private final static String DATA = "asdf";

    @Before
    public void before() throws Exception {
        curatorChildrenCache = new CuratorChildrenCache();
    }

    @After
    public void after() throws Exception {
        curatorChildrenCache = null;
    }

    /**
     * Method: addChildrenListener(String path)
     */
    @Test
    public void testAddChildrenListener() throws Exception {
        curatorChildrenCache.addChildrenListener(PATH);
        curatorChildrenCache.createNode(SUB_PATH_1);
        curatorChildrenCache.createNode(SUB_PATH_2);
        Thread.sleep(1000);
        curatorChildrenCache.deleteNode(SUB_PATH_2);
        curatorChildrenCache.updateNode(SUB_PATH_1, DATA.getBytes());
        Thread.sleep(1000);
    }
}
