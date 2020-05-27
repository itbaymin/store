package com.byc.common.mvc;

import com.byc.common.mvc.exception.CommonErrorCode;
import com.byc.common.mvc.exception.IErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**响应结果*/
public class WebResult<T> {

    public static final WebResult UN_LOGIN = WebResult.fail(CommonErrorCode.UNLOGIN_ERROR);

    @Getter
    @JsonProperty("status")
    private int code = 200;
    @Getter
    private String message = "";
    @Getter
    private T data;
    @Setter
    @Getter
    @JsonProperty("msg_key")
    private String msgKey = "";

    private WebResult(T data,String message) {
        this.data = data;
        this.message = message;
    }

    private WebResult(T data, int code) {
        this(data,"");
        this.code = code;
    }

    public static <T> WebResult success(T data) {
        return new WebResult(data,"");
    }

    public static <T> WebResult success(T data,String message) {
        return new WebResult(data,message);
    }

    public static WebResult<String> fail(IErrorCode errorCode) {
        return fail(errorCode, "");
    }

    public static WebResult<String> fail(IErrorCode errorCode, String errorMessage) {
        WebResult failResult = new WebResult("", errorCode.code());
        failResult.message = StringUtils.defaultIfBlank(errorMessage, errorCode.message());
        return failResult;
    }

    public static WebResult<String> fail(IErrorCode errorCode, String errorMessage, Object data) {
        WebResult failResult = fail(errorCode, errorMessage);
        failResult.data = data;
        return failResult;
    }
}
