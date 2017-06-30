package com.yuzhouwan.common.util;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Dynamic PropUtils Test
 *
 * @author Benedict Jin
 * @since 2017/6/28
 */
public class DynamicPropUtilsTest {

//    @Test
    public void syncTest() throws Exception {
        String projectName = "yuzhouwan", key = "site", value = "blog";
        DynamicPropUtils dp = DynamicPropUtils.getInstance();
        Properties p = new Properties();
        p.put(key, value);
        dp.add(projectName, p);
        assertEquals(value, dp.get(projectName, key));
        dp.sync(projectName);
        assertEquals(value, dp.getFromRemote(projectName, key));
    }
}
