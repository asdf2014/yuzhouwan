package com.yuzhouwan.log.elastic.analyzer;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Benedict Jin on 2016/5/30.
 */
public class QueryTermWithAnalyzerTest {

    private static final Logger _log = LoggerFactory.getLogger(QueryTermWithAnalyzerTest.class);

    private QueryTermWithAnalyzer query = new QueryTermWithAnalyzer();
    private static final String ES_INDEX = "analyzer";
    private static final String ES_TYPE = "attack_info";

//    @Before
    public void init() throws Exception {

        if (query.getClient() == null) {
            synchronized (QueryTermWithAnalyzer.class) {
                if (query.getClient() == null) {
                    try {

                        _log.info("ElasticSearch : Starting in Client Mode");
                        TransportClient c = TransportClient.builder().settings(query.loadSettings()).build();
                        //no need to connect with all node :D
                        String hosts = "192.168.1.101";
                        for (String host : hosts.split(",")) {
                            c.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
                        }
                        query.setClient(c);
                        _log.info("ElasticSearch : Started in Client Mode");
                    } catch (ElasticsearchException e) {
                        query.setClient(null);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

//    @Test
    public void ignoreCaseAndLikeQuery() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("title", "Http"));

        TimeValue timeValue = new TimeValue(1000 * 60);
        SearchRequestBuilder searchRequestBuilder = query.getClient()
                .prepareSearch(ES_INDEX).setTypes(ES_TYPE)
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
            searchResponse = query.getClient().prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(timeValue)
                    .execute()
                    .actionGet();
        }
        //[{"content":"uk","date":1459401634901,"title":"TCP,Http"},{"content":"usa","date":1459401534901,"title":"tcp,http,cc"}]
        _log.info(JSON.toJSONString(attackReports));

        if (query.getClient() != null)
            query.getClient().close();
    }
}
