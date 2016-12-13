package com.yuzhouwan.bigdata.hbase.util;

import com.yuzhouwan.common.util.StrUtils;

import java.util.LinkedList;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：HBase Utils
 *
 * @author Benedict Jin
 * @since 2016/11/21
 */
public class HBaseUtils {

    /**
     * [startKey, endKey)
     *
     * @param startKey
     * @param endKey
     * @param stepSize
     * @param fill
     * @return
     */
    public static String generateSplitKeys(int startKey, int endKey, int stepSize, int fill) {

        StringBuilder strBuilder = new StringBuilder("SPLITS => [");
        for (int i = startKey; i < endKey; i += stepSize) {

            strBuilder.append("'").append(StrUtils.fillWitchZero(i, fill)).append("', ");
        }
        strBuilder.append("]");
        return strBuilder.toString().replaceAll(", ]", "]");
    }

    public static LinkedList<String> splitJmxRegion(String aim) {
        return StrUtils.splitMulti(aim, "namespace_", "_table_", "_region_", "_metric_");
    }
}
