package com.yuzhouwan.site.api.validation.model;

import com.yuzhouwan.site.api.validation.enums.NotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullValidator implements ConstraintValidator<NotEmpty, Object> {

    @Override
    public void initialize(NotEmpty annotation) {
    }

    @Override
    public boolean isValid(Object str, ConstraintValidatorContext constraintValidatorContext) {
        return str != null;
    }

}