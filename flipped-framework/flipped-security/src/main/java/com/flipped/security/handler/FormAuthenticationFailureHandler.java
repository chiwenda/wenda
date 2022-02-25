package com.flipped.security.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.flipped.common.core.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chiwenda
 * @date 2022/1/29 下午3:09
 */
public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final Logger log = LoggerFactory.getLogger(FormAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("表单登录失败:{}", exception.getLocalizedMessage());
        String url = HttpUtil.encodeParams(String.format("/token/login?error=%s", exception.getMessage()),
                CharsetUtil.CHARSET_UTF_8);
        WebUtils.getResponse().sendRedirect(url);
    }
}
