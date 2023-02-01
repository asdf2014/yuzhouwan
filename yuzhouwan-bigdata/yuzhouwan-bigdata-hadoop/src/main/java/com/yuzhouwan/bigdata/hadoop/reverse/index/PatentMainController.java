package com.yuzhouwan.bigdata.hadoop.reverse.index;

import com.yuzhouwan.bigdata.hadoop.util.ConfUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：控制整个 Patent处理任务的 Job组件
 *
 * @author Benedict Jin
 * @since 2016/3/31
 */
public class PatentMainController {

    /**
     * Main方法里面，设置了 Patent任务流程，Mapper ->Combiner ->Reducer ->Partitioner.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //配置 Job，并完成初始化
        Job job = Job.getInstance(new Configuration());

        //指定 Job的主类
        job.setJarByClass(PatentMainController.class);

        //指定 Job的 Mapper组件
        job.setMapperClass(PatentMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //指定 Job的数据输入地址
        FileInputFormat.setInputPaths(job, new Path(args[0]));

        //指定 Job的 Combiner组件
        job.setCombinerClass(InverseIndexByKeywordCombiner.class);
        job.setReducerClass(InverseIndexByKeywordCombiner.class);

        //指定 Job的 Reducer组件
        job.setReducerClass(PatentReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //指定 Job的数据输出地址
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setPartitionerClass(PatentPartitioner.class);
        //指定最大的 Task数量
        job.setNumReduceTasks(ConfUtil.getMax());

        //提交并等待执行完成
        job.waitForCompletion(true);
    }
}
