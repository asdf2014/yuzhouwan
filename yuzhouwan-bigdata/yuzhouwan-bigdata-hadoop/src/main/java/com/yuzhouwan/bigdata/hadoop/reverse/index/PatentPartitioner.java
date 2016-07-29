package com.yuzhouwan.bigdata.hadoop.reverse.index;

import com.yuzhouwan.bigdata.hadoop.util.ConfUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Copyright @ 2015 patent.com
 * All right reserved.
 * Function：指定最终的结果文件数量的 Partitioner组件
 *
 * @author Benedict Jin
 * @since 2016/3/31
 */
public class PatentPartitioner extends Partitioner<Text, Text> {

    /**
     * Partitioner 的核心方法
     *
     * @param key
     * @param value
     * @param numPartitions
     * @return
     */
    @Override
    public int getPartition(Text key, Text value, int numPartitions) {

        //这里可以暂时不指定（使用 Hadoop默认分区），到后面能掌握好 Partition的级别程度，可以加入进行调整
        return ConfUtil.getMax();
    }

}
