package com.yuzhouwan.bigdata.hbase.util;

import com.yuzhouwan.common.util.StrUtils;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：HBase Utils
 *
 * @author Benedict Jin
 * @since 2016/11/21
 */
public class HBaseUtils {

    public static String generateSplitKeys(int startKey, int endKey, int stepSize, int fill) {

        StringBuilder strBuilder = new StringBuilder("SPLITS => [");
        for (int i = startKey; i < endKey; i += stepSize) {

            strBuilder.append("'").append(StrUtils.fillWitchZero(i, fill)).append("', ");
        }
        strBuilder.append("]");
        return strBuilder.toString().replaceAll(", ]", "]");
    }

}
