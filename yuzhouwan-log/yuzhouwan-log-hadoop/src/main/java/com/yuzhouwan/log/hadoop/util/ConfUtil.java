package com.yuzhouwan.log.hadoop.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Copyright @ 2015 patent.com
 * All right reserved.
 * Function：针对不同任务，利用配置定制化处理流程
 *
 * @author Benedict Jin
 * @since 2016/3/31 0023
 */
public class ConfUtil {

    //用于读取配置参数
    private static Properties p;

    //用于从 HDFS分布式系统获取文件
    private static FileSystem fs;

    //最大分区，即 结果文件的数量
    private static int max;

    static {
        //实例化
        p = new Properties();
        try {
            //连接到 HDFS
            fs = FileSystem.get(new URI("hdfs://ns1"), // :9000 - RPC
                    new Configuration(), "root"); // not security
        } catch (IOException e) {
            // 处理 IO异常栈
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // 通讯中断异常栈处理
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            // 处理 URI解析异常
            throw new RuntimeException(e);
        }

        InputStream in;
        try {
            // 读取 HDFS上配置文件
            in = fs.open(new Path("/patent.conf"));
        } catch (IllegalArgumentException e) {
            // 处理参数异常
            throw new RuntimeException(e);
        } catch (IOException e) {
            // 处理 IO异常栈
            throw new RuntimeException(e);
        }

        try {
            //载入 HDFS文件流
            p.load(in);
        } catch (FileNotFoundException e) {
            // 处理文件无法找到异常
            throw new RuntimeException(e);
        } catch (IOException e) {
            // 处理 IO异常栈
            throw new RuntimeException(e);
        }
        //获取配置，如果没有，则给默认最大分区为 10
        max = Integer.parseInt(p.getProperty("max", "10"));
    }

    /**
     * 返回最大分区的值
     *
     * @return
     */
    public static int getMax() {
        return max;
    }
}
