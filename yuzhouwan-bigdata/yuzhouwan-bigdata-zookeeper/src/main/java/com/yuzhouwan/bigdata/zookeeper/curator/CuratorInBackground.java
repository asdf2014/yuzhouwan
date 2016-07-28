package com.yuzhouwan.bigdata.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Curator In Background
 *
 * @author Benedict Jin
 * @since 2015/12/17 0017
 */
public class CuratorInBackground {

    private static final Logger _log = LoggerFactory.getLogger(CuratorInBackground.class);

    private CuratorFramework curatorFramework;
    private CountDownLatch countDownLatch;
    private ExecutorService executorService;

    public CuratorInBackground() {
        init();
        showCurrentThreadName();
    }

    private void init() {
        RetryPolicy retryPolicy = new RetryNTimes(3, 2000);
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(4000)
                .retryPolicy(retryPolicy)
                .namespace("background")
                .build();
        curatorFramework.start();

        countDownLatch = new CountDownLatch(2);
        executorService = Executors.newFixedThreadPool(2);
    }

    public void createTwice(String path) throws Exception {
        createEphemeralNodeRecursionInBackground(path);
        createEphemeralNodeRecursionInBackground(path);

        countDownLatch.await();
        executorService.shutdown();

        _log.info("end.");
    }

    public void createEphemeralNodeRecursionInBackground(String path) throws Exception {
        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {

                        _log.info("event's result code: {}, type: {}", event.getResultCode(), event.getType());

                        showCurrentThreadName();

                        countDownLatch.countDown();
                    }
                }).forPath(path);
    }

    private void showCurrentThreadName() {
        _log.info("Thread: {}", Thread.currentThread().getName());
    }
}
