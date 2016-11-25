package com.yuzhouwan.bigdata.hadoop.util;

import com.yuzhouwan.common.util.PropUtils;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：com.yuzhouwan.bigdata.hadoop.util
 *
 * @author Benedict Jin
 * @since 2016/11/25
 */
public class HadoopUtils {

    public static void setUpEnv() {
        PropUtils p = PropUtils.getInstance();
        System.setProperty("hadoop.home.dir", p.getProperty("hadoop.home.dir"));

        System.getProperties().setProperty("HADOOP_USER_NAME", p.getProperty("HADOOP_USER_NAME"));
        System.getProperties().setProperty("HADOOP_GROUP_NAME", p.getProperty("HADOOP_GROUP_NAME"));
    }
}