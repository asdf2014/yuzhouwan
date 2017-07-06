package com.yuzhouwan.bigdata.zookeeper.benchmark;

import com.yuzhouwan.common.util.ThreadUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

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
                .connectString("10.27.129.60:2181,10.27.129.60:2182,10.27.129.60:2183")
                .connectionTimeoutMs(500)
                .sessionTimeoutMs(1000)
                .retryPolicy(new ExponentialBackoffRetry(100, 3))
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
    JVM: -ea -Xmx512M -Xms512M -Xmn256M -XX:+AlwaysPreTouch

    [DataSize: 0.001kb, MaxTime: 9ms, MinTime: 1ms, AvgTime: 4ms]
    [DataSize: 0.01kb, MaxTime: 43ms, MinTime: 2ms, AvgTime: 11ms]
    [DataSize: 0.1kb, MaxTime: 4ms, MinTime: 2ms, AvgTime: 3ms]
    [DataSize: 1.0kb, MaxTime: 5ms, MinTime: 2ms, AvgTime: 3ms]
    [DataSize: 10.0kb, MaxTime: 11ms, MinTime: 3ms, AvgTime: 6ms]
    [DataSize: 100.0kb, MaxTime: 284ms, MinTime: 13ms, AvgTime: 53ms]
    [DataSize: 1000.0kb, MaxTime: 228ms, MinTime: 107ms, AvgTime: 161ms]
     */
//    @Test
    public void dataSizeTest() throws Exception {
        double dataSize = 1024;
        for (int i = -3; i < 4; i++) {
            dataSize(dataSize * Math.pow(10, i));
        }
    }

    //    @Test
    public void reConn() throws Exception {
        ExecutorService es = ThreadUtils.buildExecutorService(10, "reConn");
        int count = 10;
        while (count > 0) {
            System.out.println(count);

            es.execute(() -> {
                try {
                    init();
                    Thread.sleep(1000);
                    close();
                } catch (Exception ignored) {
                }
            });
            count--;
        }
        es.shutdown();
        while (!es.isTerminated()) {
            Thread.sleep(1000);
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

    @After
    public void release() throws Exception {
        close();
    }

    private void close() throws Exception {
        if (curatorFramework != null) curatorFramework.close();
        _log.info("Closed.");
    }
}
