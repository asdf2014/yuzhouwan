package com.yuzhouwan.common.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：TimeUtils Test
 *
 * @author Benedict Jin
 * @since 2016/3/8 0030
 */
public class TimeUtilsTest {

    private Logger _log = LoggerFactory.getLogger(TimeUtilsTest.class);

    @Test
    public void test() {

        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        String month = simpleDateFormat.format(now);
        int m = Integer.parseInt(month);
        _log.debug("month:{}", m);
        assertEquals(m, TimeUtils.month());
    }

    @Test
    public void test2(){

        LinkedList<Integer> l = new LinkedList<Integer>();
        l.add(1);
        l.add(2);
        int counter = 0;
        for (Integer integer : l) {
            counter++;
        }
    }
}