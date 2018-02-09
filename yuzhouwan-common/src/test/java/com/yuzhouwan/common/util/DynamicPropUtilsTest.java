package com.yuzhouwan.common.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function：Dynamic PropUtils Test
 *
 * @author Benedict Jin
 * @since 2017/6/28
 */
public class DynamicPropUtilsTest {

    @Ignore
    @Test
    public void syncTest() {
        String projectName = "yuzhouwan", key = "site", value = "blog";
        DynamicPropUtils dp = DynamicPropUtils.getInstance();
        Properties p = new Properties();
        p.put(key, value);
        dp.add(projectName, p);
        assertEquals(value, dp.get(projectName, key));
        dp.sync(projectName);
        assertEquals(value, dp.getFromRemote(projectName, key));
        dp.close();
    }

    @Test
    public void countTest() {
        int count = 0, max = 1;
        {
            boolean interWhileLoop = false;
            while (count++ > max) interWhileLoop = true;
            assertEquals(false, interWhileLoop);
            count = 0;
            while (++count > max) interWhileLoop = true;
            assertEquals(false, interWhileLoop);
            count = 0;
            while (count++ >= max) interWhileLoop = true;
            assertEquals(false, interWhileLoop);
            count = 0;
            while (++count >= max) interWhileLoop = true;
            assertEquals(true, interWhileLoop);
        }
        {
            count = 0;
            int x = ++count;
            assertEquals(1, x);
            assertEquals(1, count);
            count = 0;
            int y = count++;
            assertEquals(0, y);
            assertEquals(1, count);
        }
    }
}
