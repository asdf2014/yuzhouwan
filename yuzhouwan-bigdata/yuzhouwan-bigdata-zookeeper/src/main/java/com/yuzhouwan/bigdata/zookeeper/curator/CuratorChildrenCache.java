package com.yuzhouwan.bigdata.zookeeper.curator;

import com.yuzhouwan.common.util.TimeUtils;
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
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Watch children changes with curator
 *
 * @author Benedict Jin
 * @since 2015/12/22 0022
 */
public class CuratorChildrenCache {

    // Start a org.apache.zookeeper.server.ZooKeeperServerMain instance local first
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

    public String readNode(String path) throws Exception {
        return new String(curatorFramework
                .getData()
                .forPath(path));
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

    public static void main(String[] args) throws Exception {
        System.setProperty("jute.maxbuffer", "1024");
//        System.setProperty("zookeeper.clientCnxnSocket", "org.apache.zookeeper.ClientCnxnSocketNetty");
        CuratorChildrenCache ccc = new CuratorChildrenCache();
        ccc.createNode("/yuzhouwan");
        ccc.updateNode("/yuzhouwan", "Blog Update Date: ".concat(TimeUtils.nowStr()).getBytes());
        System.out.println(ccc.readNode("/yuzhouwan"));
//        for (int i = 1024 * 1024 - 42; i < 1024 * 1024; i++) {
//            jute(ccc, i);
//        }
        jute(ccc, 512 - 89);
        System.out.println(ccc.readNode("/"));
        jute(ccc, 512 - 88);
//        jute(ccc, 511);
//        jute(ccc, 512);
//        jute(ccc, 513);

        jute(ccc, 1024 - 89);
        /*
        java.io.IOException: Packet len1024 is out of range!
        返回的包大小超出了 1024字节
         */
        jute(ccc, 936); // read > 1024
        jute(ccc, 937);

        jute(ccc, 1023);
        /*
        Exception in thread "main" org.apache.zookeeper.KeeperException$ConnectionLossException:
        KeeperErrorCode = ConnectionLoss for /children/jute1023
        写 ZNode数据的包大小超出了 1024字节
         */
        jute(ccc, 1024); // write > 1024
        jute(ccc, 1025);

        jute(ccc, 1024 * 1024 - 42);
        /*
        java.io.IOException: 您的主机中的软件中止了一个已建立的连接。
         */
        jute(ccc, 1024 * 1024 - 41);  // write > 1024 * 1024 (1M)
        jute(ccc, 1024 * 1024);
        jute(ccc, 1024 * 1024 + 1);
    }

    private static void jute(CuratorChildrenCache ccc, int len) throws Exception {
        String jutePath = "/jute" + len;
        ccc.createNode(jutePath);
        System.out.println("Created ".concat(jutePath));
        byte[] jute = new byte[len];
        for (int i = 0; i < len; i++) {
            jute[i] = '0';
        }
        ccc.updateNode(jutePath, jute);
        System.out.println("Updated ".concat(jutePath));
        System.out.println(ccc.readNode(jutePath));
    }
}
