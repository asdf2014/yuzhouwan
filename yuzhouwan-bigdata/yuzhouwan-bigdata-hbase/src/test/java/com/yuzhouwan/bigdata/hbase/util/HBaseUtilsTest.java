package com.yuzhouwan.bigdata.hbase.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.yuzhouwan.bigdata.hbase.util.HBaseUtils.extractTimestamp4Region;
import static com.yuzhouwan.bigdata.hbase.util.HBaseUtils.removeEndKey;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：HBase Utils Test
 *
 * @author Benedict Jin
 * @since 2016/11/21
 */
public class HBaseUtilsTest {

    /*
     关闭自动分区
     alter 'yuzhouwan', {METHOD => 'table_att', SPLIT_POLICY => 'org.apache.hadoop.hbase.regionserver.DisabledRegionSplitPolicy'}
      */
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseUtilsTest.class);

    @Test
    public void generateSplitKeysTest() {

        assertEquals("SPLITS => ['-128', '-112', '-96', '-80', '-64', '-48', '-32', '-16', '0', " +
                        "'16', '32', '48', '64', '80', '96', '112', '128']",
                HBaseUtils.generateSplitKeys(-128, 128, 16, 0));

        assertEquals("SPLITS => ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', " +
                        "'14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', " +
                        "'31', '32', '33', '34', '35', '36', '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', " +
                        "'48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59', '60', '61', '62', '63', '64', " +
                        "'65', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80']",
                HBaseUtils.generateSplitKeys(0, 80, 1, 2));

        assertEquals("SPLITS => ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10']",
                HBaseUtils.generateSplitKeys(1, 10, 1, 0));
    }

    @Test
    public void getHexSplitsTest() {
//        byte[][] splits = HBaseUtils.getHexSplits("0000000000", "ffffffffffffffffffffffffffffffffffffffff", 400_0000);
        byte[][] splits = HBaseUtils.getHexSplits("0", "f", 16);
        LOGGER.debug("Splits size: {}", splits.length);
        for (byte[] split : splits) {
            LOGGER.debug("Hex Splits: {}", JSON.toJSONString(Arrays.asList(split)));
        }
    }

    @Test
    public void removeEndKeyTest() {
        String regionName = "hbase:meta,,1..";
        assertEquals("hbase:meta,", removeEndKey(regionName));
        regionName = "hbase:meta,0,1..";
        assertEquals("hbase:meta,0", removeEndKey(regionName));
        regionName = "";
        assertEquals("", removeEndKey(regionName));
        regionName = " ";
        assertEquals(" ", removeEndKey(regionName));
    }

    @Test
    public void extraTimestampTest() {
        {
            String regionName = "hbase:acl,400309eb,1495941314418.4cc9912158f76cdabb253c7dbe9059c2.";
            assertEquals("1495941314418", extractTimestamp4Region(regionName));
            regionName = "hbase:acl,,1495941314418..";
            assertEquals("1495941314418", extractTimestamp4Region(regionName));
            regionName = ":,,1495941314418..";
            assertEquals("1495941314418", extractTimestamp4Region(regionName));
            regionName = ":,,..";
            assertEquals("", extractTimestamp4Region(regionName));
        }
        {
            String regionName = "hbase:acl,40030,9eb,1495941314418.4cc9912158f76cdabb253c7dbe9059c2.";
            assertEquals("1495941314418", extractTimestamp4Region(regionName));
            regionName = "hbase:acl,,1495941314418..";
            assertEquals("1495941314418", extractTimestamp4Region(regionName));
            regionName = ":,,1495941314418..";
            assertEquals("1495941314418", extractTimestamp4Region(regionName));
            regionName = ":,,..";
            assertEquals("", extractTimestamp4Region(regionName));
        }
    }
}
