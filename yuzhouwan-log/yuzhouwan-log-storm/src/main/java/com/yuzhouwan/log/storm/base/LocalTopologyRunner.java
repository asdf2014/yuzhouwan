package com.yuzhouwan.log.storm.base;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.utils.Utils;

public class LocalTopologyRunner {

    public static void main(String[] args) {
        StormTopology topology = CreditCardTopologyBuilder.build();
        Config config = new Config();
        config.setDebug(true);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("local-topology", config, topology);
        Utils.sleep(30000);
        cluster.killTopology("local-topology");
        cluster.shutdown();
    }
}