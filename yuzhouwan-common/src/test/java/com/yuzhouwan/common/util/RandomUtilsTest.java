package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

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
    public void getGaussianTest() {

        LinkedList<Integer> gaussian = RandomUtils.getGaussian(0, 15, 15);
        int total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 15, 15, total);
        System.out.println(JSON.toJSONString(gaussian));
//        assertEquals("[53,101,178,294,453,653,881,1112,1313,1450,1500,1450,1313,1112,881]",
//                JSON.toJSONString(gaussian));
//        assertEquals(true, total == 12744);

        gaussian = RandomUtils.getGaussian(0, 9, 9);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 9, 9, total);
        System.out.println(JSON.toJSONString(gaussian));
//        assertEquals("[53,101,178]", JSON.toJSONString(gaussian));
//        assertEquals(true, total == 332);

        gaussian = RandomUtils.getGaussian(0, 15, 71);
        total = 0;
        for (Integer g : gaussian) total += g;
        _log.info("Param: {}/{}/{}, Total: {}", 0, 15, 71, total);
        System.out.println(JSON.toJSONString(gaussian));
//        assertEquals("[10,10,10,10,10,20,20,20,20,20,35,35,35,35,35,58,58,58,58,58,90,90,90,90,90,130,130,130,130,130,176,176,176,176,176,222,222,222,222,222,262,262,262,262,262,290,290,290,290,290,300,300,300,300,300,290,290,290,290,290,262,262,262,262,262,222,222,222,222,222,176,176,176,176,176]",
//                JSON.toJSONString(gaussian));
//        assertEquals(true, total == 12715);
    }
}
