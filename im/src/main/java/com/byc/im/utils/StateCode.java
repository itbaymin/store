package com.byc.im.utils;

import com.byc.common.mvc.exception.IErrorCode;
import lombok.AllArgsConstructor;

/**
 * Created by baiyc
 * 2019/10/5/005 17:03
 * Description：
 */
@AllArgsConstructor
public enum StateCode implements IErrorCode {
    CODE_SUCCESS(200,"成功"),
    CODE_BUSINESS(10000000,"业务异常");

    private int code;
    private String message;
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
