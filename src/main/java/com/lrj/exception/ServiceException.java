package com.lrj.exception;


import com.lrj.common.ErrorCode;

/**
 * @author linqin
 * @date 2018/9/4
 */
public class ServiceException extends RuntimeException {

    private Integer errorCode;

    private Object data;

    public Object data() {
        return data;
    }

    public ServiceException data(Object data) {
        this.data = data;
        return this;
    }

    public ServiceException(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.msg());
        this.errorCode = errorCode.code();
    }

    public ServiceException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
