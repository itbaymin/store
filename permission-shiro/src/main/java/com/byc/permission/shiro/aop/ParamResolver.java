package com.byc.permission.shiro.aop;

/**
 * Created by baiyc
 * 2020/7/6/006 09:18
 * Description：
 */

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by baiyc
 * 2020/6/23/023 10:23
 * Description：header和body参数解析
 */
@Component
public class ParamResolver extends RequestResponseBodyMethodProcessor {

    public ParamResolver(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(HeaderAndRequestBody.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        Object result = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        HashMap<String, Object> headersMap = Maps.newHashMapWithExpectedSize(10);
        Iterator<String> headerNames = webRequest.getHeaderNames();
        headerNames.forEachRemaining(headerName->headersMap.put(headerName,webRequest.getHeader(headerName)));
        BeanUtils.populate(result,headersMap);
        return result;
    }
}

