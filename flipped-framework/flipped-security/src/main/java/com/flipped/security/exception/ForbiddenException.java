package com.flipped.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flipped.security.component.CustomAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @author chiwenda
 * @date 2022/2/8 下午8:54
 */
@JsonSerialize(using = CustomAuth2ExceptionSerializer.class)
public class ForbiddenException extends CustomAuth2Exception{
    public ForbiddenException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "access_denied";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.FORBIDDEN.value();
    }

}
