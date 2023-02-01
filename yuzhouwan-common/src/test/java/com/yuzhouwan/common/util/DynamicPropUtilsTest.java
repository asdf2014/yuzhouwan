package com.yuzhouwan.common.util;

import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：Dynamic PropUtils Test
 *
 * @author Benedict Jin
 * @since 2017/6/28
 */
public class DynamicPropUtilsTest {

    private TestingServer server;
    private DynamicPropUtils dp;

    @Before
    public void setup() throws Exception {
        server = new TestingServer(2181, true);
        dp = DynamicPropUtils.getInstance();
    }

    @Test
    public void syncTest() {
        String projectName = "yuzhouwan", key = "site", value = "blog";
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
            assertFalse(interWhileLoop);
            count = 0;
            while (++count > max) interWhileLoop = true;
            assertFalse(interWhileLoop);
            count = 0;
            while (count++ >= max) interWhileLoop = true;
            assertFalse(interWhileLoop);
            count = 0;
            while (++count >= max) interWhileLoop = true;
            assertTrue(interWhileLoop);
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

    @After
    public void teardown() throws Exception {
        if (dp != null) dp.close();
        if (server != null) server.close();
    }
}
