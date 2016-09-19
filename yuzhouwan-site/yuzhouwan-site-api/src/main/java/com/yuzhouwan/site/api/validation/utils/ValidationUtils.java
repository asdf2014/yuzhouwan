package com.yuzhouwan.site.api.validation.utils;

import org.springframework.validation.BindingResult;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function: Validation Utils
 *
 * @author Benedict Jin
 * @since 2016/9/7
 */
public class ValidationUtils {

    public static String getErrorInfos(BindingResult result) {
        StringBuilder strBuilder = new StringBuilder();
        result.getAllErrors().forEach(error -> strBuilder.append(error.getDefaultMessage()).append(" "));
        return strBuilder.toString().trim();
    }
}
