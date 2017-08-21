package com.yuzhouwan.bigdata.zookeeper.benchmark;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Zookeeper Benchmark Watch
 *
 * @author Benedict Jin
 * @since 2017/6/1
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class ZKBenchmarkWatch {

    private final static Logger _log = LoggerFactory.getLogger(ZKBenchmarkWatch.class);
    private final static String NAMESPACE = "benchmark";
    private final static String WATCH_PREFIX = "/watch";
    private final static char FILL_CHAR = '0';
    private CuratorFramework curatorFramework;

    /*
    [Client]
        operation:    C      D       R       W
        data size:    1B     10B     100B    1KB     10KB    100KB   [1000KB]
        ops      :    1      10      100     1k      10k
        watch num:    1      10      100     1k      10k     100KB
        concurrent:   1      5       10      100     1k

    [Server]
        cluster node:       1      3       5       7       9
        child znode num:    1      10      100     1k      10k     100KB

    [Config]
        rpc type:     nio    netty
        init/sync:    5/2    10/4    20/8    30/16

    [Tool]
        JMH
    */
    public ZKBenchmarkWatch() {
        try {
            init();
        } catch (Exception ignored) {
        }
    }

    private void init() {
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(500)
                .sessionTimeoutMs(1000)
                .retryPolicy(new ExponentialBackoffRetry(100, 3))
                .namespace(NAMESPACE)
                .build();
        _log.debug("Initialized.");
        curatorFramework.start();
        _log.debug("Started.");
    }

    private String znodePath10KB;
    private LinkedList<String> znodeWatchNodes = new LinkedList<>();
    private byte[] jute10KB;
    private static ZKBenchmarkWatch bench;

    /*
    Benchmark                                             Mode  Cnt    Score      Error  Units
    # 1 child
    ZKBenchmarkWatch.dataSize001WatchWriteBenchmark10KB  thrpt    3  176.419 ± 1063.429  ops/s
    ZKBenchmarkWatch.dataSize002WatchReadBenchmark10KB   thrpt    3  377.952 ± 1051.229  ops/s

    # 10 children
    ZKBenchmarkWatch.dataSize001WatchWriteBenchmark10KB  thrpt    3  168.674 ±  551.438  ops/s
    ZKBenchmarkWatch.dataSize002WatchReadBenchmark10KB   thrpt    3  396.244 ± 1738.852  ops/s

    # 100 children
    ZKBenchmarkWatch.dataSize001WatchWriteBenchmark10KB  thrpt    3  220.910 ±  402.890  ops/s
    ZKBenchmarkWatch.dataSize002WatchReadBenchmark10KB   thrpt    3  371.567 ± 2556.598  ops/s

    # 1k children
    ZKBenchmarkWatch.dataSize001WatchWriteBenchmark10KB  thrpt    3  131.748 ± 1261.011  ops/s
    ZKBenchmarkWatch.dataSize002WatchReadBenchmark10KB   thrpt    3  400.540 ±   51.629  ops/s

    # 10k children
    ZKBenchmarkWatch.dataSize001WatchWriteBenchmark10KB  thrpt    3  122.966 ± 568.570  ops/s
    ZKBenchmarkWatch.dataSize002WatchReadBenchmark10KB   thrpt    3  412.240 ± 266.844  ops/s

    # 100KB children
    # Too long!!
    ZKBenchmarkWatch.dataSize001WatchWriteBenchmark10KB  thrpt    3  171.022 ±  829.324  ops/s
    ZKBenchmarkWatch.dataSize002WatchReadBenchmark10KB   thrpt    3  449.105 ± 1169.032  ops/s
     */
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ZKBenchmarkWatch.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(3)
                .threads(1)
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void initBench() throws Exception {
        int size = 10 * 1024;
        znodePath10KB = "/node" + size;
        jute10KB = new byte[size];
        for (int i = 0; i < size; i++) jute10KB[i] = FILL_CHAR;

        bench = new ZKBenchmarkWatch();
        if (existNode(znodePath10KB)) bench.deleteNode(znodePath10KB);
        if (!existNode(znodePath10KB)) bench.createNode(znodePath10KB);
        bench.writeNode(znodePath10KB, jute10KB);

        int watchSize = 100;
        String watchNode;
        for (int i = 0; i < watchSize; i++) {
            watchNode = WATCH_PREFIX + i;
            if (existNode(watchNode)) bench.deleteNode(watchNode);
            if (!existNode(watchNode)) bench.createNode(watchNode);
            znodeWatchNodes.add(watchNode);
            bench.watchNode(watchNode);
        }
    }

    @Benchmark
    public void dataSize001ChildWriteBenchmark10KB() throws Exception {
        bench.writeNode(znodePath10KB, jute10KB);
    }

    @Benchmark
    public void dataSize002ChildReadBenchmark10KB() throws Exception {
        bench.readNode(znodePath10KB);
    }

    private boolean watchNode(String path) throws Exception {
        return curatorFramework.getData().watched().inBackground().forPath(path) != null;
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

    @TearDown
    public void tearDown() throws Exception {
        bench.deleteNode(znodePath10KB);
        for (String znodeWatchNode : znodeWatchNodes) bench.deleteNode(znodeWatchNode);
        znodeWatchNodes.clear();
        close();
    }

    private void close() throws Exception {
        if (curatorFramework != null) curatorFramework.close();
        _log.info("Closed.");
    }
}
