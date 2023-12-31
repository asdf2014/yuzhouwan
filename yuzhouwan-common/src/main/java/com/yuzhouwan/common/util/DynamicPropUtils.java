package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Dynamic PropUtils
 *
 * @author Benedict Jin
 * @since 2017/6/28
 */
public final class DynamicPropUtils implements Serializable, Cloneable, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicPropUtils.class);
    private static final String ZNODE_PREFIX = "/";
    private static final ConcurrentHashMap<String, Prop> PROJECT_PROPERTIES = new ConcurrentHashMap<>();
    // curator configs
    private static final int CONNECTION_TIMEOUT_MS = 5000;
    private static final int SESSION_TIMEOUT_MS = 40000;
    private static final int RETRY_POLICY_INTERVAL = 2000;
    private static final int RETRY_POLICY_TIMES = 3;
    private static final long TICK_THRESHOLD = 30L;
    private static final long TICK_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private static volatile DynamicPropUtils instance;
    private static CuratorFramework curatorFramework;
    // tick for sync
    private static final LongAdder TICK = new LongAdder();
    private static volatile boolean KEEP_SYNCING = true;
    private static final Runnable TIMING_SYNC = () -> {
        while (KEEP_SYNCING) {
            if (TICK.sum() >= TICK_THRESHOLD) {
                DynamicPropUtils instance = getInstance();
                for (Map.Entry<String, Prop> entry : PROJECT_PROPERTIES.entrySet()) instance.sync(entry.getKey());
                TICK.reset();
            } else TICK.increment();
            try {
                Thread.sleep(TICK_MILLIS);
            } catch (InterruptedException e) {
                LOGGER.error("", e);
            }
        }
    };

    private DynamicPropUtils() {
        init();
    }

    private static void init() {
        try {
            initCurator();
        } catch (Exception e) {
            LOGGER.error("Cannot init curator in Dynamic PropUtils!", e);
            throw new RuntimeException(e);
        }
        KEEP_SYNCING = true;
        new Thread(TIMING_SYNC).start();
    }

    private static void initCurator() throws Exception {
        LOGGER.debug("Curator initializing...");

        Object zkPath;
        int count = 0;
        while ((zkPath = PropUtils.getInstance().getProperty("dynamic.prop.utils.zk.path")) == null) {
            if (++count >= TICK_THRESHOLD) {
                instance.close();
                System.exit(-1);
            }
            LOGGER.warn("Cannot get dynamic.prop.utils.zk.path from Dynamic PropUtils! Times: {}", count);
            LOGGER.warn("PropUtils: {}", JSON.toJSONString(PropUtils.getInstance().getProperties()));
            Thread.sleep(TICK_MILLIS);
        }
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString(zkPath.toString())
                .connectionTimeoutMs(CONNECTION_TIMEOUT_MS)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(RETRY_POLICY_INTERVAL, RETRY_POLICY_TIMES))
                .namespace("dynamic")
                .build();
        LOGGER.debug("Curator initialized.");
        LOGGER.debug("Curator starting...");
        curatorFramework.start();
        LOGGER.debug("Curator started.");
    }

    public static DynamicPropUtils getInstance() {
        if (instance == null) synchronized (DynamicPropUtils.class) {
            if (instance == null) {
                instance = new DynamicPropUtils();
            }
        }
        return instance;
    }

    public boolean add(String projectName, Properties p) {
        if (projectName == null || p == null) {
            LOGGER.warn("Params is invalid! projectName: {}, properties: {}.", projectName, p);
            return false;
        }
        Prop prop = PROJECT_PROPERTIES.get(projectName);
        if (prop == null) prop = new Prop(p, System.nanoTime());
        else {
            Properties properties = prop.getP();
            Object key;
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                key = entry.getKey();
                properties.put(key, p.get(key));
            }
            prop.setP(properties);
        }
        PROJECT_PROPERTIES.put(projectName, prop);
        return true;
    }

    public Object get(String projectName, String key) {
        if (projectName == null || key == null) {
            LOGGER.warn("Params is invalid! projectName: {}, key: {}.", projectName, key);
            return null;
        }
        Properties p = getProperties(projectName);
        if (p == null) return null;
        return p.get(key);
    }

    public Properties getProperties(String projectName) {
        if (projectName == null) {
            LOGGER.warn("ProjectName is null!");
            return null;
        }
        Prop prop = PROJECT_PROPERTIES.get(projectName);
        if (prop == null) return null;
        return prop.getP();
    }

    public Object getFromLocal(String projectName, String key) {
        return get(projectName, key);
    }

    public Object getFromRemote(String projectName, String key) {
        if (projectName == null || key == null) {
            LOGGER.warn("Params is invalid! projectName: {}, key: {}.", projectName, key);
            return null;
        }
        Prop propFromRemote;
        try {
            propFromRemote = getPropFromRemote(projectName);
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }
        if (propFromRemote == null) return null;
        Properties p = propFromRemote.getP();
        if (p == null) return null;
        return p.get(key);
    }

    public boolean sync(String projectName) {

        if (uninitialized()) return false;

        // (local + non_local) * (remote + non_monitor)
        if (projectName == null) {
            LOGGER.error("Sync failed! Cause projectName cannot be null!");
            return false;
        }
        ExistsBuilder existsBuilder = curatorFramework.checkExists();
        Stat stat;
        try {
            stat = existsBuilder.forPath(ZNODE_PREFIX.concat(projectName));
        } catch (Exception e) {
            LOGGER.error("Sync failed!", e);
            return false;
        }
        Prop localProp = PROJECT_PROPERTIES.get(projectName);
        boolean isRemote, isLocal;
        if (stat == null) {
            LOGGER.debug("Configuration about project[{}] is not on remote.", projectName);
            isRemote = false;
        } else {
            LOGGER.debug("Configuration about project[{}] is on remote.", projectName);
            isRemote = true;
        }
        if (localProp == null) {
            LOGGER.debug("Configuration about project[{}] is not on local.", projectName);
            isLocal = false;
        } else {
            LOGGER.debug("Configuration about project[{}] is on local.", projectName);
            isLocal = true;
        }
        boolean isSynced = internalSync(projectName, localProp, isRemote, isLocal);
        if (isSynced) LOGGER.debug("Sync success!");
        return isSynced;
    }

    private boolean uninitialized() {
        if (curatorFramework == null) {
            LOGGER.error("Sync failed! Cause curatorFramework is null!");
            return true;
        }
        return false;
    }

    private boolean internalSync(String projectName, Prop localProp, boolean isRemote, boolean isLocal) {
        try {
            if (!isLocal && !isRemote) {
                LOGGER.warn("Sync failed! Cause: config about project[{}] is not on local and remote!", projectName);
                return false;
            } else if (isLocal && !isRemote) {
                curatorFramework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(ZNODE_PREFIX.concat(projectName));
                LOGGER.debug("Created ".concat(projectName));
                return setProp2Remote(projectName, localProp);
            } else if (!isLocal) {
                Prop remoteProp = getPropFromRemote(projectName);
                if (remoteProp == null) return false;
                PROJECT_PROPERTIES.put(projectName, remoteProp);
            } else {
                Prop remoteProp = getPropFromRemote(projectName);
                if (remoteProp == null) return false;
                long remoteModifyDate = remoteProp.getModify();
                long localModifyDate = localProp.getModify();
                if (remoteModifyDate == localModifyDate) return true;
                else if (remoteModifyDate > localModifyDate) PROJECT_PROPERTIES.put(projectName, remoteProp);
                else return setProp2Remote(projectName, localProp);
            }
        } catch (Exception e) {
            LOGGER.error("Sync failed!", e);
            return false;
        }
        return true;
    }

    private boolean setProp2Remote(String projectName, Prop localProp) throws Exception {
        if (uninitialized()) return false;
        curatorFramework.setData()
                .forPath(ZNODE_PREFIX.concat(projectName),
                        JSON.toJSONString(localProp).getBytes(StandardCharsets.UTF_8));
        LOGGER.debug("Set data to remote success!");
        return true;
    }

    private Prop getPropFromRemote(String projectName) throws Exception {
        if (uninitialized()) return null;
        String data = new String(curatorFramework.getData()
                .forPath(ZNODE_PREFIX.concat(projectName)), StandardCharsets.UTF_8);
        return JSON.parseObject(data, Prop.class);
    }

    @Override
    public void close() {
        KEEP_SYNCING = false;
        instance = null;
        if (curatorFramework != null) {
            curatorFramework.close();
            curatorFramework = null;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
