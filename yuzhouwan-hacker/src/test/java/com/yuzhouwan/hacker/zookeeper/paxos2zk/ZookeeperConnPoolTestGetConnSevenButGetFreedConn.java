package com.yuzhouwan.hacker.zookeeper.paxos2zk;

import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: ZooKeeperConnPool Tester
 *
 * @author Benedict Jin
 * @since 2015/12/8 0008
 */
public class ZookeeperConnPoolTestGetConnSevenButGetFreedConn {

    @Test
    public void testGetConnSevenButGetFreedConn() throws InterruptedException {
        ZooKeeperConnPool zookeeperConnPool = ZooKeeperConnPool.getInstance();
        {
            ZooKeeper zooKeeper1 = zookeeperConnPool.getConn();
            isAlive(zooKeeper1);
        }
        Thread.sleep(1000);
        {
            ZooKeeper zooKeeper2 = zookeeperConnPool.getConn();
            isAlive(zooKeeper2);
        }
        Thread.sleep(1000);
        {
            ZooKeeper zooKeeper3 = zookeeperConnPool.getConn();
            isAlive(zooKeeper3);
        }
        Thread.sleep(1000);
        {
            ZooKeeper zooKeeper4 = zookeeperConnPool.getConn();
            isAlive(zooKeeper4);
        }
        Thread.sleep(1000);
        ZooKeeper zooKeeper5 = zookeeperConnPool.getConn();
        isAlive(zooKeeper5);
        Thread.sleep(1000);
        {
            ZooKeeper zooKeeper6 = zookeeperConnPool.getConn();
            assertEquals(null, zooKeeper6);
        }
        {
            zookeeperConnPool.freeConn(zooKeeper5);
            isAlive(zooKeeper5);
            ZooKeeper zooKeeper7 = zookeeperConnPool.getConn();
            isAlive(zooKeeper7);
        }
    }

    private void isAlive(ZooKeeper zooKeeper) {
        assertEquals(true, zooKeeper.getState().isAlive());
    }
}
