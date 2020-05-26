package com.byc.common.mvc.exception;

/**
 * Created by baiyc
 * 2020/5/26/026 20:43
 * Description：
 */
public class BizException extends RuntimeException {
    private IErrorCode errorCode;
    private Object data;

    public BizException(IErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BizException(IErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }

    public BizException(IErrorCode errorCode, Exception e, String message) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public BizException(IErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return this.errorCode;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}

