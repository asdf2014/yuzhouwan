package com.yuzhouwan.log.storm.base;

import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.elasticsearch.storm.EsBolt;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: CreditCardTopologyBuilder
 *
 * @author Benedict Jin
 * @since 2016/3/30
 */
public final class CreditCardTopologyBuilder {

    private CreditCardTopologyBuilder() {
    }

    public static StormTopology build() {

        String json1 = "{\"reason\" : \"business\",\"airport\" : \"SFO\"}";
        String json2 = "{\"participants\" : 5,\"airport\" : \"OTP\"}";

        Map<String, Object> conf = new HashMap<>();
        /*
         * Configuration: https://www.elastic.co/guide/en/elasticsearch/hadoop/current/configuration.html
         */
        conf.put("es.nodes", "192.168.1.101");
        conf.put("es.port", 9200);
        conf.put("es.input.json", "true");
        conf.put("es.batch.size.entries", "100");

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("json-spout", new StringSpout(json1, json2));
        builder.setBolt("es-bolt", new EsBolt("storm/json-trips", conf)).shuffleGrouping("json-spout");

        return builder.createTopology();
    }
}
