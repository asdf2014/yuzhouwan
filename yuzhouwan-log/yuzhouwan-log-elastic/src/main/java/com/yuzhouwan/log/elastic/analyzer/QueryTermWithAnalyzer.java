package com.yuzhouwan.log.elastic.analyzer;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright @ 2016 zhong-ying Co.Ltd
 * All right reserved.
 * Function：Like Query and Ignore Case with Analyzer
 *
 * @author Benedict Jin
 * @since 2016/3/31 0002
 */
public class QueryTermWithAnalyzer {

    private static final Logger _log = LoggerFactory.getLogger(QueryTermWithAnalyzer.class);

    private volatile Client client;
    private static final String ES_INDEX = "analyzer";
    private static final String ES_TYPE = "attack_info";

    @Before
    public void init() throws Exception {

        if (client == null) {
            synchronized (QueryTermWithAnalyzer.class) {
                if (client == null) {
                    try {

                        _log.info("ElasticSearch : Starting in Client Mode");
                        TransportClient c = TransportClient.builder().settings(loadSettings()).build();
                        //no need to connect with all node :D
                        String hosts = "192.168.1.101";
                        for (String host : hosts.split(",")) {
                            c.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
                        }
                        client = c;
                        _log.info("ElasticSearch : Started in Client Mode");
                    } catch (ElasticsearchException e) {
                        client = null;
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private Settings.Builder loadSettings() throws Exception {
        Settings.Builder settings = Settings.settingsBuilder();

        // set default settings
        settings.put("client.transport.sniff", "true");
        settings.put("cluster.name", "thirdpartpay");

        /**
         * Configuration: https://www.elastic.co/guide/en/elasticsearch/hadoop/current/configuration.html
         */
        settings.put("es.nodes", "192.168.1.101");
        settings.put("es.port", 9200);
        settings.put("es.batch.size.entries", "100");

        settings.build();
        _log.info("Elasticsearch : Settings  " + settings.internalMap().toString());
        return settings;
    }

    @Test
    public void ignoreCaseAndLikeQuery() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("title", "Http"));

        TimeValue timeValue = new TimeValue(1000 * 60);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(ES_INDEX).setTypes(ES_TYPE)
                .setQuery(boolQueryBuilder).setSize(100)
                .setScroll(timeValue);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        List<AttackReport> attackReports = null;
        AttackReport attackReport;
        while (true) {
            SearchHits searchHits = searchResponse.getHits();
            if (searchHits == null) {
                break;
            }
            long totalHits = searchHits.getTotalHits();
            if (totalHits <= 0 || searchHits.getHits().length == 0) {
                break;
            }
            attackReports = new LinkedList<>();
            for (SearchHit searchHit : searchHits) {
                _log.debug(JSON.toJSONString(searchHit));

                Map<String, Object> source = searchHit.getSource();
                attackReport = new AttackReport();
                attackReport.setTitle(source.get("title").toString());
                attackReport.setContent(source.get("content").toString());
                attackReport.setDate(new Date(Long.parseLong(source.get("date").toString())));
                attackReports.add(attackReport);
            }
            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(timeValue)
                    .execute()
                    .actionGet();
        }
        //[{"content":"uk","date":1459401634901,"title":"TCP,Http"},{"content":"usa","date":1459401534901,"title":"tcp,http,cc"}]
        _log.info(JSON.toJSONString(attackReports));

        if (client != null)
            client.close();
    }
}
