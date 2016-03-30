package com.yuzhouwan.log.storm.base;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * class： StringSpout
 * package： cn.com.dimensoft.storm
 * author：zxh
 * time： 2015年10月12日 上午11:02:59
 * description：产生字符串信息并emit出去
 */
public class StringSpout extends BaseRichSpout {

    /**
     * long:serialVersionUID
     * description：
     */
    private static final long serialVersionUID = 1716894015367004238L;

    private SpoutOutputCollector collector;

    private String[] words;

    public StringSpout(String... words) {
        this.words = words;
    }

    private int index = 0;

    public void open(Map map, TopologyContext context,
                     SpoutOutputCollector collector) {

        this.collector = collector;
    }

    public void nextTuple() {

        if (index < words.length) {
            // 将每行数据传递给下一个组件，即WordSplitBolt
            collector.emit(new Values(words[index++]));
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 声明传递数据的变量名
        declarer.declare(new Fields("msg"));
    }

}