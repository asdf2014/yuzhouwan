package com.yuzhouwan.site.api.validation.enums;

import com.yuzhouwan.site.api.validation.model.NotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {NotNullValidator.class})
public @interface NotEmpty {

    String field() default "";

    String message() default "{com.yuzhouwan.site.api.validation.enums.NotEmpty.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}  