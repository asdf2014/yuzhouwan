package com.yuzhouwan.common.json;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: JsonUtils Tester
 *
 * @author Benedict Jin
 * @since 2016/3/17 0030
 */
public class JsonUtilsTest {

    private JsonUtils jsonUtils;

    @Before
    public void init() {
        jsonUtils = new JsonUtils();
    }

    @Test
    public void testSimple() throws Exception {
        assertEquals("1", jsonUtils.simpleParse().get(0).getGroupId());
    }
}
