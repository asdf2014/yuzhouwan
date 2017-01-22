package com.yuzhouwan.bigdata.druid.util;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.yuzhouwan.common.util.StrUtils.isEmpty;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Druid Restful Utils
 *
 * @author Benedict Jin
 * @since 2017/1/20
 */
public class DruidRestUtils {

    private static final Logger _log = LoggerFactory.getLogger(DruidRestUtils.class);

    private static long DEFEAT_TIMEOUT;
    private static final TimeUnit DEFEAT_UNIT = TimeUnit.SECONDS;

    static {
        String timeOut = PropUtils.getInstance().getProperty("http.timeout.default.second");
        DEFEAT_TIMEOUT = isEmpty(timeOut) ? 10 : Long.parseLong(timeOut);
    }

    public DruidRestUtils() {
    }

    /**
     * Query Druid with Post
     *
     * @param url  <Broker>:<Port, default: 8082>
     * @param json query json
     * @return the result of query
     */
    public static String post(String url, String json) {
        return post(url, json, null, null);
    }

    /**
     * Query Druid with Post
     *
     * @param url     <Broker>:<Port, default: 8082>
     * @param json    query json
     * @param timeOut the timeout of http connection, unit is second
     * @return the result of query
     */
    public static String post(String url, String json, Long timeOut) {
        return post(url, json, timeOut, null);
    }

    /**
     * Query Druid with Post
     *
     * @param url     <Broker>:<Port, default: 8082>
     * @param json    query json
     * @param timeOut the timeout of http connection, unit is second
     * @param charset charset
     * @return the result of query
     */
    public static String post(String url, String json, Long timeOut, String charset) {
        Future<Response> f = null;
        try (AsyncHttpClient asyncHttpClient = new AsyncHttpClient()) {
            AsyncHttpClient.BoundRequestBuilder builder = asyncHttpClient.preparePost(url);
            builder.setBodyEncoding(StrUtils.UTF_8).setBody(json);
            return (f = builder.execute()).get(timeOut == null ? DEFEAT_TIMEOUT : timeOut, DEFEAT_UNIT)
                    .getResponseBody(charset == null ? StrUtils.UTF_8 : charset);
        } catch (Exception e) {
            _log.error(ExceptionUtils.errorInfo(e));
            throw new RuntimeException(e);
        } finally {
            if (f != null) f.cancel(true);
        }
    }
}