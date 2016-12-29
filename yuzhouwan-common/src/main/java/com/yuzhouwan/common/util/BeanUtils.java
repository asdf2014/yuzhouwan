package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Bean Utils
 *
 * @author Benedict Jin
 * @since 2016/12/1
 */
public class BeanUtils {

    private static final Logger _log = LoggerFactory.getLogger(BeanUtils.class);
    private static final ConcurrentHashMap<String, Field[]> FIELDS_CACHE = new ConcurrentHashMap<>();

    /**
     * Swap values into object's similar filed.
     *
     * @param o       object
     * @param key     filed
     * @param value   value
     * @param ignores character to be ignored
     */
    public static void swapper(Object o, String key, Object value, String... ignores) {
        if (o == null || StrUtils.isEmpty(key)) return;
        Class<?> clazz;
        if ((clazz = o.getClass()) == null) return;
        String className;
        if (StrUtils.isEmpty(className = clazz.getName())) return;
        Field[] fields;
        if (FIELDS_CACHE.size() == 0 || !FIELDS_CACHE.containsKey(className))
            FIELDS_CACHE.put(className, fields = clazz.getDeclaredFields());
        else fields = FIELDS_CACHE.get(className);
        if (fields == null || fields.length == 0) return;
        for (Field field : fields) {
            if (StrUtils.isLike(field.getName(), key, ignores)) {
                field.setAccessible(true);
                try {
                    field.set(o, value);
                } catch (IllegalAccessException e) {
                    _log.error(ExceptionUtils.errorInfo(e));
                }
                return;
            }
        }
        _log.debug("[Warn] Cannot be swapped, object: {}, key: {}, value: {}, ignores: {}.",
                o, key, value, JSON.toJSONString(ignores));
    }
}
