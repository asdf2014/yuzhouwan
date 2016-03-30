package com.yuzhouwan.log.storm.base;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

public class StringSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;

    private String[] words;

    public StringSpout(String... words) {
        this.words = words;
    }

    public void open(Map map, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.collector = collector;
    }

    public void nextTuple() {

        for (String word : words) {
            collector.emit(new Values(word));
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("msg"));
    }

}