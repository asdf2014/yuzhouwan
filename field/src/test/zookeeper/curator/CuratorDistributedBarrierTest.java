package zookeeper.curator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：CuratorDistributedBarrier Tester
 *
 * @author jinjy
 * @since 2015/12/29 0029
 */
public class CuratorDistributedBarrierTest {

    private CuratorDistributedBarrier curatorDistributedBarrier;

    @Before
    public void before() throws Exception {
        curatorDistributedBarrier = new CuratorDistributedBarrier();
    }

    @After
    public void after() throws Exception {
        curatorDistributedBarrier = null;
    }

    /**
     * Method: showThreeBarrier()
     */
    @Test
    public void testShowThreeBarrier() throws Exception {
        curatorDistributedBarrier.showThreeBarrier();
        Thread.sleep(2000);
    }

    /**
     * Method: enterLeaveBarrier()
     */
    @Test
    public void testEnterLeaveBarrier() throws Exception {
        curatorDistributedBarrier.enterLeaveBarrier(3);
        Thread.sleep(1000 * 5);
    }

}
