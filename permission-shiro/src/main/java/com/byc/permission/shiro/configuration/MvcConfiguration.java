package com.byc.permission.shiro.configuration;

import com.byc.permission.shiro.aop.ParamResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by baiyc
 * 2020/6/24/024 11:06
 * Description：jackson扫描配置
 */
@Slf4j
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private ParamResolver paramResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(paramResolver);
    }

}
