package com.yuzhouwan.bigdata.hbase.util;

import com.yuzhouwan.common.util.StrUtils;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
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

    private static final Logger _log = LoggerFactory.getLogger(HBaseUtils.class);

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
        for (int i = startKey; i < endKey; i += stepSize)
            strBuilder.append("'").append(StrUtils.fillWitchZero(i, fill)).append("', ");
        return strBuilder.append("]").toString().replaceAll(", ]", "]");
    }

    public static boolean createTable(Admin admin, HTableDescriptor table, byte[][] splits) throws IOException {
        try {
            admin.createTable(table, splits);
            return true;
        } catch (TableExistsException e) {
            _log.info("table " + table.getNameAsString() + " already exists");
            return false;
        }
    }

    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger highestKey = new BigInteger(endKey, 16);
        BigInteger range = highestKey.subtract(lowestKey);
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for (int i = 0; i < numRegions - 1; i++)
            splits[i] = String.format("%016x",
                    lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)))).getBytes();
        return splits;
    }

    public static LinkedList<String> splitJmxRegion(String aim) {
        return StrUtils.splitMulti(aim, "namespace_", "_table_", "_region_", "_metric_");
    }
}
