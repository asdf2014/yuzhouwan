package com.yuzhouwan.bigdata.zookeeper.paxos2zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: ZooKeeper Event Watcher
 *
 * @author Benedict Jin
 * @since 2015/12/8 0008
 */
public class ZKEventWatch implements Watcher {

    private static final Logger _log = LoggerFactory.getLogger(ZKEventWatch.class);

    public ZKEventWatch() {
    }

    @Override
    public void process(WatchedEvent event) {

        Watcher.Event.KeeperState state = event.getState();
        _log.info("$$$$$$$$$$$$$$$$$$ WatchedEvent's state: " + state);

        if (Watcher.Event.KeeperState.SyncConnected == state) {
            ZooKeeperConnPool.getConnectZKClientLatch().countDown();
        } else if (Watcher.Event.KeeperState.Disconnected == state) {
            ZooKeeperConnPool.getCloseZKClientLatch().countDown();
        }
    }
}
