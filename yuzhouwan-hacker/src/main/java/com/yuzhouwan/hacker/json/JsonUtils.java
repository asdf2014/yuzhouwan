package com.yuzhouwan.hacker.json;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Make some simple example for fastJSON
 *
 * @author Benedict Jin
 * @since 2016/3/17
 */
public class JsonUtils {

    /**
     * example for JSON
     *
     * @return A 数组
     */
    public List<A> simpleParse() {
        String s = "[{\"rule\":\"is down\", \"groupId\":\"1\",\"mode\":\"WECHAT\", \"level\":\"1\"}]";
        return JSON.parseArray(s, A.class);
    }
}