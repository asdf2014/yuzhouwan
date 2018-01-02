package com.yuzhouwan.bigdata.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Leader Selector
 *
 * @author Benedict Jin
 * @since 2015/12/23 0023
 */
public class CuratorLeaderSelector {

    private static final Logger LOG = LoggerFactory.getLogger(CuratorLeaderSelector.class);

    private CuratorFramework curatorFramework;

    public CuratorLeaderSelector() {
        init();
    }

    private void init() {
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(3000)
                .retryPolicy(new RetryNTimes(3, 2000))
                .namespace("leaderSelector")
                .build();
        curatorFramework.start();
    }

    public void leaderSelector(final String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            curatorFramework
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        }
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, path,
                new LeaderSelectorListenerAdapter() {
                    @Override
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        LOG.info("CuratorFramework connection string: {}",
                                client.getZookeeperClient().getCurrentConnectionString());
                        LOG.info("Namespace: {}", client.getNamespace());
                        Stat stat = client.checkExists().forPath(path);
                        LOG.info("Check the stat in path [{}]: {}", path, stat.toString());
                    }
                }
        );
        LOG.info("Leader selector auto requeue...");
        leaderSelector.autoRequeue();
        leaderSelector.start();
        LOG.info("Starting...");
    }
}
