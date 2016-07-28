package com.yuzhouwan.hacker.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Watch children changes with curator
 *
 * @author Benedict Jin
 * @since 2015/12/22 0022
 */
public class CuratorChildrenCache {

    private final static Logger _log = LoggerFactory.getLogger(CuratorChildrenCache.class);
    private CuratorFramework curatorFramework;

    public CuratorChildrenCache() throws Exception {
        init();
    }

    private void init() throws Exception {
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(2000, 3))
                .namespace("children")
                .build();
        curatorFramework.start();
    }

    public void addChildrenListener(String path) throws Exception {

        Stat existStat = curatorFramework.checkExists().forPath(path);
        if (existStat == null)
            curatorFramework
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, false);
        pathChildrenCache.start(PathChildrenCache.StartMode.NORMAL);
        pathChildrenCache
                .getListenable()
                .addListener(
                        (CuratorFramework client, PathChildrenCacheEvent event) -> {
                            PathChildrenCacheEvent.Type type = event.getType();
                            _log.info("Event type: {}", type);
                            switch (type) {
                                case CONNECTION_RECONNECTED:
                                    _log.info("Reconnected...");
                                    break;
                                case CONNECTION_LOST:
                                    _log.info("Connection lost...");
                                    pathChildrenCache.rebuild();
                                    _log.info("Rebuild pathChildrenCache...");
                                    break;
                                case CONNECTION_SUSPENDED:
                                    _log.info("Connection suspended...");
                                    break;
                                case CHILD_ADDED:
                                    _log.info("Add new child: {}", event.getData().getPath());
                                    break;
                                case CHILD_UPDATED:
                                    _log.info("Updated child: {}", event.getData().getPath());
                                    break;
                                case CHILD_REMOVED:
                                    _log.info("Removed child: {}", event.getData().getPath());
                                    break;
                                default:
                                    _log.error("Something was not excepted: {}", type);
                                    break;
                            }
                        }
                );
    }

    public void createNode(String path) throws Exception {
        curatorFramework
                .create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path);
    }

    public void deleteNode(String path) throws Exception {
        curatorFramework
                .delete()
                .deletingChildrenIfNeeded()
                .forPath(path);
    }

    public void updateNode(String path, byte[] data) throws Exception {
        curatorFramework
                .setData()
                .forPath(path, data);
    }

}
