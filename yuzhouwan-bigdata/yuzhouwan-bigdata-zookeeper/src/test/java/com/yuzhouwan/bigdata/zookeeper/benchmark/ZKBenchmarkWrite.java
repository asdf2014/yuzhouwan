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

import java.util.concurrent.TimeUnit;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Zookeeper Benchmark Write
 *
 * @author Benedict Jin
 * @since 2017/6/1
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class ZKBenchmarkWrite {

    private final static Logger _log = LoggerFactory.getLogger(ZKBenchmarkWrite.class);
    private final static String NAMESPACE = "benchmark";
    private final static char FILL_CHAR = '0';
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

    [Tool]
        JMH
    */
    public ZKBenchmarkWrite() {
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

    private String znodePath1B;
    private byte[] jute1B;
    private String znodePath10B;
    private byte[] jute10B;
    private String znodePath100B;
    private byte[] jute100B;
    private String znodePath1KB;
    private byte[] jute1KB;
    private String znodePath10KB;
    private byte[] jute10KB;
    private String znodePath100KB;
    private byte[] jute100KB;
//    private String znodePath1000KB;
//    private byte[] jute1000KB;
    private static ZKBenchmarkWrite bench;

    /*
    Benchmark                                 Mode  Cnt    Score      Error  Units
    ZKBenchmark.dataSizeBenchmark1B          thrpt    3  313.585 ±  822.907  ops/s
    ZKBenchmark.dataSizeBenchmark10B         thrpt    3  343.275 ± 1107.436  ops/s
    ZKBenchmark.dataSizeBenchmark100B        thrpt    3  260.797 ± 1158.339  ops/s
    ZKBenchmark.dataSizeBenchmark1KB         thrpt    3  236.893 ± 1410.660  ops/s
    ZKBenchmark.dataSizeBenchmark10KB        thrpt    3  177.258 ±  895.819  ops/s
    ZKBenchmark.dataSizeBenchmark100KB       thrpt    3   38.141 ±   98.854  ops/s
    ZKBenchmark.dataSizeBenchmark1000KB      thrpt    3    8.701 ±    6.425  ops/s
     */
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ZKBenchmarkWrite.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(3)
                .threads(1)
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void initBench() throws Exception {
        String threadName = Thread.currentThread().getName();

        int size = 1;
        znodePath1B = "/node" + size + threadName;
        jute1B = new byte[size];
        for (int i = 0; i < size; i++) jute1B[i] = FILL_CHAR;

        size = 10;
        znodePath10B = "/node" + size + threadName;
        jute10B = new byte[size];
        for (int i = 0; i < size; i++) jute10B[i] = FILL_CHAR;

        size = 100;
        znodePath100B = "/node" + size + threadName;
        jute100B = new byte[size];
        for (int i = 0; i < size; i++) jute100B[i] = FILL_CHAR;

        size = 1024;
        znodePath1KB = "/node" + size + threadName;
        jute1KB = new byte[size];
        for (int i = 0; i < size; i++) jute1KB[i] = FILL_CHAR;

        size = 10 * 1024;
        znodePath10KB = "/node" + size + threadName;
        jute10KB = new byte[size];
        for (int i = 0; i < size; i++) jute10KB[i] = FILL_CHAR;

        size = 100 * 1024;
        znodePath100KB = "/node" + size + threadName;
        jute100KB = new byte[size];
        for (int i = 0; i < size; i++) jute100KB[i] = FILL_CHAR;

//        size = 1000 * 1024;
//        znodePath1000KB = "/node" + size;
//        jute1000KB = new byte[size];
//        for (int i = 0; i < size; i++) jute1000KB[i] = FILL_CHAR;

        bench = new ZKBenchmarkWrite();
        if (existNode(znodePath1B)) bench.deleteNode(znodePath1B);
        if (existNode(znodePath10B)) bench.deleteNode(znodePath10B);
        if (existNode(znodePath100B)) bench.deleteNode(znodePath100B);
        if (existNode(znodePath1KB)) bench.deleteNode(znodePath1KB);
        if (existNode(znodePath10KB)) bench.deleteNode(znodePath10KB);
        if (existNode(znodePath100KB)) bench.deleteNode(znodePath100KB);
//        if (existNode(znodePath1000KB)) bench.deleteNode(znodePath1000KB);
        if (!existNode(znodePath1B)) bench.createNode(znodePath1B);
        if (!existNode(znodePath10B)) bench.createNode(znodePath10B);
        if (!existNode(znodePath100B)) bench.createNode(znodePath100B);
        if (!existNode(znodePath1KB)) bench.createNode(znodePath1KB);
        if (!existNode(znodePath10KB)) bench.createNode(znodePath10KB);
        if (!existNode(znodePath100KB)) bench.createNode(znodePath100KB);
//        if (!existNode(znodePath1000KB)) bench.createNode(znodePath1000KB);
    }

    @Benchmark
    public void dataSize001WriteBenchmark1B() throws Exception {
        bench.writeNode(znodePath1B, jute1B);
    }

    @Benchmark
    public void dataSize002WriteBenchmark10B() throws Exception {
        bench.writeNode(znodePath10B, jute10B);
    }

    @Benchmark
    public void dataSize003WriteBenchmark100B() throws Exception {
        bench.writeNode(znodePath100B, jute100B);
    }

    @Benchmark
    public void dataSize004WriteBenchmark1KB() throws Exception {
        bench.writeNode(znodePath1KB, jute1KB);
    }

    @Benchmark
    public void dataSize005WriteBenchmark10KB() throws Exception {
        bench.writeNode(znodePath10KB, jute10KB);
    }

    @Benchmark
    public void dataSize006WriteBenchmark100KB() throws Exception {
        bench.writeNode(znodePath100KB, jute100KB);
    }

    // Too dangerous!!
//    @Benchmark
//    public void dataSize007WriteBenchmark1000KB() throws Exception {
//        bench.writeNode(znodePath1000KB, jute1000KB);
//    }

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
        bench.deleteNode(znodePath1B);
        bench.deleteNode(znodePath10B);
        bench.deleteNode(znodePath100B);
        bench.deleteNode(znodePath1KB);
        bench.deleteNode(znodePath10KB);
        bench.deleteNode(znodePath100KB);
//        bench.deleteNode(znodePath1000KB);
        close();
        close();
    }

    private void close() throws Exception {
        if (curatorFramework != null) curatorFramework.close();
        _log.info("Closed.");
    }
}
