package com.yuzhouwan.hacker.zookeeper.curator;

import com.yuzhouwan.hacker.zookeeper.curator.CuratorLeaderSelector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：CuratorLeaderSelector Tester
 *
 * @author Benedict Jin
 * @since 2015/12/23 0023
 */
public class CuratorLeaderSelectorTest {

    private CuratorLeaderSelector curatorLeaderSelector;

    private final static String SELECTOR_PATH = "/selector";

    @Before
    public void before() throws Exception {
        curatorLeaderSelector = new CuratorLeaderSelector();
    }

    @After
    public void after() throws Exception {
        curatorLeaderSelector = null;
    }

    /**
     * Method: leaderSelector(final String path)
     */
    @Test
    public void testLeaderSelector() throws Exception {
        curatorLeaderSelector.leaderSelector(SELECTOR_PATH);
        Thread.sleep(1000);
    }

} 
