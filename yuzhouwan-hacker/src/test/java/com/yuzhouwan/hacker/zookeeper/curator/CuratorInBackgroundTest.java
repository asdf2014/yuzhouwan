package com.yuzhouwan.hacker.zookeeper.curator;

import com.yuzhouwan.hacker.zookeeper.curator.CuratorInBackground;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: CuratorInBackground Tester
 *
 * @author Benedict Jin
 * @since 2015/12/17 0017
 */
public class CuratorInBackgroundTest {

    private CuratorInBackground curatorInBackground;

    @Before
    public void before() throws Exception {
        curatorInBackground = new CuratorInBackground();
    }

    @After
    public void after() throws Exception {
        curatorInBackground = null;
    }

    /**
     * Method: createTwice(String path)
     */
    @Test
    public void testCreateTwice() throws Exception {
        curatorInBackground.createTwice("inBackground");
    }
}
