package com.yuzhouwan.common.http;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: HttpUtils Tester
 *
 * @author Benedict Jin
 * @since 2016/3/21
 */
public class HttpUtilsTest {

//    @Test
    public void getPlainTestLocal() throws Exception {
        String urlD = "http://localhost:8080/full/delete";
        Map<String, Object> paramsD = new HashMap<>();
        paramsD.put("key", "site");
        System.out.println(HttpUtils.getInstance().deletePlain(urlD, paramsD, null));
        {
            String url = "http://localhost:8080/full/post";
            Map<String, Object> params = new HashMap<>();
            params.put("key", "site");
            params.put("value", "yuzhouwan");
            String response = HttpUtils.getInstance().postPlain(url, params, null);
            assertEquals("", response);
        }
        {
            assertEquals("yuzhouwan", JSON.parseObject(HttpUtils.getInstance()
                    .deletePlain(urlD, paramsD, null), String.class));
        }
        {
            String url = "http://localhost:8080/full/post/consumes";
            Map<String, Object> params = new HashMap<>();
            params.put("key", "site");
            params.put("value", "yuzhouwan.com");
            String response = HttpUtils.getInstance().postPlain(url, params, null);
            assertEquals("", response);
        }
        {
            String url = String.format("http://localhost:8080/full/get?key=%s", "site");
            String response = HttpUtils.getInstance().getPlain(url, null, null);
            assertEquals("yuzhouwan.com", JSON.parseObject(response, String.class));
        }
        {
            String url = "http://localhost:8080/full/put";
            Map<String, Object> params = new HashMap<>();
            params.put("key", "site");
            params.put("value", "http://www.yuzhouwan.com");
            String response = HttpUtils.getInstance().putPlain(url, params, null);
            assertEquals("yuzhouwan.com", JSON.parseObject(response, String.class));
        }
    }
}
