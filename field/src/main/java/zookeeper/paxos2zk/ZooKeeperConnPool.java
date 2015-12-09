package zookeeper.paxos2zk;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright @ 2015 zhong-ying Co.Ltd
 * All right reserved.
 * Function：Zookeeper Alive Connection Pool
 *
 * @author jinjy
 * @since 2015/12/8 0008
 */
public class ZooKeeperConnPool {

    private static final Logger _log = LoggerFactory.getLogger(ZooKeeperConnPool.class);

    private static ZooKeeperConnPool instance;

    private static CountDownLatch connectZKClientLatch;
    private static CountDownLatch closeZKClientLatch;
    private static volatile HashSet<ZooKeeper> pool;

    private static final int MIN_CONN_IN_POOL = 3;
    private static final int MAX_CONN_IN_POOL = 5;

    private static final String HOST = "127.0.0.1";
    private static final int CLIENT_PORT = 2181;

    private static final int TIME_OUT_MILLISECOND = 5000;

    private static int used = 0;

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

    private void init() {
        connectZKClientLatch = new CountDownLatch(1);
        closeZKClientLatch = new CountDownLatch(1);
        pool = new HashSet<>();
    }

    public static void initStorage() {
        if (pool.size() >= MAX_CONN_IN_POOL)
            return;
        int count = 0;
        while (pool.size() < MIN_CONN_IN_POOL) {
            createNewConnIntoPool();
            count++;
            if (count > MIN_CONN_IN_POOL && pool.size() < MIN_CONN_IN_POOL)
                throw new RuntimeException("Cannot init conn-pool[" +
                        pool.size() + "/" + MAX_CONN_IN_POOL + "] not [" +
                        MIN_CONN_IN_POOL + "/" + MAX_CONN_IN_POOL + "]");
        }
    }

    private static void createNewConnIntoPool() {
        ZooKeeper newZookeeper;
        try {
            newZookeeper = new ZooKeeper(HOST.concat(":").concat(CLIENT_PORT + ""),
                    TIME_OUT_MILLISECOND,
                    new ZKEventWatch(getInstance()));
            connectZKClientLatch.await();
            pool.add(newZookeeper);

            _log.info("################ Add a new ZKClient Connection into pool...");
            _log.info("Storage: [" + pool.size() + "/" + MAX_CONN_IN_POOL + "]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ZooKeeperConnPool() {
        init();
    }

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
        used--;
        balance();
    }

    /**
     * Balance the sum of alive zkClient in pool.
     */
    private void balance() {
        _log.info("################ Balance the storage of pool...");
        /**
         * TODO{jinjy}: need to set bigger value into MIN_CONN_IN_POOL
         */
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

    private void closeOverFullZKFromPool() {
        _log.info("################ Remove some ZKClient Connections from pool...");
        if (pool.size() <= MIN_CONN_IN_POOL)
            return;
        int count = 0;
        while (pool.size() > MIN_CONN_IN_POOL) {
            closeConn();
            count++;
            if (count > (MAX_CONN_IN_POOL - MIN_CONN_IN_POOL) && pool.size() > MIN_CONN_IN_POOL)
                throw new RuntimeException("Cannot init conn-pool[" +
                        pool.size() + "/" + MAX_CONN_IN_POOL + "] not [" +
                        MIN_CONN_IN_POOL + "/" + MAX_CONN_IN_POOL + "]");
        }
    }

    private void closeConn() {
        Iterator<ZooKeeper> iterator = pool.iterator();
        if (iterator.hasNext()) {
            ZooKeeper needCloseZK = iterator.next();
            pool.remove(needCloseZK);
            try {
                needCloseZK.close();
                closeZKClientLatch.await();

                _log.info("Storage: [" + pool.size() + "/" + MAX_CONN_IN_POOL + "]");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CountDownLatch getCountDownLatch() {
        return connectZKClientLatch;
    }

    public static CountDownLatch getCloseZKClientLatch() {
        return closeZKClientLatch;
    }
}
