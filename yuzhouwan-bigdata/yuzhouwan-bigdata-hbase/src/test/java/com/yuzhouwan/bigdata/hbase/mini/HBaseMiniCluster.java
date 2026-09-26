package com.yuzhouwan.bigdata.hbase.mini;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseTestingUtil;
import org.apache.hadoop.hbase.SingleProcessHBaseCluster;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapred.*;
import org.junit.Test;

import java.io.IOException;

import static org.apache.hadoop.hbase.HBaseCommonTestingUtil.BASE_TEST_DIRECTORY_KEY;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: HBase Mini Cluster
 *
 * @author Benedict Jin
 * @since 2016/9/7
 */
public class HBaseMiniCluster {

    /**
     * java.util.UnknownFormatConversionException: Conversion = 'J'
     */
    private static final String BASE_PATH = "E:/work/hbase";
    private static final String TABLE_NAME = "yuzhouwan";

    @Test
    public void miniCluster() throws Exception {
        /*
         * https://github.com/apache/hbase/blob/master/hbase-common/src/test/java/org/apache/hadoop/hbase/HBaseCommonTestingUtil.java
         */
        System.setProperty(BASE_TEST_DIRECTORY_KEY, BASE_PATH.concat("data"));
        System.setProperty("hadoop.home.dir", "D:/apps/hadoop/hadoop-2.7.3/");

        HBaseTestingUtil hbaseTestingUtility = hbaseOperation();
        mapReduce(hbaseTestingUtility);
    }

    private void mapReduce(HBaseTestingUtil hbaseTestingUtility) throws IOException {
        hbaseTestingUtility.startMiniMapReduceCluster();
        FileSystem fs = FileSystem.get(hbaseTestingUtility.getConfiguration());

        Path p = new Path("/tmp/mapreduce/");
        fs.mkdirs(p);
        fs.copyFromLocalFile(new Path("/tmp/mapreduce/file01"), p);
        fs.copyFromLocalFile(new Path("/tmp/mapreduce/file02"), p);

        JobConf conf = new JobConf(hbaseTestingUtility.getConfiguration(), WordCount.class);
        conf.setJobName("minCluster");
        FileInputFormat.setInputPaths(conf, new Path("/tmp/mapreduce/"));
        FileOutputFormat.setOutputPath(conf, new Path("/tmp/result"));

        JobClient.runJob(conf);
    }

    private HBaseTestingUtil hbaseOperation() throws Exception {

        HBaseTestingUtil hbaseTestingUtility = new HBaseTestingUtil();
        /*
         * # fsOwner's name is Benedict Jin, will throw exception: Illegal character in path at index 42
         * hbaseTestingUtility.getTestFileSystem().setOwner(new Path(BASE_PATH.concat("/owner")), "Benedict Jin", "supergroup");
         */
        SingleProcessHBaseCluster hbaseCluster = hbaseTestingUtility.startMiniCluster();

        hbaseTestingUtility.createTable(TableName.valueOf(TABLE_NAME), Bytes.toBytes("context"));
        hbaseTestingUtility.deleteTable(TableName.valueOf(TABLE_NAME));

        Configuration config = hbaseCluster.getConf();
        Connection conn = ConnectionFactory.createConnection(config);
        Admin hbaseAdmin = conn.getAdmin();

        TableDescriptor desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(TABLE_NAME))
                .setColumnFamily(ColumnFamilyDescriptorBuilder.of("context"))
                .build();
        hbaseAdmin.createTable(desc);
        return hbaseTestingUtility;
    }
}
