package zookeeper.curator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：CuratorDistributedCounter Tester
 *
 * @author jinjy
 * @since 2015/12/29 0029
 */
public class CuratorDistributedCounterTest {

    private CuratorDistributedCounter curatorDistributedCounter;

    @Before
    public void before() throws Exception {
        curatorDistributedCounter = new CuratorDistributedCounter();
    }

    @After
    public void after() throws Exception {
        curatorDistributedCounter = null;
    }

    /**
     * Method: addAtomicInteger(int addNum)
     */
    @Test
    public void testAddAtomicInteger() throws Exception {
        assertEquals(100, curatorDistributedCounter.addAtomicInteger(100));
    }

}
