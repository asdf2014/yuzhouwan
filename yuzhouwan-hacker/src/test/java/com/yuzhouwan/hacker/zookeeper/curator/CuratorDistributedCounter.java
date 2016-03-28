package com.yuzhouwan.hacker.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：Distributed Counter with Curator
 *
 * @author Benedict Jin
 * @since 2015/12/29 0029
 */
public class CuratorDistributedCounter {

    private final static Logger _log = LoggerFactory.getLogger(CuratorDistributedCounter.class);
    private CuratorFramework curatorFramework;
    private DistributedAtomicInteger distributedAtomicInteger;

    private void init() {
        try {
            _log.debug("init...");
            RetryNTimes retryPolicy = new RetryNTimes(3, 2000);
            curatorFramework = CuratorFrameworkFactory
                    .builder()
                    .connectString("localhost:2181")
                    .connectionTimeoutMs(3000)
                    .sessionTimeoutMs(5000)
                    .retryPolicy(retryPolicy)
                    .namespace("distCounter")
                    .build();
            curatorFramework.start();
            distributedAtomicInteger = new DistributedAtomicInteger(curatorFramework, "/atomic", retryPolicy);
        } catch (Exception e) {
        }
    }

    public CuratorDistributedCounter() throws Exception {
        try {
            init();
            _log.debug("Set atomic integer as zero.");
            distributedAtomicInteger.trySet(0);
        } catch (Exception e) {
        }
    }

    public int addAtomicInteger(int addNum) throws Exception {
        try {
            _log.info("add {} ...", addNum);
            AtomicValue<Integer> atomicValue = distributedAtomicInteger.add(addNum);
            return atomicValue.postValue();
        } catch (Exception e) {
        }
        return 0;
    }

}
