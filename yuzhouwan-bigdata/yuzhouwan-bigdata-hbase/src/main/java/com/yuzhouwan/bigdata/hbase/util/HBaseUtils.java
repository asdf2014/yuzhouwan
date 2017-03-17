package com.yuzhouwan.bigdata.hbase.util;

import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;
import com.yuzhouwan.common.util.TimeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.RegionLoad;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;

import static com.yuzhouwan.common.util.StrUtils.*;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：HBase Utils
 *
 * @author Benedict Jin
 * @since 2016/11/21
 */
public class HBaseUtils {

    private static final Logger _log = LoggerFactory.getLogger(HBaseUtils.class);
    private static final String NAMESPACE_DEFAULT = "default";

    private static volatile HBaseUtils instance;
    private static Configuration configuration;

    public static HBaseUtils getInstance() {
        if (instance == null) synchronized (HBaseUtils.class) {
            if (instance == null)
                init();
        }
        return instance;
    }

    private static void init() {
        setUpEnv();

        instance = new HBaseUtils();
        configuration = HBaseConfiguration.create();

        PropUtils p = PropUtils.getInstance();
        String zookeeperQuorum = p.getProperty("metric.hbase.zookeeper.quorum");
        if (zookeeperQuorum == null) {
            _log.error("hbase.zookeeper.quorum not configZK in prop/hbase.cluster.infos.properties");
            System.exit(-1);
        }
        configZK(zookeeperQuorum, p.getProperty("metric.hbase.zookeeper.property.clientPort"),
                p.getProperty("metric.zookeeper.znode.parent"));
    }

    public static void setUpEnv() {
        PropUtils p = PropUtils.getInstance();
        String HADOOP_HOME_DIR = p.getProperty("hadoop.home.dir");
        if (!isEmpty(HADOOP_HOME_DIR))
            System.setProperty("hadoop.home.dir", HADOOP_HOME_DIR);

        String HADOOP_USER_NAME = p.getProperty("HADOOP_USER_NAME");
        if (!isEmpty(HADOOP_USER_NAME))
            System.setProperty("HADOOP_USER_NAME", HADOOP_USER_NAME);

        String HADOOP_GROUP_NAME = p.getProperty("HADOOP_GROUP_NAME");
        if (!isEmpty(HADOOP_GROUP_NAME))
            System.setProperty("HADOOP_GROUP_NAME", HADOOP_GROUP_NAME);
    }

    private static void configZK(String zookeeperQuorum, String port, String znodeParent) {
        configuration.set("metric.hbase.zookeeper.quorum", zookeeperQuorum);
        configuration.set("metric.hbase.zookeeper.property.clientPort", port);
        configuration.set("metric.zookeeper.znode.parent", znodeParent);
    }

    public static boolean createTable(Admin admin, HTableDescriptor table, byte[][] splits) {
        if (admin == null || table == null || splits == null) return false;
        try {
            admin.createTable(table, splits);
        } catch (TableExistsException e) {
            _log.error("Table {} already exists!", table.getNameAsString());
            return false;
        } catch (Exception e) {
            _log.error(ExceptionUtils.errorInfo(e));
            throw new RuntimeException(e);
        }
        return true;
    }

    public Date getClusterStartTime() {
        try {
            return new Date(new HBaseAdmin(configuration).getClusterStatus().getMaster().getStartcode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClusterStartTimeStr() {
        return TimeUtils.date2Str(getClusterStartTime());
    }

    public static String getNameSpace(RegionLoad region) {
        String tableName;
        if (isEmpty(tableName = getTableName(region))) return null;
        return tableName.split(COLON)[0];
    }

    public static String getSingleTableName(RegionLoad region) {
        String tableName;
        if (isEmpty(tableName = getTableName(region))) return null;
        return tableName.split(COLON)[1];
    }

    public static String getTableName(RegionLoad region) {
        if (region == null) return null;
        // return extractTableName(HRegionInfo.encodeRegionName(region.getName()));
        return extractTableName(region.getNameAsString());
    }

    public static String extractTableName(String regionName) {
        String[] rn = regionName.split(",");
        if (rn.length != 3) return null;
        String[] t1 = rn[0].split(COLON);
        String namespace, tableName;
        if (t1.length == 2) {
            namespace = t1[0];
            tableName = t1[1];
        } else {
            namespace = NAMESPACE_DEFAULT;
            tableName = rn[0];
        }
        return namespace.concat(COLON).concat(tableName);
    }

    public static LinkedList<String> splitJmxRegion(String aim) {
        return StrUtils.splitMulti(aim, "namespace_", "_table_", "_region_", "_metric_");
    }

    public static String removeEndKey(String regionName) {
        if (isEmpty(regionName) || !regionName.contains(",")) return regionName;
        return regionName.substring(0, regionName.lastIndexOf(",")).trim();
    }

    /**
     * @param regionServerName like: slave06-sit.yuzhouwan.com,60020,1478326494532
     * @return hostname, like: slave06-sit.yuzhouwan.com
     */
    public static String extractHostName(String regionServerName) {
        return isEmpty(regionServerName) ? "" : regionServerName.substring(0, regionServerName.indexOf(COMMA));
    }

    public static String removeTimestamp(String regionServerName) {
        if (isEmpty(regionServerName)) return regionServerName;
        return regionServerName.substring(0, regionServerName.lastIndexOf(COMMA));
    }

    public static String extractTimestamp(String regionServerName) {
        if (isEmpty(regionServerName)) return regionServerName;
        return regionServerName.substring(regionServerName.lastIndexOf(COMMA) + 1);
    }

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

    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions - 1][];
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger highestKey = new BigInteger(endKey, 16);
        BigInteger regionIncrement = highestKey.subtract(lowestKey).divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for (int i = 0; i < numRegions - 1; i++)
            splits[i] = String.format("%016x", lowestKey.add(
                    regionIncrement.multiply(BigInteger.valueOf(i)))).getBytes();
        return splits;
    }
}