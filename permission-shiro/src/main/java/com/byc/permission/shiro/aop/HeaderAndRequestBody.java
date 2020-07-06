package com.byc.permission.shiro.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by baiyc
 * 2020/7/6/006 09:35
 * Description：解析header和body注解
 */
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface HeaderAndRequestBody {
}
