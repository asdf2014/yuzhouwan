package com.yuzhouwan.site.hacker.zookeeper.curator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：CuratorDistributedLock Tester
 *
 * @author Benedict Jin
 * @since 2015/12/29 0028
 */
public class CuratorDistributedLockTest {

    private CuratorDistributedLock curatorDistributedLock;

    @Before
    public void before() throws Exception {
        curatorDistributedLock = new CuratorDistributedLock();
    }

    @After
    public void after() throws Exception {
        curatorDistributedLock = null;
    }

    /**
     * Method: noSupervene()
     */
    @Test
    public void testNoSupervene() throws Exception {
        curatorDistributedLock.noSupervene();
        Thread.sleep(1000);
    }

    /**
     * Method: supervene()
     */
    @Test
    public void testSupervene() throws Exception {
        curatorDistributedLock.supervene();
        Thread.sleep(3000);
    }

} 
