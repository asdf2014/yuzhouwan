package com.yuzhouwan.bigdata.druid.util;

import com.yuzhouwan.common.util.BeanUtils;
import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Druid Utils
 *
 * @author Benedict Jin
 * @since 2016/12/2
 */
public class DruidUtils {

    private static final String metricsSpecPrefix;
    private static final String metricsSpecMiddle;

    static {
        PropUtils p = PropUtils.getInstance();
        if (StrUtils.isEmpty(metricsSpecPrefix = p.getProperty("metrics.spec.prefix")) ||
                StrUtils.isEmpty(metricsSpecMiddle = p.getProperty("metrics.spec.middle")))
            throw new RuntimeException("Properties [metrics.spec.prefix/middle] is empty!");
    }

    /**
     * Generate tranquility metricsSpec in Druid config file.
     *
     * @param classList the list of classes
     * @return metricsSpec
     */
    public static String genTranquilityMetricsSpec(Class... classList) {
        if (classList == null || classList.length <= 0) return "";
        StringBuilder strBuilder = new StringBuilder(metricsSpecPrefix);
        String fieldName, simpleTypeName;
        LinkedHashSet<String> checkExists = new LinkedHashSet<>();
        for (Class clazz : classList)
            for (Field field : BeanUtils.getAllFields(clazz)) {
                if (checkExists.contains(fieldName = field.getName())) continue;
                checkExists.add(fieldName);
                if ("string".equalsIgnoreCase(simpleTypeName = field.getType().getSimpleName()) ||
                        !"long".equalsIgnoreCase(simpleTypeName) && !"double".equalsIgnoreCase(simpleTypeName))
                    continue;
                strBuilder.append(String.format(metricsSpecMiddle, fieldName, fieldName, simpleTypeName,
                        fieldName, fieldName, simpleTypeName));
            }
        strBuilder.append("]}");
        return strBuilder.toString().replaceAll(",]", "]");
    }
}
