package com.yuzhouwan.bigdata.zookeeper.paxos2zk;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Zookeeper Alive Connection Pool
 *
 * @author Benedict Jin
 * @since 2015/12/8 0008
 */
public final class ZooKeeperConnPool {

    private static final Logger _log = LoggerFactory.getLogger(ZooKeeperConnPool.class);
    // There need to be set bigger value into [MIN_CONN_IN_POOL], how about the one-third of [MAX_CONN_IN_POOL]
    private static final int MIN_CONN_IN_POOL = 3;
    private static final int MAX_CONN_IN_POOL = 5;
    private static final String HOST = "127.0.0.1";
    private static final int CLIENT_PORT = 2181;
    private static final int TIME_OUT_MILLISECOND = 5000;
    private static volatile ZooKeeperConnPool instance;
    private static CountDownLatch connectZKClientLatch;
    private static CountDownLatch closeZKClientLatch;
    private static volatile Set<ZooKeeper> pool;
    private static int used = 0;

    private ZooKeeperConnPool() {
        init();
    }

    /**
     * Single instance.
     *
     * @return a single instance of this class
     */
    public static ZooKeeperConnPool getInstance() {
        if (instance == null)
            synchronized (ZooKeeperConnPool.class) {
                if (instance == null) {
                    instance = new ZooKeeperConnPool();
                    initStorage();
                }
            }
        return instance;
    }

    /**
     * Create some new connections into pool, when the size of pool less than MIN_CONN_IN_POOL.
     */
    private static void initStorage() {
        if (pool.size() >= MAX_CONN_IN_POOL)
            return;
        int count = 0;
        while (pool.size() < MIN_CONN_IN_POOL) {
            createNewConnIntoPool();
            count++;
            if (count > MIN_CONN_IN_POOL && pool.size() < MIN_CONN_IN_POOL)
                throw new RuntimeException("Cannot init conn-pool["
                        + pool.size() + "/" + MAX_CONN_IN_POOL + "] not ["
                        + MIN_CONN_IN_POOL + "/" + MAX_CONN_IN_POOL + "]");
        }
    }

    /**
     * Create a new connection.
     */
    private static void createNewConnIntoPool() {
        ZooKeeper newZookeeper;
        try {
            newZookeeper = new ZooKeeper(HOST.concat(":").concat(CLIENT_PORT + ""),
                    TIME_OUT_MILLISECOND,
                    new ZKEventWatch());
            // TODO{Benedict Jin}: timeout
            connectZKClientLatch.await();
            pool.add(newZookeeper);

            _log.info("################ Add a new ZKClient Connection into pool...");
            _log.info("Storage: [" + pool.size() + "/" + MAX_CONN_IN_POOL + "]");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static CountDownLatch getConnectZKClientLatch() {
        return connectZKClientLatch;
    }

    public static CountDownLatch getCloseZKClientLatch() {
        return closeZKClientLatch;
    }

    /**
     * Make a initialization.
     */
    private void init() {
        connectZKClientLatch = new CountDownLatch(1);
        closeZKClientLatch = new CountDownLatch(1);
        pool = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * Get a alive connection from pool.
     *
     * @return a alive zookeeper connection which state is Watcher.Event.KeeperState.SyncConnected
     */
    public ZooKeeper getConn() {
        _log.info("################ Get ZKClient Connection from pool...");
        if (used >= MAX_CONN_IN_POOL) {
            return null;
        }
        if (pool.size() <= 0) {
            return null;
        } else if (pool.size() >= MAX_CONN_IN_POOL) {
            throw new RuntimeException("Error: Zookeeper is over weight...");
        } else {
            if (pool.isEmpty())
                return null;
            else {
                ZooKeeper pullZK = pool.iterator().next();
                pool.remove(pullZK);
                used++;
                balance();
                return pullZK;
            }
        }
    }

    /**
     * Free a alive connection into pool.
     *
     * @param freeZK a zookeeper connection will be closed or add into connection pool
     */
    public void freeConn(ZooKeeper freeZK) {
        _log.info("################ Free ZKClient Connection into pool...");
        if (freeZK == null)
            return;
        if (!freeZK.getState().isAlive())
            return;
        if (pool.size() >= MIN_CONN_IN_POOL) {
            try {
                freeZK.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            pool.add(freeZK);
        }
        // [used] field means the capacity of supporting the max sum of connections with zookeeper actually
        used--;
        balance();
    }

    /**
     * Close some overfull connection from pool.
     */
    private void closeOverFullZKFromPool() {
        _log.info("################ Remove some ZKClient Connections from pool...");
        if (pool.size() <= MIN_CONN_IN_POOL)
            return;
        int count = 0;
        while (pool.size() > MIN_CONN_IN_POOL) {
            closeConn();
            count++;
            if (count > (MAX_CONN_IN_POOL - MIN_CONN_IN_POOL) && pool.size() > MIN_CONN_IN_POOL)
                throw new RuntimeException("Cannot init conn-pool["
                        + pool.size() + "/" + MAX_CONN_IN_POOL + "] not ["
                        + MIN_CONN_IN_POOL + "/" + MAX_CONN_IN_POOL + "]");
        }
    }

    /**
     * Close a connection from pool.
     */
    private void closeConn() {
        Iterator<ZooKeeper> iterator = pool.iterator();
        if (iterator.hasNext()) {
            ZooKeeper needCloseZK = iterator.next();
            pool.remove(needCloseZK);
            try {
                /**
                 * TODO{Benedict Jin}: timeout
                 */
                needCloseZK.close();
                closeZKClientLatch.await();

                _log.info("Storage: [" + pool.size() + "/" + MAX_CONN_IN_POOL + "]");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Balance the sum of alive zkClient in pool.
     */
    private void balance() {
        _log.info("################ Balance the storage of pool...");

        if (used < MAX_CONN_IN_POOL && pool.size() < MIN_CONN_IN_POOL) {
            Thread addSomeConnThread = new Thread() {
                @Override
                public void run() {
                    initStorage();
                }
            };
            addSomeConnThread.start();
        } else if (used < MAX_CONN_IN_POOL && pool.size() > MIN_CONN_IN_POOL) {
            Thread removeSomeConnThread = new Thread() {
                @Override
                public void run() {
                    closeOverFullZKFromPool();
                }
            };
            removeSomeConnThread.start();
        }
    }
}
