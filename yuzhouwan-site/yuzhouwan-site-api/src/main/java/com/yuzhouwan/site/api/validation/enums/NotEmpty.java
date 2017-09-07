package com.yuzhouwan.site.api.validation.enums;

import com.yuzhouwan.site.api.validation.model.NotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: NotEmpty Annotation
 *
 * @author Benedict Jin
 * @since 2016/9/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {ElementType.METHOD,
                ElementType.FIELD,
                ElementType.ANNOTATION_TYPE,
                ElementType.CONSTRUCTOR,
                ElementType.PARAMETER})
@Constraint(validatedBy = {NotNullValidator.class})
public @interface NotEmpty {

    String field() default "";

    String message() default "{com.yuzhouwan.site.api.validation.enums.NotEmpty.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
