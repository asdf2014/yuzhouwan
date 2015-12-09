package zookeeper.paxos2zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2015 zhong-ying Co.Ltd
 * All right reserved.
 * Function: ZooKeeper Event Watcher
 *
 * @author jinjy
 * @since 2015/12/8 0008
 */
public class ZKEventWatch implements Watcher {

    private static final Logger _log = LoggerFactory.getLogger(ZKEventWatch.class);

    private ZooKeeperConnPool pool;

    public ZKEventWatch(ZooKeeperConnPool pool) {
        this.pool = pool;
    }

    @Override
    public void process(WatchedEvent event) {

        Watcher.Event.KeeperState state = event.getState();
        _log.info("$$$$$$$$$$$$$$$$$$ WatchedEvent's state: " + state);

        if (Watcher.Event.KeeperState.SyncConnected == state) {
            pool.getCountDownLatch().countDown();
        } else if (Watcher.Event.KeeperState.Disconnected == state) {
            pool.getCloseZKClientLatch().countDown();
        }
    }
}
