package com.yuzhouwan.bigdata.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Watch Node Cache's change
 *
 * @author Benedict Jin
 * @since 2015/12/22 0022
 */
public class CuratorNodeCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorNodeCache.class);

    private CuratorFramework curatorFramework;

    public CuratorNodeCache() {
        init();
    }

    private void init() {
        curatorFramework = CuratorFrameworkFactory.
                builder().
                connectString("10.27.129.60:2181,10.27.129.60:2182,10.27.129.60:2183").
                sessionTimeoutMs(5000).
                connectionTimeoutMs(10000).
                retryPolicy(new RetryNTimes(2, 2000)).
                namespace("watchNodeCache").
                build();
        curatorFramework.start();
        LOGGER.info("Curator's Framework start...");
    }

    public void addNodeCacheListener(String path) throws Exception {
        Stat existStat = curatorFramework
                .checkExists()
                .forPath(path);
        if (existStat == null)
            curatorFramework
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        NodeCache nodeCache = new NodeCache(curatorFramework, path, false);
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> {
                    ChildData currentData = nodeCache.getCurrentData();
                    LOGGER.info("New Cache Data: {}", currentData == null ? "null" : new String(currentData.getData()));
                }
        );
    }

    public void setData(String path, byte[] data) throws Exception {
        curatorFramework.setData().forPath(path, data);
    }

    public String getData(String path) throws Exception {
        return new String(curatorFramework.getData().forPath(path));
    }

    public boolean delPath(String path) throws Exception {
        curatorFramework.delete().forPath(path);
        return curatorFramework.checkExists().forPath(path) == null;
    }
}
