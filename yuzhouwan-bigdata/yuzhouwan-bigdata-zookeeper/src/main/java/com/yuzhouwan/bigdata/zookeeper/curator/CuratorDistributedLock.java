package com.yuzhouwan.bigdata.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Distributed Lock with Curator
 *
 * @author Benedict Jin
 * @since 2015/12/28 0028
 */
public class CuratorDistributedLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorDistributedLock.class);

    private CuratorFramework curatorFramework;
    private SimpleDateFormat simpleDateFormat;

    public CuratorDistributedLock() {
        init();
    }

    private void init() {

        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new RetryNTimes(3, 2000))
                .namespace("distLock")
                .build();
        curatorFramework.start();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss| SSS");
    }

    public void noSupervene() {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        int count = 10;
        while (count > 0) {

            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String now = simpleDateFormat.format(new Date());
                LOGGER.info("Now time: ".concat(now));
            }).start();
            count--;
        }
        countDownLatch.countDown();
    }

    public void supervene() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final InterProcessLock interProcessLock = new InterProcessMutex(curatorFramework, "/lock");

        int count = 10;
        while (count > 0) {

            new Thread(() -> {
                try {
                    countDownLatch.await();
                    interProcessLock.acquire();
                    String now = simpleDateFormat.format(new Date());
                    LOGGER.info("Now time: ".concat(now));
                    interProcessLock.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
            count--;
        }
        countDownLatch.countDown();
    }

}
