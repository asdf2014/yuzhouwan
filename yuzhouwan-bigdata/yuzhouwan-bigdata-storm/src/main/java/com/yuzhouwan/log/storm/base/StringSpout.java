package com.yuzhouwan.log.storm.base;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: StringSpout
 *
 * @author Benedict Jin
 * @since 2016/3/30
 */
public class StringSpout extends BaseRichSpout {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringSpout.class);

    private SpoutOutputCollector collector;

    private final String[] words;

    public StringSpout(String... words) {
        this.words = words;
    }

    public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    public void nextTuple() {
        for (String word : words) collector.emit(new Values(word));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("msg"));
    }
}
