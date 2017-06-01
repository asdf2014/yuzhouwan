package com.yuzhouwan.bigdata.zookeeper.benchmark;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Zookeeper Benchmark
 *
 * @author Benedict Jin
 * @since 2017/6/1
 */
public class ZKBenchmark {

    private final static Logger _log = LoggerFactory.getLogger(ZKBenchmark.class);
    private final static char FILL_CHAR = '0';
    private final static int TEST_TIMES = 10;
    private CuratorFramework curatorFramework;

    /*
    [Client]
        operation:    C      D       R       W
        data size:    1B     10B     100B    1KB    10KB    100KB   1000KB
        ops      :    1      10      100     1k      10k
        watch num:    1      10      100     1k      10k
        concurrent:   1      5       10      100     1k

    [Server]
        cluster node: 1      3       5       7       9
        znode num:    1      10      100     1k      10k

    [Config]
        rpc type:     nio    netty
        init/sync:    5/2    10/4    20/8    30/16
    */
    public ZKBenchmark() throws Exception {
        init();
    }

    private void init() throws Exception {
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(10000)
                .retryPolicy(new ExponentialBackoffRetry(2000, 3))
                .namespace("benchmark")
                .build();
        _log.debug("Initialized.");
        curatorFramework.start();
        _log.debug("Started.");
    }

    private boolean existNode(String path) throws Exception {
        return curatorFramework.checkExists().forPath(path) != null;
    }

    private void createNode(String path) throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
        _log.debug("Created ".concat(path));
    }

    private void deleteNode(String path) throws Exception {
        curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        _log.debug("Deleted ".concat(path));
    }

    private String readNode(String path) throws Exception {
        return new String(curatorFramework.getData().forPath(path));
    }

    private void writeNode(String path, byte[] data) throws Exception {
        curatorFramework.setData().forPath(path, data);
        _log.debug("Updated ".concat(path));
    }

    private long fillNode(ZKBenchmark ccc, int len, String path) throws Exception {
        byte[] jute = new byte[len];
        for (int i = 0; i < len; i++) jute[i] = FILL_CHAR;
        long startTime = System.currentTimeMillis();
        ccc.writeNode(path, jute);
        return System.currentTimeMillis() - startTime;
    }

    /*
    JVM: -ea -Xmx1024M -Xms1024M -Xmn256M -XX:+AlwaysPreTouch

    [DataSize: 0.001kb, MaxTime: 99ms, MinTime: 21ms, AvgTime: 50ms]
    [DataSize: 0.01kb, MaxTime: 75ms, MinTime: 22ms, AvgTime: 49ms]
    [DataSize: 0.1kb, MaxTime: 66ms, MinTime: 20ms, AvgTime: 43ms]
    [DataSize: 1.0kb, MaxTime: 31ms, MinTime: 23ms, AvgTime: 31ms]
    [DataSize: 10.0kb, MaxTime: 156ms, MinTime: 25ms, AvgTime: 67ms]
    [DataSize: 100.0kb, MaxTime: 62ms, MinTime: 29ms, AvgTime: 49ms]
    [DataSize: 1000.0kb, MaxTime: 219ms, MinTime: 140ms, AvgTime: 202ms]
     */
    @Test
    public void dataSizeTest() throws Exception {
        double dataSize = 1024;
        for (int i = -3; i < 4; i++) {
            dataSize(dataSize * Math.pow(10, i));
        }
    }

    private void dataSize(double dataSize) throws Exception {
        long usedTime, totalTime = 0L;
        List<Long> time = new LinkedList<>();

        ZKBenchmark bench = new ZKBenchmark();
        int size = (int) dataSize;
        String znodePath = "/node" + size;
        for (int i = 0; i < TEST_TIMES; i++) {
            znodePath = znodePath + i;
            if (!existNode(znodePath)) bench.createNode(znodePath);

            usedTime = fillNode(bench, size, znodePath);

            assertEquals(size, bench.readNode(znodePath).length());
            bench.deleteNode(znodePath);
            time.add(usedTime);
            Thread.sleep(1000);
        }

        Collections.sort(time);
        for (Long t : time) totalTime += t;
        int timeLen = time.size();
        _log.info("[DataSize: {}kb, MaxTime: {}ms, MinTime: {}ms, AvgTime: {}ms]",
                dataSize / 1024D, time.remove(timeLen - 1), time.remove(0), totalTime / (timeLen - 2));
        time.clear();
    }
}
