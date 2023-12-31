package com.yuzhouwan.bigdata.hadoop.util;

import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Hadoop Utils
 *
 * @author Benedict Jin
 * @since 2016/11/25
 */
public final class HadoopUtils {

    private HadoopUtils() {
    }

    public static void setUpEnv() {
        PropUtils p = PropUtils.getInstance();
        String HADOOP_HOME_DIR;
        if (!StrUtils.isEmpty(HADOOP_HOME_DIR = p.getProperty("hadoop.home.dir")))
            System.setProperty("hadoop.home.dir", HADOOP_HOME_DIR);

        String HADOOP_USER_NAME;
        if (!StrUtils.isEmpty(HADOOP_USER_NAME = p.getProperty("HADOOP_USER_NAME")))
            System.setProperty("HADOOP_USER_NAME", HADOOP_USER_NAME);

        String HADOOP_GROUP_NAME;
        if (!StrUtils.isEmpty(HADOOP_GROUP_NAME = p.getProperty("HADOOP_GROUP_NAME")))
            System.setProperty("HADOOP_GROUP_NAME", HADOOP_GROUP_NAME);
    }
}
