package com.yuzhouwan.bigdata.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Distributed Queue with Zookeeper
 *
 * @author Benedict Jin
 * @since 2015/10/14 0008
 */
public class DistributedQueue {

    private static final Logger _log = LoggerFactory.getLogger(DistributedQueue.class);

    private static final String yuzhouwan4 = "192.168.1.101:2181";
    private static final String yuzhouwan5 = "192.168.1.102:2181";
    private static final String yuzhouwan6 = "192.168.1.103:2181";

    public static void main(String... args) throws Exception {

        /*
         * Queue is exist in '/distributedQueue'!
         * Adding	1	into queue [/distributedQueue] ...
         * Adding	2	into queue [/distributedQueue] ...
         * --------------------------------
         * Adding	3	into queue [/distributedQueue] ...
         * --------------------------------
         * Get the data:	3	from /distributedQueue/30000000062
         * Get the data:	1	from /distributedQueue/10000000060
         * Get the data:	2	from /distributedQueue/20000000061
         * No node to consume.
         */
        process();
    }

    public static void process() throws Exception {

        ZooKeeper zk4 = connection(yuzhouwan4);
        initQueue(zk4);

        produce(zk4, 1);
        produce(zk4, 2);

        System.out.println("--------------------------------");
        ZooKeeper zk5 = connection(yuzhouwan5);
        produce(zk5, 3);

        System.out.println("--------------------------------");
        ZooKeeper zk6 = connection(yuzhouwan6);
        consume(zk6);
        consume(zk6);
        consume(zk6);
        consume(zk6);
    }

    private static ZooKeeper connection(String host) throws IOException {

        return new ZooKeeper(host, 60000, event -> {
            _log.info("Path: {}, State: {}", event.getPath(), event.getState());
        });
    }

    private static void initQueue(ZooKeeper zk) throws KeeperException, InterruptedException {

        if (zk.exists("/distributedQueue", false) == null) {
            System.out.println("Create queue that path is '/distributedQueue'.");
            zk.create("/distributedQueue", "DQ".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            System.out.println("Queue is exist in '/distributedQueue'!");
        }
    }

    private static void produce(ZooKeeper zk, int zNode) throws KeeperException, InterruptedException {

        System.out.println("Adding\t" + zNode + "\tinto queue [/distributedQueue] ...");
        zk.create("/distributedQueue/" + zNode, (zNode + "").getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    private static void consume(ZooKeeper zk) throws KeeperException, InterruptedException {

        List<String> list = zk.getChildren("/distributedQueue", true);
        if (list.size() > 0) {

            String subPath = list.get(0);
            String zNode = "/distributedQueue/" + subPath;
            System.out.println("Get the data:\t" + new String(zk.getData(zNode, false, null),
                    Charset.forName("UTF-8")) + "\tfrom " + zNode);

            zk.delete(zNode, 0);
        } else {
            System.out.println("No node to consume.");
        }
    }
}
