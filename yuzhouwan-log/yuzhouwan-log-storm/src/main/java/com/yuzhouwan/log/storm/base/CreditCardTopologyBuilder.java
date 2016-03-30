package com.yuzhouwan.log.storm.base;

import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import org.elasticsearch.storm.EsBolt;

import java.util.HashMap;
import java.util.Map;

public class CreditCardTopologyBuilder {

    public static StormTopology build() {

        String json1 = "{\"reason\" : \"business\",\"airport\" : \"SFO\"}";
        String json2 = "{\"participants\" : 5,\"airport\" : \"OTP\"}";

        Map conf = new HashMap();
        conf.put("es.input.json", "true");

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("json-spout", new StringSpout(json1, json2));
        builder.setBolt("es-bolt", new EsBolt("storm/json-trips", conf)).shuffleGrouping("json-spout");

        return builder.createTopology();
    }
}