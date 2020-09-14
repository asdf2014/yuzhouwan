package com.yuzhouwan.bigdata.redis.multi.cache;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: CacheException
 *
 * @author Benedict Jin
 * @since 2016/8/31
 */
public class CacheException extends RuntimeException {

    public CacheException(String s) {
        super(s);
    }

    public CacheException(String s, Throwable e) {
        super(s, e);
    }

    public CacheException(Throwable e) {
        super(e);
    }
}
