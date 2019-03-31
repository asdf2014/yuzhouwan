package com.yuzhouwan.hacker.json;

import com.alibaba.fastjson.JSON;
import com.yuzhouwan.common.http.HttpUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.Ignore;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Copyright @ yuzhouwan.com
 * All right reserved.
 * Function：PostRequestMethod Tester
 *
 * @author Benedict Jin
 * @since 2016/3/21
 */
public class PostRequestMethodTest {

    private static final Logger _log = LoggerFactory.getLogger(PostRequestMethodTest.class);
    private static final String url = "http://localhost:8080/yuzhouwan-site/post/msg";

    @Ignore
    @Test
    public void queryPost() {
        this.doRequest("msg");
    }

    /**
     * This method will be suit for query url in other process without SPRING CONTEXT.
     *
     * @param msg
     * @return
     */
    private List<A> doRequest(String msg) {
        try {
            _log.debug("do request url {} param {}", PostRequestMethodTest.url, msg);
            StringEntity entity = new StringEntity(msg, ContentType.DEFAULT_TEXT);
            String post = HttpUtils.getInstance().postPlain(PostRequestMethodTest.url, entity, null);
            return JSON.parseArray(post, A.class);
        } catch (Exception e) {
            _log.error(e.getMessage());
            return null;
        }
    }
}