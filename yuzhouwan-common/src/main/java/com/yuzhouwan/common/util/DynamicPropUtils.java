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

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.yuzhouwan.common.util.StrUtils.isEmpty;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Dynamic PropUtils
 *
 * @author Benedict Jin
 * @since 2017/6/28
 */
public class DynamicPropUtils {

    private static final Logger _log = LoggerFactory.getLogger(DynamicPropUtils.class);
    private static final String ZNODE_PREFIX = "/";

    private static DynamicPropUtils instance;
    private static CuratorFramework curatorFramework;
    private static final ConcurrentHashMap<String, Prop> PROJECT_PROPERTIES = new ConcurrentHashMap<>();

    private volatile static long TICK;
    private static final long TICK_THRESHOLD = 30L;
    private static final long TICK_MILLIS = 1000L;
    private volatile static boolean KEEP_SYNCING = true;
    private static final Thread TIMING_SYNC = new Thread(() -> {
        while (KEEP_SYNCING) {
            if (TICK >= TICK_THRESHOLD) {
                for (Map.Entry<String, Prop> entry : PROJECT_PROPERTIES.entrySet()) {
                    getInstance().sync(entry.getKey());
                }
                TICK = 0;
            } else TICK++;
            try {
                Thread.sleep(TICK_MILLIS);
            } catch (InterruptedException e) {
                _log.error("", e);
            }
        }
    });

    private static void init() {
        try {
            initCurator();
        } catch (Exception e) {
            _log.error("Cannot init curator in Dynamic PropUtils!", e);
            throw new RuntimeException(e);
        }
        TIMING_SYNC.start();
    }

    private static void initCurator() throws Exception {
        _log.debug("Begin init Curator...");
        String zkPath = PropUtils.getInstance().getProperty("dynamic.prop.utils.zk.path");
        if (isEmpty(zkPath))
            throw new RuntimeException("Cannot get dynamic.prop.utils.zk.path form PropUtils.");
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString(zkPath)
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
                init();
                instance = new DynamicPropUtils();
            }
        }
        return instance;
    }

    public boolean add(String projectName, Properties p) {
        if (projectName == null || p == null) {
            _log.warn("Params is invalid! projectName: {}, properties: {}.", projectName, p);
            return false;
        }
        Prop prop = PROJECT_PROPERTIES.get(projectName);
        if (prop == null) {
            prop = new Prop(p, System.currentTimeMillis());
        } else {
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
            _log.warn("Params is invalid! projectName: {}, key: {}.", projectName, key);
            return null;
        }
        Properties p = getProperties(projectName);
        if (p == null) return null;
        return p.get(key);
    }

    public Properties getProperties(String projectName) {
        if (projectName == null) {
            _log.warn("ProjectName is null!");
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
            _log.warn("Params is invalid! projectName: {}, key: {}.", projectName, key);
            return null;
        }
        Prop propFromRemote;
        try {
            propFromRemote = getPropFromRemote(projectName);
        } catch (Exception e) {
            _log.error("", e);
            return null;
        }
        if (propFromRemote == null) return null;
        Properties p = propFromRemote.getP();
        if (p == null) return null;
        return p.get(key);
    }

    public boolean sync(String projectName) {

        if (curatorFramework == null) {
            _log.error("Sync failed! Cause curatorFramework is null!");
            return false;
        }

        // (local + non_local) * (remote + non_monitor)
        if (projectName == null) {
            _log.error("Sync failed! Cause projectName cannot be null!");
            return false;
        }
        ExistsBuilder existsBuilder = curatorFramework.checkExists();
        Stat stat;
        try {
            stat = existsBuilder.forPath(ZNODE_PREFIX.concat(projectName));
        } catch (Exception e) {
            _log.error("Sync failed! Cause: {}", e.getMessage());
            return false;
        }
        Prop localProp = PROJECT_PROPERTIES.get(projectName);
        boolean isRemote, isLocal;
        if (stat == null) {
            _log.debug("Configuration about project[{}] is not on remote.", projectName);
            isRemote = false;
        } else {
            _log.debug("Configuration about project[{}] is on remote.", projectName);
            isRemote = true;
        }
        if (localProp == null) {
            _log.debug("Configuration about project[{}] is not on local.", projectName);
            isLocal = false;
        } else {
            _log.debug("Configuration about project[{}] is on local.", projectName);
            isLocal = true;
        }
        Boolean isSynced = internalSync(projectName, localProp, isRemote, isLocal);
        if (isSynced != null) return isSynced;
        _log.debug("Sync success!");
        return true;
    }

    private Boolean internalSync(String projectName, Prop localProp, boolean isRemote, boolean isLocal) {
        try {
            if (!isLocal && !isRemote) {
                _log.warn("Sync failed! Cause: configuration about project[{}] is not on local and remote!",
                        projectName);
                return false;
            } else if (isLocal && !isRemote) {
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                        .forPath(ZNODE_PREFIX.concat(projectName));
                _log.debug("Created ".concat(projectName));
                setProp2Remote(projectName, localProp);
                return true;
            } else if (!isLocal) {
                Prop remoteProp = getPropFromRemote(projectName);
                PROJECT_PROPERTIES.put(projectName, remoteProp);
            } else {
                Prop remoteProp = getPropFromRemote(projectName);
                long remoteModifyDate = remoteProp.getModify();
                long localModifyDate = localProp.getModify();
                if (remoteModifyDate > localModifyDate) {
                    PROJECT_PROPERTIES.put(projectName, remoteProp);
                } else {
                    setProp2Remote(projectName, localProp);
                }
            }
        } catch (Exception e) {
            _log.error("Sync failed!", e);
            return false;
        }
        return null;
    }

    private void setProp2Remote(String projectName, Prop localProp) throws Exception {
        curatorFramework.setData()
                .forPath(ZNODE_PREFIX.concat(projectName), JSON.toJSONString(localProp).getBytes(Charset.forName("UTF-8")));
        _log.debug("Set data to remote success!");
    }

    private Prop getPropFromRemote(String projectName) throws Exception {
        String data = new String(curatorFramework.getData()
                .forPath(ZNODE_PREFIX.concat(projectName)), Charset.forName("UTF-8"));
        return JSON.parseObject(data, Prop.class);
    }

    public void close() {
        KEEP_SYNCING = false;
        instance = null;
        if (curatorFramework != null) {
            curatorFramework.close();
            curatorFramework = null;
        }
    }
}
