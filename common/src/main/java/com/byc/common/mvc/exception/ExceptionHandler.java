package com.byc.common.mvc.exception;

import com.byc.common.mvc.WebResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Set;

/**
 * Created by baiyc
 * 2020/5/26/026 20:40
 * Description：
 */
@ControllerAdvice
public class ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    static final Set<MediaType> EXCEPTION_RESPONSE_MEDIA_TYPE;

    @org.springframework.web.bind.annotation.ExceptionHandler({BizException.class})
    @ResponseBody
    public WebResult businessException(BizException exception, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, EXCEPTION_RESPONSE_MEDIA_TYPE);
        WebResult webResult = WebResult.fail(exception.getErrorCode(), exception.getMessage(), exception.getData());
        return webResult;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({BindException.class})
    @ResponseBody
    public WebResult bindException(BindException exception, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, EXCEPTION_RESPONSE_MEDIA_TYPE);
        String tipsMsg = this.bindExceptionMsg(exception);
        WebResult webResult = WebResult.fail(CommonErrorCode.PARAM_EXCEPTION, tipsMsg);
        return webResult;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public WebResult methodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, EXCEPTION_RESPONSE_MEDIA_TYPE);
        String tipsMsg = this.bindExceptionMsg(exception.getBindingResult());
        WebResult webResult = WebResult.fail(CommonErrorCode.PARAM_EXCEPTION, tipsMsg);
        return webResult;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler({ConstraintViolationException.class})
    public WebResult handleApiConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, EXCEPTION_RESPONSE_MEDIA_TYPE);
        String tipsMsg = (ex.getConstraintViolations().iterator().next()).getMessage();
        WebResult webResult = WebResult.fail(CommonErrorCode.PARAM_EXCEPTION, tipsMsg);
        return webResult;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public WebResult runtimeException(RuntimeException exception, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, EXCEPTION_RESPONSE_MEDIA_TYPE);
        log.error("未捕获的异常!", exception);
        WebResult webResult = WebResult.fail(CommonErrorCode.COMMON_BIZ_EXCEPTION, "稍后重试!");
        return webResult;
    }

    private String bindExceptionMsg(BindingResult bindingResult) {
        String tipsMsg = (bindingResult.getAllErrors().get(0)).getDefaultMessage();
        return tipsMsg;
    }

    static {
        EXCEPTION_RESPONSE_MEDIA_TYPE = Collections.singleton(MediaType.APPLICATION_JSON_UTF8);
    }
}
