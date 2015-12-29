package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：Distributed Barrier with Curator
 *
 * @author jinjy
 * @since 2015/12/29 0029
 */
public class CuratorDistributedBarrier {

    private final static Logger _log = LoggerFactory.getLogger(CuratorDistributedBarrier.class);
    private CuratorFramework curatorFramework;
    private DistributedBarrier distributedBarrier;

    private void init() {
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(3000)
                .sessionTimeoutMs(5000)
                .retryPolicy(new RetryNTimes(3, 2000))
                .namespace("distBarrier")
                .build();
        curatorFramework.start();
        distributedBarrier = new DistributedBarrier(curatorFramework, "/barrier");
    }

    public CuratorDistributedBarrier() {
        init();
    }

    public void showThreeBarrier() throws Exception {

        int count = 3;
        while (count > 0) {

            final int finalCount = count;
            new Thread() {
                @Override
                public void run() {
                    try {
                        _log.info("set {}...", finalCount);
                        distributedBarrier.setBarrier();
                        distributedBarrier.waitOnBarrier();
                        _log.info("start {}...", finalCount);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();
            count--;
        }
        /**
         * keep a while for waiting other barriers
         */
        Thread.sleep(2000);
        distributedBarrier.removeBarrier();
    }

}
