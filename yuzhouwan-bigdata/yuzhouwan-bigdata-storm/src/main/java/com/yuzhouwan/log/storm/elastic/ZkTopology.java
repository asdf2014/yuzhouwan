package com.yuzhouwan.log.storm.elastic;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import org.elasticsearch.storm.EsBolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: ZkTopology
 *
 * @author Benedict Jin
 * @since 2016/3/30
 */
public final class ZkTopology {

    private static final Logger LOG = LoggerFactory.getLogger(ZkTopology.class);

    private ZkTopology() {
    }

    public static void main(String[] args) {

        //这个地方其实就是kafka配置文件里边的zookeeper.connect这个参数，可以去那里拿过来
        String brokerZkStr = "10.100.90.201:2181/kafka_online_sample";
        String brokerZkPath = "/brokers";
        ZkHosts zkHosts = new ZkHosts(brokerZkStr, brokerZkPath);

        String topic = "mars-wap";
        //以下：将offset汇报到哪个zk集群,相应配置
        String offsetZkServers = "10.199.203.169";
        String offsetZkPort = "2181";
        List<String> zkServersList = new ArrayList<>();
        zkServersList.add(offsetZkServers);
        //汇报offset信息的root路径
        String offsetZkRoot = "/stormExample";
        //存储该spout id的消费offset信息,譬如以topoName来命名
        String offsetZkId = "storm-example";

        SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, topic, offsetZkRoot, offsetZkId);
        kafkaConfig.zkPort = Integer.parseInt(offsetZkPort);
        kafkaConfig.zkServers = zkServersList;
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        KafkaSpout spout = new KafkaSpout(kafkaConfig);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", spout, 1);
        builder.setBolt("bolt", new EsBolt("storm/docs"), 1).shuffleGrouping("spout");

        Config config = new Config();
        config.put("es.index.auto.create", "true");

        if (args.length > 0) {
            try {
                StormSubmitter.submitTopology("storm-kafka-example", config, builder.createTopology());
            } catch (Exception e) {
                LOG.error("error: {}", e.getMessage());
            }
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test", config, builder.createTopology());
        }
    }
}
