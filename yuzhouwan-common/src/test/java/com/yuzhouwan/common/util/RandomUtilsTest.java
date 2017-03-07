package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

import static com.yuzhouwan.common.util.RandomUtils.perm;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
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
        assertEquals(true, total == 19912);

        gaussian = RandomUtils.getGaussian(0, 9, 9, factor, stdDeviation, variance, mean);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 9, 9, total);
        _log.info(JSON.toJSONString(gaussian));
        assertEquals("[62,461,1919,4512,6000,4512,1919,461,62]", JSON.toJSONString(gaussian));
        assertEquals(true, total == 19908);

        gaussian = RandomUtils.getGaussian(0, 15, 71, factor, stdDeviation, variance, mean);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 15, 71, total);
        _log.info(JSON.toJSONString(gaussian));
        assertEquals("[12,12,12,12,12,92,92,92,92,92,383,383,383,383,383,902,902,902,902,902,1200,1200,1200,1200,1200,902,902,902,902,902,383,383,383,383,383,92,92,92,92,92,12,12,12,12,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]",
                JSON.toJSONString(gaussian));
        assertEquals(true, total == 19890);

        factor = 2500;
        stdDeviation = 8;
        variance = 1;

        gaussian = RandomUtils.getGaussian(0, 15, 9, factor, stdDeviation, variance, 32 / 6 + 1);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Size: {}, Total: {}", 0, 15, 9, gaussian.size(), total);
        _log.info(JSON.toJSONString(gaussian));

        gaussian = RandomUtils.getGaussian(0, 15, 70, factor, stdDeviation, variance, 32 / 6 + 1);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Size: {}, Total: {}", 0, 15, 70, gaussian.size(), total);
        _log.info(JSON.toJSONString(gaussian));
    }
}
