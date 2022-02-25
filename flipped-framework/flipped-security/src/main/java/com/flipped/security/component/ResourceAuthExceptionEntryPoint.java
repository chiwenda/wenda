package com.flipped.security.component;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipped.common.core.contants.CommonConstants;
import com.flipped.common.core.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 客户端异常处理 1. 可以根据 AuthenticationException 不同细化异常处理
 *
 * @author cwd
 * @date 2022/2/1614:53
 **/
@RequiredArgsConstructor
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        response.setCharacterEncoding(CommonConstants.UTF8);
        response.setContentType(CommonConstants.CONTENT_TYPE);
        R<String> result = new R<>();
        result.setCode(CommonConstants.FAIL);
        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        if (authException != null) {
            result.setMsg("error");
            result.setData(authException.getMessage());
        }

        // 针对令牌过期返回特殊的 424
        if (authException instanceof InsufficientAuthenticationException) {
            response.setStatus(org.springframework.http.HttpStatus.FAILED_DEPENDENCY.value());
            result.setMsg("token expire");
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(result));
    }
}
