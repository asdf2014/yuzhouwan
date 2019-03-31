package com.yuzhouwan.bigdata.druid.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：Druid Utils Test
 *
 * @author Benedict Jin
 * @since 2016/12/2
 */
public class DruidUtilsTest {

    @Test
    public void genTranquilityMetricsSpec() {
        assertEquals("{\"metricsSpec\":[{\"name\":\"count\",\"type\":\"count\"}," +
                        "{\"fieldName\":\"b\",\"name\":\"b_min\",\"type\":\"longMin\"}," +
                        "{\"fieldName\":\"b\",\"name\":\"b_max\",\"type\":\"longMax\"}," +
                        "{\"fieldName\":\"c\",\"name\":\"c_min\",\"type\":\"doubleMin\"}," +
                        "{\"fieldName\":\"c\",\"name\":\"c_max\",\"type\":\"doubleMax\"}]}",
                DruidUtils.genTranquilityMetricsSpec(B.class));
    }

    private class B {
        private String a;
        private long b;
        private double c;
    }
}
