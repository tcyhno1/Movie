package com.yuhao.exception;

/**
 * 自定义异常  业务异常
 */
public class BizException extends Exception {

    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }
}
