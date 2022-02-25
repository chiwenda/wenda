package com.flipped.security.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 认证失败事件处理器
 *
 * @author chiwenda
 * @date 2022/1/29 下午2:45
 */
public abstract class AbstractAuthenticationFailureEventHandler implements ApplicationListener<AbstractAuthenticationFailureEvent> {
    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        AuthenticationException exception = event.getException();
        Authentication source = (Authentication) event.getSource();
        handle(exception, source);
    }

    /**
     * 处理登录成功方法
     * <p>
     *
     * @param authenticationException 登录的authentication 对象
     * @param authentication          登录的authenticationException 对象
     */
    public abstract void handle(AuthenticationException authenticationException, Authentication authentication);
}
