package com.flipped.auth.handler;


import com.flipped.admin.api.entity.SysLog;
import com.flipped.common.core.utils.SpringContextHolder;
import com.flipped.common.log.event.SysLogEvent;
import com.flipped.common.log.utils.SysLogUtils;
import com.flipped.common.log.utils.enums.LogTypeEnum;
import com.flipped.security.handler.AbstractAuthenticationFailureEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 处理登录失败方法
 *
 * @author chiwenda
 * @date 2022/1/29 下午2:43
 */
@Component
public class AuthenticationFailureEventHandler extends AbstractAuthenticationFailureEventHandler {

    private final Logger log = LoggerFactory.getLogger(AuthenticationFailureEventHandler.class);

    @Override
    public void handle(AuthenticationException authenticationException, Authentication authentication) {
        log.info("用户：{} 登录失败，异常：{}", authentication.getPrincipal(), authenticationException.getLocalizedMessage());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SysLog logVo = SysLogUtils.getSysLog();
        logVo.setTitle("登录失败");
        logVo.setType(LogTypeEnum.ERROR.getType());
        logVo.setException(authenticationException.getMessage());
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis();
        logVo.setTime(endTime - startTime);
        logVo.setCreateBy(authentication.getName());
        logVo.setUpdateBy(authentication.getName());
        SpringContextHolder.publishEvent(new SysLogEvent(logVo));
    }
}
