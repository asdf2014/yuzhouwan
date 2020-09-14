package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;

import static com.yuzhouwan.common.util.RandomUtils.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function: Random Utils Test
 *
 * @author Benedict Jin
 * @since 2016/8/23
 */
public class RandomUtilsTest {

    private static final Logger _log = LoggerFactory.getLogger(RandomUtilsTest.class);

    @Test
    public void permTest() {
        assertNotEquals("[9,0,8,3,5,7,2,10,4,1,6] ", JSON.toJSONString(perm(11)));
    }

    @Test
    public void getGaussianTest() {

        long factor = 6000;
        double stdDeviation = 0.7;
        double variance = 1;
        double mean = 4;

        LinkedList<Integer> gaussian = RandomUtils.getGaussian(0, 15, 15, factor, stdDeviation, variance, mean);
        int total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 15, 15, total);
        _log.info(JSON.toJSONString(gaussian));
        assertEquals("[62,461,1919,4512,6000,4512,1919,461,62,4,0,0,0,0,0]", JSON.toJSONString(gaussian));
        Assert.assertEquals(19912, total);

        gaussian = RandomUtils.getGaussian(0, 9, 9, factor, stdDeviation, variance, mean);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 9, 9, total);
        _log.info(JSON.toJSONString(gaussian));
        assertEquals("[62,461,1919,4512,6000,4512,1919,461,62]", JSON.toJSONString(gaussian));
        Assert.assertEquals(19908, total);

        gaussian = RandomUtils.getGaussian(0, 15, 71, factor, stdDeviation, variance, mean);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 15, 71, total);
        _log.info(JSON.toJSONString(gaussian));
        assertEquals("[12,12,12,12,12,92,92,92,92,92,383,383,383,383,383,902,902,902,902,902,1200,1200,1200,1200,1200,902,902,902,902,902,383,383,383,383,383,92,92,92,92,92,12,12,12,12,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]",
                JSON.toJSONString(gaussian));
        Assert.assertEquals(19890, total);

        factor = 2500;
        stdDeviation = 8;
        variance = 1;

        gaussian = RandomUtils.getGaussian(0, 15, 9, factor, stdDeviation, variance, 32 / 6 + 1);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Size: {}, Total: {}", 0, 15, 9, gaussian.size(), total);
        _log.info(JSON.toJSONString(gaussian));

        factor = 2000;
        stdDeviation = 8;
        variance = 1.5;

        gaussian = RandomUtils.getGaussian(0, 15, 71, factor, stdDeviation, variance, 6);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Size: {}, Total: {}", 0, 15, 71, gaussian.size(), total);
        _log.info(JSON.toJSONString(gaussian));
        //start: 0, end: 15, num: 71, factor: 2000, stdDeviation: 8.0, variance: 1.0, mean: 4.0,
        //Gaussian: [268,268,268,268,268,319,319,319,319,319,362,362,362,362,362,390,390,390,390,390,400,400,400,
        // 400,400,390,390,390,390,390,362,362,362,362,362,319,319,319,319,319,268,268,268,268,268,214,214,214,
        // 214,214,163,163,163,163,163,117,117,117,117,117,81,81,81,81,81,53,53,53,53,53,33,33,33,33,33
        // ],
        //total: 18695
    }

    /*
    JVM: -ea -Xmx1024M -Xms1024M -Xmn256M -XX:+AlwaysPreTouch
     */
    @Test
    public void testUUID() {
        int count = 0, len = 100, lost = 0;
        HashSet<Long> set = new HashSet<>(len);
        int size;
        long time = 0;
        long begin, end, uuid;
        while (count < len) {
            size = set.size();
            begin = System.nanoTime();
            uuid = uuid();
            end = System.nanoTime();
            time += (end - begin);
            set.add(uuid);
            if (set.size() == size) lost++;
            count++;
        }
        assertEquals(0, lost);
        // Count: 1000_0000, Time: 1131.016648 ms
        _log.info("Count: {}, Time: {} ms", count, time / Math.pow(10, 6));
    }

    /*
    JVM: -ea -Xmx1024M -Xms1024M -Xmn256M -XX:+AlwaysPreTouch
     */
    @Test
    public void testNativeUUID() {
        int count = 0, len = 100, lost = 0;
        HashSet<String> set = new HashSet<>(len);
        int size;
        long time = 0;
        long begin, end;
        String uuid;
        while (count < len) {
            size = set.size();
            begin = System.nanoTime();
            uuid = uuid2();
            end = System.nanoTime();
            time += (end - begin);
            set.add(uuid);
            if (set.size() == size) lost++;
            count++;
        }
        assertEquals(0, lost);
        // Count: 100_0000, Time: 1294.214348 ms
        _log.info("Count: {}, Time: {} ms", count, time / Math.pow(10, 6));
    }
}
