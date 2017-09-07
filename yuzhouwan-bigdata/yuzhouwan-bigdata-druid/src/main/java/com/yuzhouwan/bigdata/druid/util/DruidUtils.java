package com.yuzhouwan.bigdata.druid.util;

import com.yuzhouwan.common.util.BeanUtils;
import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function：Druid Utils
 *
 * @author Benedict Jin
 * @since 2016/12/2
 */
public class DruidUtils {

    /**
     * Generate tranquility metricsSpec in Druid config file.
     * [Note]: if using <code>BeanUtils#columns2Row</code>, then could avoid this situation
     *
     * @param classList the list of classes
     * @return metricsSpec
     */
    public static String genTranquilityMetricsSpec(Class... classList) {
        if (classList == null || classList.length <= 0) return "";
        String metricsSpecPrefix, metricsSpecMiddle;
        if (StrUtils.isEmpty(metricsSpecPrefix = PropUtils.getInstance().getProperty("metrics.spec.prefix"))
                || StrUtils.isEmpty(metricsSpecMiddle = PropUtils.getInstance().getProperty("metrics.spec.middle")))
            throw new RuntimeException("Properties [metrics.spec.prefix/middle] is empty!");
        String fieldName;
        StringBuilder strBuilder = new StringBuilder(metricsSpecPrefix);
        LinkedHashSet<String> checkExists = new LinkedHashSet<>();
        for (Class clazz : classList)
            for (Field field : BeanUtils.getAllFields(clazz)) {
                if (checkExists.contains(fieldName = field.getName())) continue;
                buildMiddlePart(strBuilder, field, fieldName, metricsSpecMiddle, checkExists);
            }
        return strBuilder.append("]}").toString().replaceAll(",]", "]");
    }

    private static void buildMiddlePart(StringBuilder strBuilder, Field field, String fieldName,
                                        String metricsSpecMiddle, LinkedHashSet<String> checkExists) {
        String simpleTypeName;
        checkExists.add(fieldName);
        if ("string".equalsIgnoreCase(simpleTypeName = field.getType().getSimpleName())
                || !"long".equalsIgnoreCase(simpleTypeName) && !"double".equalsIgnoreCase(simpleTypeName))
            return;
        strBuilder.append(String.format(metricsSpecMiddle, fieldName, fieldName, simpleTypeName,
                fieldName, fieldName, simpleTypeName));
    }
}
