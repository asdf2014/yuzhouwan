package com.yuzhouwan.bigdata.elastic.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.yuzhouwan.common.util.PropUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

import static com.yuzhouwan.common.util.StrUtils.isBlank;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：ElasticSearch Utils
 *
 * @author Benedict Jin
 * @since 2017/12/08
 */
public final class ESUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ESUtils.class);

    public static final String ES_SEARCH = "/_search";
    public static final String APPLICATION_JACKSON_SMILE = "application/x-jackson-smile";
    public static final String ES_HOSTS = PropUtils.getInstance().getProperty("es.transport.addresses.list");
    public static final ObjectMapper SMILE_MAPPER;
    public static final SmileFactory SMILE_FACTORY;
    public static final RestClientBuilder REST_CLIENT_BUILDER = getESHosts();

    static {
        SMILE_FACTORY = new SmileFactory();
        SMILE_FACTORY.configure(SmileGenerator.Feature.ENCODE_BINARY_AS_7BIT, false);
        SMILE_FACTORY.delegateToTextual(true);
        SMILE_FACTORY.configure(SmileFactory.Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW, false);
        SMILE_FACTORY.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
        SMILE_FACTORY.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);

        SMILE_MAPPER = new ObjectMapper(SMILE_FACTORY);
        SMILE_MAPPER.getFactory().setCodec(SMILE_MAPPER);
    }

    private ESUtils() {
    }

    public static RestClientBuilder getESHosts() {
        if (isBlank(ES_HOSTS))
            throw new RuntimeException("Cannot get elasticSearch hosts from config! "
                    + "Please check es.hosts config option.");
        String[] hosts = ES_HOSTS.split(",");
        int len;
        if ((len = hosts.length) <= 0)
            throw new RuntimeException("Cannot get elasticSearch hosts from config! "
                    + "Please check es.hosts config option.");
        String host;
        String[] hostAndPort;
        LinkedList<HttpHost> httpHosts = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            host = hosts[i];
            hostAndPort = host.split(":");
            if (hostAndPort.length != 2) {
                LOG.warn("Invalid es host: {}!", host);
                continue;
            }
            httpHosts.add(new HttpHost(hostAndPort[0], Integer.parseInt(hostAndPort[1]), "http"));
        }
        int size;
        HttpHost[] httpHostsArray = new HttpHost[size = httpHosts.size()];
        for (int i = 0; i < size; i++) {
            httpHostsArray[i] = httpHosts.get(i);
        }
        return RestClient.builder(httpHostsArray);
    }

    public static String buildTimestampQuery(Long timestampStart, Long timestampEnd) {
        String timestampPrefix = "        {\n"
                + "          \"range\": {\n"
                + "            \"time\": {\n";
        String timestampPostfix = "            }\n"
                + "          }\n"
                + "        },\n";
        if (timestampStart != null && timestampEnd != null)
            return timestampPrefix
                    + "              \"gte\": \"" + timestampStart + "\",\n"
                    + "              \"lt\": \"" + timestampEnd + "\"\n"
                    + timestampPostfix;
        else if (timestampStart == null && timestampEnd != null)
            return timestampPrefix
                    + "              \"lt\": \"" + timestampEnd + "\"\n"
                    + timestampPostfix;
        else if (timestampStart != null)
            return timestampPrefix
                    + "              \"gte\": \"" + timestampStart + "\"\n"
                    + timestampPostfix;
        else return "";
    }

    public static String buildFieldQuery(String field, Object fieldValue) {
        if (fieldValue == null) return "";
        String fieldValueStr = fieldValue.toString();
        if (isBlank(fieldValueStr)) return "";
        return "        {\n"
                + "          \"match\": {\n"
                + "            \"" + field + "\": \"" + fieldValueStr + "\"\n"
                + "          }\n"
                + "        },\n";
    }

    public static HttpEntity createEntity(String json) {
        try {
            byte[] bytes = SMILE_MAPPER.writeValueAsBytes(SMILE_MAPPER.readValue(json, Object.class));
            return new NByteArrayEntity(bytes, ContentType.create(APPLICATION_JACKSON_SMILE));
        } catch (Exception e) {
            LOG.error("Cannot create entity!", e);
            throw new RuntimeException("Cannot create entity!", e);
        }
    }
}
