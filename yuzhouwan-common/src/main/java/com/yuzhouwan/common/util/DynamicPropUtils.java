package com.yuzhouwan.common.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：${PACKAGE}
 *
 * @author Benedict Jin
 * @since 2017/6/28
 */
public class DynamicPropUtils {

    private static final Logger _log = LoggerFactory.getLogger(DynamicPropUtils.class);

    private static DynamicPropUtils instance;
    private static CuratorFramework curatorFramework;
    private static final ConcurrentHashMap<Long, Properties> map = new ConcurrentHashMap<>();

    private static LongAdder tick = new LongAdder();
    private static final long TICK_THRESHOLD = 60L;
    private static final Thread timingSync = new Thread(() -> {
        for (; ; ) {
            long tickTimes = tick.longValue();
            if (tickTimes >= TICK_THRESHOLD) {
                getInstance().sync();
                tick.reset();
            } else {
                tick.increment();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                _log.error("", e);
            }
        }
    });

    private static void init() {
        try {
            initCurator();
        } catch (Exception e) {
            _log.error("Cannot init curator in Dynamic PropUtils", e);
            throw new RuntimeException(e);
        }
        timingSync.start();
    }

    private static void initCurator() throws Exception {
        _log.debug("Begin init Curator...");
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("10.27.129.60:2181;10.27.129.60:2182;10.27.129.60:2183")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(40000)
                .retryPolicy(new ExponentialBackoffRetry(2000, 3))
                .namespace("dynamic")
                .build();
        _log.debug("Curator initialized.");
        curatorFramework.start();
        _log.debug("Curator started.");
    }

    private DynamicPropUtils() {
    }

    public static DynamicPropUtils getInstance() {
        if (instance == null) synchronized (DynamicPropUtils.class) {
            if (instance == null) {
                instance = new DynamicPropUtils();
                init();
            }
        }
        return instance;
    }

    public boolean upload(Properties p) {
        return true;
    }

    public Properties download() {
        return new Properties();
    }

    public boolean sync() {
        return true;
    }
}
