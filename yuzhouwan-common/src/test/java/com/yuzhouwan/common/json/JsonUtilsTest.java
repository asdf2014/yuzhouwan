package com.yuzhouwan.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

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

    @Test
    public void testMapParse() throws Exception {

        //"{\"name\":\"mkyong\", \"age\":29}"
        String json = "{'args':['0:userName', '0:userPassword'], 'rets':['0:email']}".replace("'", "\"");
        LinkedHashMap<String, LinkedList<String>> map = new ObjectMapper().readValue(json,
                new TypeReference<LinkedHashMap<String, LinkedList<String>>>() {
                });
        assertEquals("{args=[0:userName, 0:userPassword], rets=[0:email]}", map.toString());
    }
}
