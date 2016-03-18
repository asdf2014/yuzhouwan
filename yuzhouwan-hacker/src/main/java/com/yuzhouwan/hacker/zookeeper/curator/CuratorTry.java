package com.yuzhouwan.hacker.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: zookeeper.curator
 *
 * @author Benedict Jin
 * @since 2015/12/16 0016
 */
public class CuratorTry {

    private static final String zkHost = "127.0.0.1:2181";
    private CuratorFramework curatorClient;

    private void init() {

        RetryPolicy retrypolicy = new ExponentialBackoffRetry(3000, 60);

        /**
         * using fluent api
         */
        curatorClient = CuratorFrameworkFactory.builder()
                .connectString(zkHost)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(retrypolicy)
                .namespace("watcher")       // root path: /watcher
                .build();
        curatorClient.start();
    }

    public CuratorTry() {
        init();
    }

    /**
     * create
     *
     * @param path
     * @throws Exception
     */
    public void create(String path) throws Exception {
        curatorClient.create().forPath(path);
    }

    public void createWithData(String path, byte[] data) throws Exception {
        curatorClient.create().forPath(path, data);
    }

    public void createPersistentNode(String path, byte[] data) throws Exception {
        curatorClient.create().withMode(CreateMode.PERSISTENT).forPath(path, data);
    }

    public void createEphemeralNodeRecursion(String path, byte[] data) throws Exception {
        curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data);
    }

    /**
     * delete
     *
     * @param path
     */
    public void delete(String path) throws Exception {
        curatorClient.delete().forPath(path);
    }

    public void deleteRecursion(String path) throws Exception {
        curatorClient.delete().deletingChildrenIfNeeded().forPath(path);
    }

    public void deleteRecursionWithVersion(String path, int version) throws Exception {
        curatorClient.delete().deletingChildrenIfNeeded().withVersion(version).forPath(path);
    }

    public void deleteRecursionGuaranteedWithVersion(String path, int version) throws Exception {
        curatorClient.delete().guaranteed().deletingChildrenIfNeeded().withVersion(version).forPath(path);
    }

    /**
     * update
     *
     * @param path
     * @throws Exception
     */
    public void update(String path) throws Exception {
        curatorClient.setData().forPath(path);
    }

    public void updateWithVersion(String path, int version) throws Exception {
        curatorClient.setData().withVersion(version).forPath(path);
    }

    /**
     * read
     *
     * @param path
     * @return
     * @throws Exception
     */
    public String read(String path) throws Exception {
        byte[] data = curatorClient.getData().forPath(path);
        return new String(data);
    }

    public String readWithVersion(String path, int version) throws Exception {
//        curatorClient.getData().forPath(path);
        throw new RuntimeException("Not support?");
    }

}
