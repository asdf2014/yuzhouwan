package zookeeper.paxos2zk;

import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 zhong-ying Co.Ltd
 * All right reserved.
 * Function: ZooKeeperConnPool Tester
 *
 * @author jinjy
 * @since 2015/12/8 0008
 */
public class ZookeeperConnPoolTestFreeConnNormal {

    @Test
    public void testFreeConnNormal() throws InterruptedException {
        ZooKeeperConnPool zookeeperConnPool = ZooKeeperConnPool.getInstance();

        ZooKeeper zooKeeper = zookeeperConnPool.getConn();
        isAlive(zooKeeper);
        zookeeperConnPool.freeConn(zooKeeper);
        isAlive(zooKeeper);
    }

    private void isAlive(ZooKeeper zooKeeper) {
        assertEquals(true, zooKeeper.getState().isAlive());
    }
}
