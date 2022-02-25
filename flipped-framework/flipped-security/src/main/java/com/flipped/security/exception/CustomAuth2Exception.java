package com.flipped.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flipped.security.component.CustomAuth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 *
 * @author chiwenda
 * @date 2022/2/8 下午8:45
 */
@JsonSerialize(using = CustomAuth2ExceptionSerializer.class)
public class CustomAuth2Exception extends OAuth2Exception {


    @Getter
    private String errorCode;


    public CustomAuth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public CustomAuth2Exception(String msg) {
        super(msg);
    }
}
