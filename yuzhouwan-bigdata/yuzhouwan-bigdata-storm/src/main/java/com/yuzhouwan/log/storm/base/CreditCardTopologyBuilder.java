package com.yuzhouwan.log.storm.base;

import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import org.elasticsearch.storm.EsBolt;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: CreditCardTopologyBuilder
 *
 * @author Benedict Jin
 * @since 2016/3/30
 */
public class CreditCardTopologyBuilder {

    public static StormTopology build() {

        String json1 = "{\"reason\" : \"business\",\"airport\" : \"SFO\"}";
        String json2 = "{\"participants\" : 5,\"airport\" : \"OTP\"}";

        Map<String, Object> conf = new HashMap<>();
        /**
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