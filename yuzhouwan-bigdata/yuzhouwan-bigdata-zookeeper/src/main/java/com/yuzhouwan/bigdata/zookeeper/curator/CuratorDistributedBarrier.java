package com.yuzhouwan.bigdata.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Distributed Barrier with Curator
 *
 * @author Benedict Jin
 * @since 2015/12/29 0029
 */
public class CuratorDistributedBarrier {

    private final static Logger _log = LoggerFactory.getLogger(CuratorDistributedBarrier.class);
    private DistributedBarrier distributedBarrier;
    private DistributedDoubleBarrier distributedDoubleBarrier;

    public CuratorDistributedBarrier() throws Exception {
        init();
    }

    private void init() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(3000)
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryNTimes(3, 2000))
                .namespace("distBarrier")
                .build();
        curatorFramework.start();
        distributedBarrier = new DistributedBarrier(curatorFramework, "/barrier");

//        try {
//            Stat stat = curatorFramework.checkExists().forPath("/double");
//            if (stat != null)
//                curatorFramework.delete().deletingChildrenIfNeeded().forPath("/double");
//            else
//                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/double");
//        } catch (Exception e) {
//            throw new RuntimeException("Cannot create path '/double' !!", e);
//        }
        distributedDoubleBarrier = new DistributedDoubleBarrier(curatorFramework, "/double", 3);
    }

    public void enterLeaveBarrier(int count) throws Exception {

        while (count > 0) {
            new Thread() {
                @Override
                public void run() {
                    Thread t = Thread.currentThread();
                    String threadName = t.getName();
                    System.out.println(threadName + " is ready...");
                    try {
                        /**
                         * Exception in thread "Thread-1" Exception in thread "Thread-3"
                         * java.lang.RuntimeException: org.apache.zookeeper.KeeperException$NodeExistsException:
                         *
                         * KeeperErrorCode = NodeExists for /distBarrier/double/59686981-2dd5-4a74-a990-e99046b08a58
                         */
                        distributedDoubleBarrier.enter();
                        System.out.println(threadName + " is entering...");
                        distributedDoubleBarrier.leave();
                        System.out.println(threadName + " is leaving...");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();
            count--;
        }
        Thread.sleep(2000);
    }

    public void showThreeBarrier() throws Exception {

        int count = 3;
        while (count > 0) {

            final int finalCount = count;
            new Thread() {
                @Override
                public void run() {
                    try {
                        _log.info("set {}...", finalCount);
                        distributedBarrier.setBarrier();
                        distributedBarrier.waitOnBarrier();
                        _log.info("start {}...", finalCount);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();
            count--;
        }
        /**
         * keep a while for waiting other barriers
         */
        Thread.sleep(2000);
        distributedBarrier.removeBarrier();
    }

}
