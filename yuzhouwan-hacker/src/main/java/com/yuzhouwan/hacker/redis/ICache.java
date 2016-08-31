package com.yuzhouwan.hacker.redis;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: ICache
 *
 * @author Benedict Jin
 * @since 2016/8/31
 */
interface ICache<T> {

    T get(String key) throws CacheException;

    T get(String group, String key) throws CacheException;

    T[] get(String[] keys) throws CacheException;

    T[] get(String group, String[] keys) throws CacheException;

    void put(String key, T object) throws CacheException;

    void putSafe(String key, T object) throws CacheException;

    void put(String groupName, String key, T object) throws CacheException;

    void remove(String key) throws CacheException;

    void remove(String group, String key) throws CacheException;

    void remove(String[] keys) throws CacheException;

    void remove(String group, String[] keys) throws CacheException;
}