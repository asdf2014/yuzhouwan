package com.yuzhouwan.site.api.validation.model;

import com.yuzhouwan.site.api.validation.enums.NotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: Not Null Validator
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
public class NotNullValidator implements ConstraintValidator<NotEmpty, Object> {

    @Override
    public void initialize(NotEmpty annotation) {
    }

    @Override
    public boolean isValid(Object str, ConstraintValidatorContext constraintValidatorContext) {
        return str != null;
    }
}
