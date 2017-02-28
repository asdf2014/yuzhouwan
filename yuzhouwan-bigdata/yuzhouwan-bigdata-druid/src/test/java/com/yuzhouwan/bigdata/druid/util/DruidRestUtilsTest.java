package com.yuzhouwan.bigdata.druid.util;

import com.yuzhouwan.common.dir.DirUtils;
import com.yuzhouwan.common.util.FileUtils;
import com.yuzhouwan.common.util.StrUtils;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Druid Restful Utils Test
 *
 * @author Benedict Jin
 * @since 2017/1/20
 */
public class DruidRestUtilsTest {

    //    @Test
    public void testPost() throws Exception {
        String except = new String(FileUtils.readFile(DirUtils.TEST_RESOURCES_PATH.concat(
                "rest/druid.query.result.json")));
        String result = DruidRestUtils.post("http://yuzhouwan:8082/druid/v2/?pretty",
                new String(FileUtils.readFile(DirUtils.TEST_RESOURCES_PATH.concat("rest/druid.query.json"))));
        assertEquals(StrUtils.compression(except), StrUtils.compression(result));
    }
}