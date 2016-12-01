package com.yuzhouwan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

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

    public static void swapper(Object o, String key, Object value, String... ignores) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isLike(field.getName(), key, ignores)) {
                field.setAccessible(true);
                try {
                    field.set(o, value);
                } catch (IllegalAccessException e) {
                    _log.error(ExceptionUtils.errorInfo(e));
                }
                return;
            }
        }
    }

    public static boolean isLike(String origin, String aim, String... ignores) {
        for (String ignore : ignores) {
            origin = origin.replaceAll(ignore, "");
            aim = aim.replaceAll(ignore, "");
        }
        return origin.equalsIgnoreCase(aim);
    }
}
