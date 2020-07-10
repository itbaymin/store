package com.byc.common.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**签名校验**/
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface NotFormat {
    /**是否校验*/
    boolean value() default true;
}
