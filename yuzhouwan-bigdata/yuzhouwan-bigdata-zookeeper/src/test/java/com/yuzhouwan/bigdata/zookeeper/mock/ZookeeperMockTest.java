package com.yuzhouwan.bigdata.zookeeper.mock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Zookeeper Mock Test
 *
 * @author Benedict Jin
 * @since 2018/11/13
 */
public class ZookeeperMockTest {

    private TestingServer server;
    private CuratorFramework client;

    @Rule
    public final TestRule timeout = new Timeout(60, TimeUnit.SECONDS);

    @Before
    public void setup() throws Exception {
        server = new TestingServer(2181, true);
        server.start();

        client = CuratorFrameworkFactory.newClient("127.0.0.1",
                new ExponentialBackoffRetry(1000, 3));
        client.start();
    }

    @Test
    public void testFoobar() throws Exception {
        String path = "/yuzhouwan";
        byte[] dataBytes = "test-data".getBytes();
        client.create().forPath(path, dataBytes);
        assertArrayEquals(dataBytes, client.getData().forPath(path));
    }

    @After
    public void teardown() throws IOException {
        try {
            if (client != null) {
                client.close();
            }
        } finally {
            if (server != null) {
                server.stop();
            }
        }
    }
}
