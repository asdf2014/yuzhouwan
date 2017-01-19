package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.LinkedList;

import static com.yuzhouwan.common.util.StrUtils.hex2Str;
import static com.yuzhouwan.common.util.StrUtils.str2Hex;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: String Stuff Tester
 *
 * @author Benedict Jin
 * @since 2016/3/23 0030
 */
public class StrUtilsTest {

    @Test
    public void fillTest() throws Exception {
        assertEquals("00000010", StrUtils.fillWitchZero(10, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.0d, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.01d, 8));
    }

    @Test
    public void mainValueTest() throws Exception {
        if (1 == Integer.parseInt(StrUtils.getMainValue("ATK000001", "ATK".length(), "0"))) {
            System.out.println("1");
        }
        if (40 == Integer.parseInt(StrUtils.getMainValue("ATK000040", "ATK".length(), "0"))) {
            System.out.println("40");
        }
    }

    @Test
    public void cutStart() throws Exception {
        assertEquals("yuzhouwan.com", StrUtils.cutStartStr("www.yuzhouwan.com", "www."));
    }

    @Test
    public void cutMiddle() throws Exception {
        assertEquals("\\com\\yuzhouwan\\common\\util\\StrUtilsTest.class",
                StrUtils.cutMiddleStr("F:\\如何成为 Java 高手\\笔记\\Soft Engineering\\Git\\[code]\\yuzhouwan\\yuzhouwan-common\\target\\test-classes\\..\\test-classes\\com\\yuzhouwan\\common\\util\\StrUtilsTest.class",
                        "test-classes"));
    }

    @Test
    public void cutTail() throws Exception {
        assertEquals("F:\\如何成为 Java 高手\\笔记\\Soft Engineering\\Git\\[code]\\yuzhouwan\\yuzhouwan-common\\target\\",
                StrUtils.cutTailStr("F:\\如何成为 Java 高手\\笔记\\Soft Engineering\\Git\\[code]\\yuzhouwan\\yuzhouwan-common\\target\\test-classes\\",
                        "test-classes\\"));
    }

    @Test
    public void holderTest() throws Exception {
        assertEquals("a1b2c3", String.format("%s1b%Sc%d", "a", "2", 3));
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("a");
        linkedList.add("b");
        assertEquals("a b", String.format("%s %s", linkedList.toArray()));
    }

    @Test
    public void splitMulti() throws Exception {
        LinkedList<String> expect = new LinkedList<>();
        expect.add("ns_fac");
        expect.add("hb_scapaysettlereg_acc");
        expect.add("006b897c8c6b0cdc258566b81508efe5");
        expect.add("storeCount");
        LinkedList<String> result = StrUtils.splitMulti(
                "namespace_ns_fac_table_hb_scapaysettlereg_acc_region_006b897c8c6b0cdc258566b81508efe5_metric_storeCount",
                "namespace_", "_table_", "_region_", "_metric_");
        assert result != null;
        int size = result.size();
        assertEquals(true, expect.size() == size);
        for (int i = 0; i < size; i++) {
            assertEquals(expect.get(i), result.get(i));
        }
    }

    @Test
    public void isNumberTest() throws Exception {
        assertEquals(true, StrUtils.isNumber("0"));
        assertEquals(true, StrUtils.isNumber("1"));
        assertEquals(true, StrUtils.isNumber("100"));
        assertEquals(false, StrUtils.isNumber("-1"));
        assertEquals(false, StrUtils.isNumber("1.1"));
        assertEquals(false, StrUtils.isNumber("abc"));
    }

    @Test
    public void map2JsonTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cluster", "test");
        jsonObject.put("table", "default");
        assertEquals("{\"cluster\":\"test\",\"table\":\"default\"}", jsonObject.toJSONString());
        jsonObject.put("metric", 1L);
        assertEquals("{\"cluster\":\"test\",\"metric\":1,\"table\":\"default\"}", jsonObject.toJSONString());
        jsonObject.remove("metric");
        assertEquals("{\"cluster\":\"test\",\"table\":\"default\"}", jsonObject.toJSONString());
        jsonObject.put("metric2", 0.02d);
        assertEquals("{\"cluster\":\"test\",\"metric2\":0.02,\"table\":\"default\"}", jsonObject.toJSONString());
    }

    @Test
    public void hexTest() throws Exception {
        String yuzhouwan = "宇宙湾yuzhouwan123";
        String yuzhouwanHex = "\\xe5\\xae\\x87\\xe5\\xae\\x99\\xe6\\xb9\\xbe\\x79\\x75\\x7a\\x68\\x6f\\x75\\x77\\x61\\x6e\\x31\\x32\\x33";
        assertEquals(yuzhouwanHex, str2Hex(yuzhouwan));
        assertEquals(yuzhouwan, hex2Str(str2Hex(yuzhouwan)));
        assertEquals(yuzhouwan, hex2Str(yuzhouwanHex));
    }
}
