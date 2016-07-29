package com.yuzhouwan.bigdata.elastic.analyzer;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Settings.Builder loadSettings() throws Exception {
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
