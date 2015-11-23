package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：annotation
 *
 * @author asdf2014
 * @since 2015/11/23 0023
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefineAnnotation {
}
