package com.yuzhouwan.bigdata.redis.multi.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: MultiCache
 *
 * @author Benedict Jin
 * @since 2016/8/31
 */
public class MultiCache implements ICache {

    /*
    1-level cache.
     */
    private ICache<Object> cacheFirst;

    /*
    2-level cache.
     */
    private ICache<Object> cacheSecond;

    public MultiCache() {
    }

    public MultiCache(ICache<Object> cacheFirst, ICache<Object> cacheSecond) {
        this.cacheFirst = cacheFirst;
        this.cacheSecond = cacheSecond;
    }

    public Object get(String key) throws CacheException {
        Object obj = cacheFirst.get(key);
        if (obj != null) return obj;
        obj = cacheSecond.get(key);
        cacheFirst.put(key, obj);
        return obj;
    }

    public Object get(String group, String key) throws CacheException {
        Object obj = cacheFirst.get(group, key);
        if (obj != null) return obj;
        obj = cacheSecond.get(group, key);
        cacheFirst.put(group, key, obj);
        return obj;
    }

    public Object[] get(String[] keys) throws CacheException {
        List<Object> objList = new ArrayList<>();
        if (keys == null || keys.length == 0) return objList.toArray();
        for (String key : keys) objList.add(get(key));
        return objList.toArray();
    }

    public Object[] get(String group, String[] keys) throws CacheException {
        List<Object> objList = new ArrayList<>();
        if (keys == null || keys.length == 0) return objList.toArray();
        for (String key : keys) objList.add(get(group, key));
        return objList.toArray();
    }

    public void put(String key, Object object) throws CacheException {
        cacheFirst.put(key, object);
        cacheSecond.put(key, object);
    }

    public void putSafe(String key, Object object) throws CacheException {
        cacheFirst.putSafe(key, object);
        cacheSecond.putSafe(key, object);
    }

    public void put(String groupName, String key, Object object) throws CacheException {
        cacheFirst.put(groupName, key, object);
        cacheSecond.put(groupName, key, object);
    }

    public void remove(String key) throws CacheException {
        cacheFirst.remove(key);
        cacheSecond.remove(key);
    }

    public void remove(String group, String key) throws CacheException {
        cacheFirst.remove(group, key);
        cacheSecond.remove(group, key);
    }

    public void remove(String[] keys) throws CacheException {
        cacheFirst.remove(keys);
        cacheSecond.remove(keys);
    }

    public void remove(String group, String[] keys) throws CacheException {
        cacheFirst.remove(group, keys);
        cacheSecond.remove(group, keys);
    }
}
