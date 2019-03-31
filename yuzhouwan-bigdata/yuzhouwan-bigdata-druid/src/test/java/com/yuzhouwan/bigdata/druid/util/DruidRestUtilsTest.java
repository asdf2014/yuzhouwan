package com.yuzhouwan.bigdata.druid.util;

import com.yuzhouwan.common.util.FileUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.junit.Ignore;
import org.junit.Test;

import static com.yuzhouwan.common.dir.DirUtils.TEST_RESOURCES_PATH;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Druid Restful Utils Test
 *
 * @author Benedict Jin
 * @since 2017/1/20
 */
public class DruidRestUtilsTest {

    private static final String QUERY_RESULT = TEST_RESOURCES_PATH.concat("rest/druid.query.result.json");
    private static final String QUERY = TEST_RESOURCES_PATH.concat("rest/druid.query.json");
    private static final String QUERY_URL = "http://yuzhouwan:8082/druid/v2/?pretty";

    @Ignore
    @Test
    public void testPost() throws Exception {
        byte[] queryResult = FileUtils.readFile(QUERY_RESULT);
        if (queryResult == null)
            throw new RuntimeException(String.format("Cannot read content from %s!", QUERY_RESULT));
        String except = new String(queryResult);
        byte[] queryJson = FileUtils.readFile(QUERY);
        if (queryJson == null)
            throw new RuntimeException(String.format("Cannot read content from %s!", QUERY));
        String result = DruidRestUtils.post(QUERY_URL, new String(queryJson));
        assertEquals(StrUtils.compression(except), StrUtils.compression(result));
    }
}
