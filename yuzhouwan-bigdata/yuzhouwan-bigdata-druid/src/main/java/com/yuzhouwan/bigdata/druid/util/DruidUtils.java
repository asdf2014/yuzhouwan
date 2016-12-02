package com.yuzhouwan.bigdata.druid.util;

import java.lang.reflect.Field;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Druid Utils
 *
 * @author Benedict Jin
 * @since 2016/12/2
 */
public class DruidUtils {

    private static final String METRICS_SPEC_TEMPLATE = "{\"fieldName\":\"stores\",\"name\":\"stores_min\",\"type\":\"longMin\"},{\"fieldName\":\"stores\",\"name\":\"stores_max\",\"type\":\"longMax\"}";

    public static String genTranquilityMetricsSpec(Class clazz) {

        StringBuilder strBuilder = new StringBuilder("{\"metricsSpec\":[{\"name\":\"count\",\"type\":\"count\"},");
        Field[] fields = clazz.getDeclaredFields();
        String fieldName;
        String simpleTypeName;
        for (Field field : fields) {
            fieldName = field.getName();
            simpleTypeName = field.getType().getSimpleName();
            if ("string".equalsIgnoreCase(simpleTypeName) || !"long".equalsIgnoreCase(simpleTypeName)
                    && !"double".equalsIgnoreCase(simpleTypeName))
                continue;
            strBuilder.append(String.format(
                    "{\"fieldName\":\"%s\",\"name\":\"%s_min\",\"type\":\"%sMin\"}," +
                            "{\"fieldName\":\"%s\",\"name\":\"%s_max\",\"type\":\"%sMax\"},",
                    fieldName, fieldName, simpleTypeName, fieldName, fieldName, simpleTypeName));
        }
        strBuilder.append("]}");
        return strBuilder.toString().replaceAll(",]", "]");
    }
}
