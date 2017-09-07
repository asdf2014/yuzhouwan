package com.yuzhouwan.bigdata.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：ZookeeperWatcher
 *
 * @author Benedict Jin
 * @since 2015/11/9
 */
public class ZookeeperWatcher {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperWatcher.class);

    private static final String yuzhouwan4 = "yuzhouwan04:2181";
    private static final String zNode = "/yuzhouwan";

    {
        init();
    }

    public void init() {

        RetryPolicy retrypolicy = new ExponentialBackoffRetry(3000, 60);
        CuratorFramework curatorClient;
//        curatorClient = CuratorFrameworkFactory.newClient(yuzhouwan4, 5000, 3000, retrypolicy);

        /**
         * using fluent api
         */
        curatorClient = CuratorFrameworkFactory.builder()
                .connectString(yuzhouwan4)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(retrypolicy)
                .namespace("watcher")       // root path: /watcher
                .build();
        curatorClient.start();

        final PathChildrenCache cached = new PathChildrenCache(curatorClient, zNode, true);
        cached.getListenable().addListener((client, event) -> {
            PathChildrenCacheEvent.Type childrenEventType = event.getType();
            if (childrenEventType != null) {
                switch (childrenEventType) {
                    case CONNECTION_RECONNECTED:
                        cached.rebuild();
                        break;
                    case CONNECTION_SUSPENDED:
                    case CONNECTION_LOST:
                        logger.error("Connection error, waiting...");
                        break;
                    default:
                        logger.info("PathChildrenCache changed : {path:" + event.getData().getPath() + " data:"
                                + new String(event.getData().getData()) + "}");
                }
            }
        });
        try {
            cached.start();
        } catch (Exception e) {
            logger.error("Can not start PathChildrenCache!!");
            throw new RuntimeException(e);
        }
    }
}
