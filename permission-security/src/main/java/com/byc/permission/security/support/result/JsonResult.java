package com.byc.permission.security.support.result;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Created by baiyc
 * 2019/10/4/004 14:59
 * Description：
 */
@NoArgsConstructor
@Slf4j
@Data
public class JsonResult<T extends Object> {

    @JsonSerialize(using = StatusCode.StatusCodeSerializer.class)
    private StatusCode status;
    private T data;
    private String message;

    /**一般的简单返回*/
    public static JsonResult fail(StatusCode code){
        return fail(code,code.getMessage());
    }
    /**一般的简单返回*/
    public static JsonResult fail(StatusCode code, String message){
        JsonResult result = new JsonResult();
        result.setData(new HashMap());
        result.setStatus(code);
        result.setMessage(message);
        log.warn("Response failed,message:{}",message);
        return result;
    }
    /**成功响应*/
    public static JsonResult succ(){
        return succ(new HashMap());
    }
    /**成功响应*/
    public static <T> JsonResult succ(T data){
        JsonResult result = new JsonResult();
        result.setData(data);
        result.setStatus(StatusCode.CODE_SUCCESS);
        result.setMessage(StatusCode.CODE_SUCCESS.getMessage());
        log.info("Response success,message:{}",StatusCode.CODE_SUCCESS.getMessage());
        return result;
    }
}
