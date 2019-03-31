package com.yuzhouwan.bigdata.elastic.client;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhouwan.common.util.PropUtils;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;

import static com.yuzhouwan.bigdata.elastic.util.ESUtils.*;
import static com.yuzhouwan.common.util.StrUtils.isBlank;
import static com.yuzhouwan.common.util.StrUtils.isNotBlank;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：ElasticSearch Utils Test
 *
 * @author Benedict Jin
 * @since 2017/12/08
 */
public class ESUtilsTest extends ElasticSearchClientBaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(ESUtilsTest.class);
    private static final String ES_INDEX = PropUtils.getInstance().getProperty("es.index.name").concat(ES_SEARCH);

    public static String buildQuery(Long timestampStart, Long timestampEnd, String uuid) {
        String query = buildTimestampQuery(timestampStart, timestampEnd) + buildFieldQuery("uuid", uuid);
        if (isNotBlank(query) && query.endsWith("},\n")) query = query.substring(0, query.length() - 4).concat("}\n");
        return query;
    }

    @Disabled
    @Test
    public void test() {
        try (RestClient esClient = REST_CLIENT_BUILDER.build()) {
            final ObjectMapper smileMapper = new ObjectMapper(SMILE_FACTORY);
            final HttpEntity entity = createEntity("{\n"
                    + "  \"from\": " + 0 + ",\n"
                    + "  \"size\": " + 10 + ",\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + buildQuery(1501813000005L, 1501813999999L, "7dcaa4ff-58fd-4a0d-842f-843d108c1067")
                    + "      ]\n"
                    + "    }\n"
                    + "  }\n"
                    + "}");
            Response response = esClient.performRequest("GET", ES_INDEX, Collections.emptyMap(), entity);
            final JsonNode jsonNode = smileMapper.readTree(response.getEntity().getContent());
            JsonNode hits;
            if (jsonNode == null || (hits = jsonNode.get("hits")).size() == 0) return;
            String nodeStr;
            Object resultBean;
            LinkedList<Object> objs = new LinkedList<>();
            for (JsonNode node : hits.get("hits")) {
                node = node.get("_source");
                nodeStr = node.toString();
                if (isBlank(nodeStr)) continue;
                try {
                    resultBean = JSON.parseObject(nodeStr);
                    objs.add(resultBean);
                } catch (Exception e) {
                    LOG.warn("Cannot parse jsonNode: {}!", node);
                }
            }
            LOG.info(JSON.toJSONString(objs));
        } catch (Exception e) {
            throw new RuntimeException("Cannot query events from es!", e);
        }
    }
}
