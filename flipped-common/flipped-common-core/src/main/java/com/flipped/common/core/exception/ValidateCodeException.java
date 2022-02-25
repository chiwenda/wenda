package com.flipped.common.core.exception;

/**
 * 验证码异常
 *
 * @author cwd
 * @date 2022/1/14 下午11:10
 */
public class ValidateCodeException extends RuntimeException {
    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException() {
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
