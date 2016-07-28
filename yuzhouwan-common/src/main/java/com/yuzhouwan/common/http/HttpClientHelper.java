package com.yuzhouwan.common.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CodingErrorAction;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: HttpClientHelper
 *
 * @author Benedict Jin
 * @since 2016/3/21 0030
 */
public class HttpClientHelper {

    private static final Logger _log = LoggerFactory.getLogger(HttpClientHelper.class);

    private static HttpClientHelper helper;

    private static String userAgent;

    private final static int TIMEOUT_CONNECTION = 60 * 1000;
    private final static int TIMEOUT_SOCKET = 60 * 1000;
    private final static int MAX_TOTAL = 200;
    private final static int MAX_RETRY = 5;
    private final static int MAX_ROUTE_TOTAL = 20;

    private CloseableHttpClient httpClient;
    private HttpClientContext httpClientContext;
    private TrustManager[] trustManagers = new TrustManager[1];
    private SSLConnectionSocketFactory sslSocketFactory;

    private HttpClientHelper() {
    }

    public static HttpClientHelper getInstance() {
        if (helper == null) {
            helper = new HttpClientHelper();
            helper.initialize();
        }
        return helper;
    }

    public static void destory() {
        try {
            helper.httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        // Connection配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();

        // 设置重定向策略
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

        // 重试策略
        HttpRequestRetryHandler retryHandler = (IOException exception, int executionCount, HttpContext context) -> {
            if (executionCount >= MAX_RETRY) {
                // Do not retry if over max retry count
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // Retry if the server dropped connection on us
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                // Do not retry on SSL handshake exception
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        };

        //覆盖证书检测过程（用以非CA的https链接）
        trustManagers[0] = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[0], trustManagers, new SecureRandom());
            sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_SOCKET)
                .setConnectTimeout(TIMEOUT_CONNECTION)
                .setConnectionRequestTimeout(TIMEOUT_CONNECTION).build();

        // 创建httpclient连接池
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create().
                        register("http", PlainConnectionSocketFactory.getSocketFactory()).
                        register("https", sslSocketFactory).build());
        httpClientConnectionManager.setMaxTotal(MAX_TOTAL); // 设置连接池线程最大数量
        httpClientConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_TOTAL); // 设置单个路由最大的连接线程数量
        httpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);

        /* 默认cookie */
        CookieStore cookieStore = new BasicCookieStore();

        // httpclient上下文
        httpClientContext = HttpClientContext.create();
        httpClientContext.setCookieStore(cookieStore);

        // 初始化httpclient客户端
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setRedirectStrategy(redirectStrategy)
                .setRetryHandler(retryHandler);

        if (this.userAgent != null) {
            httpClientBuilder.setUserAgent(userAgent);
        }
        httpClient = httpClientBuilder.build();

    }

    private MultipartEntityBuilder processBuilderParams(Map<String, Object> params) {

        ContentType contentType = ContentType.TEXT_PLAIN.withCharset(Consts.UTF_8);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (params != null) {
            _log.debug(params.toString());
            Set<Entry<String, Object>> sets = params.entrySet();
            for (Entry<String, Object> entry : sets) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                } else if (value instanceof File) {
                    builder.addBinaryBody(entry.getKey(), (File) value);
                } else if (value instanceof CharSequence) {
                    builder.addTextBody(entry.getKey(), value.toString(), contentType);
                } else {
                    builder.addTextBody(entry.getKey(), JSON.toJSONString(value), contentType);
                }
            }
        }
        return builder;
    }

    public InputStream postStream(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return this.post(url, params, headers).getStream();
    }

    public String postPlain(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return this.post(url, params, headers).getText();
    }

    public String postPlain(String url, HttpEntity entityParam, Map<String, String> headers) throws Exception {
        return this.post(url, entityParam, headers).getText();
    }

    public HttpResponse post(String url, HttpEntity entity, Map<String, String> headers) throws Exception {
        HttpPost post = new HttpPost(url);
        _log.debug(url);
        post.setEntity(entity);
        processHeader(post, headers);
        return doPost(post);
    }

    public HttpResponse post(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        HttpPost post = new HttpPost(url);
        _log.debug(url);
        MultipartEntityBuilder builder = processBuilderParams(params);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Consts.UTF_8);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        processHeader(post, headers);
        return doPost(post);
    }

    private HttpResponse doPost(HttpPost post) throws Exception {
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post, httpClientContext);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setCode(statusCode);
            if (httpResponse.isError()) {
                _log.error("error response status {},method post {} ", statusCode, post.toString());
                throw new RuntimeException();
            }
            httpResponse.setBytes(EntityUtils.toByteArray(response.getEntity()));
            httpResponse.setContentType(response.getEntity().getContentType().getValue());
            return httpResponse;
        } catch (Exception e) {
            throw e;
        } finally {
            release(post, response, response != null ? response.getEntity() : null);
        }
    }

    public InputStream getStream(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return this.get(url, params, headers).getStream();
    }

    public String getPlain(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        return this.get(url, params, headers).getText();
    }

    public HttpResponse get(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        CloseableHttpResponse response = null;
        HttpGet get = null;
        try {
            _log.debug(url);
            get = new HttpGet(processURL(url, params));
            processHeader(get, headers);
            response = httpClient.execute(get, httpClientContext);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setCode(statusCode);
            if (httpResponse.isError()) {
                _log.error("error response status {},method get {} ", statusCode, get.toString());
                throw new RuntimeException();
            }
            httpResponse.setBytes(EntityUtils.toByteArray(response.getEntity()));
            httpResponse.setContentType(response.getEntity().getContentType().getValue());
            return httpResponse;
        } catch (Exception e) {
            throw e;
        } finally {
            release(get, response, response != null ? response.getEntity() : null);
        }
    }

    private void processHeader(HttpRequestBase entity, Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Entry<String, String> entry : headers.entrySet()) {
            entity.addHeader(entry.getKey(), entry.getValue());
        }
    }

    private String processURL(String processUrl, Map<String, Object> params) {
        if (params == null) {
            return processUrl;
        }
        _log.debug(params.toString());
        StringBuilder url = new StringBuilder(processUrl);
        if (url.indexOf("?") < 0)
            url.append('?');

        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
        }
        return url.toString().replace("?&", "?");
    }

    private void release(HttpRequestBase request, CloseableHttpResponse response, HttpEntity entity) {
        try {
            if (request != null) {
                request.releaseConnection();
            }
            if (entity != null) {
                EntityUtils.consume(entity);
            }
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUserAgent() {
        return userAgent;
    }

    public static void setUserAgent(String userAgent) {
        HttpClientHelper.userAgent = userAgent;
    }
}
