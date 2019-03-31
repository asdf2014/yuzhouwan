package com.yuzhouwan.hacker.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: JsonUtils Tester
 *
 * @author Benedict Jin
 * @since 2016/3/17
 */
public class JsonUtilsTest {

    private JsonUtils jsonUtils;

    @Before
    public void init() {
        jsonUtils = new JsonUtils();
    }

    @Test
    public void testSimple() {
        assertEquals("1", jsonUtils.simpleParse().get(0).getGroupId());
    }

    @Test
    public void testMapParse() throws Exception {
        {
            //jackson
            String json = "{'args':['0:userName', '0:userPassword'], 'rets':['0:email']}".replace("'", "\"");
            LinkedHashMap<String, LinkedList<String>> map = new ObjectMapper().readValue(json,
                    new com.fasterxml.jackson.core.type.TypeReference<LinkedHashMap<String, LinkedList<String>>>() {
                    });
            assertEquals("{args=[0:userName, 0:userPassword], rets=[0:email]}", map.toString());
        }
        {
            //fastjson
            String json = "{'args':['0:userName', '0:userPassword'], 'rets':['0:email']}";
            LinkedHashMap<String, LinkedList<String>> map = JSON.parseObject(json,
                    new TypeReference<LinkedHashMap<String, LinkedList<String>>>() {
                    });
            assertEquals("{args=[0:userName, 0:userPassword], rets=[0:email]}", map.toString());
        }
        {
            LinkedHashMap<String, Integer> l = new LinkedHashMap<>();
            l.put("3", 3);
            l.put("1", 2);
            l.put("2", 1);
            l.put("4", 4);
            assertEquals("{\"3\":3,\"1\":2,\"2\":1,\"4\":4}", JSON.toJSONString(l));
        }
    }
}
