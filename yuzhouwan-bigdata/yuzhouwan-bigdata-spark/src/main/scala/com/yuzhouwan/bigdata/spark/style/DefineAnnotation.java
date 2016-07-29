package com.yuzhouwan.bigdata.spark.style;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：annotation
 *
 * @author Benedict Jin
 * @since 2015/11/23 0023
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefineAnnotation {
}
