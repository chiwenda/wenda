package com.flipped.gateway.config;

import com.flipped.gateway.handler.ImageCodeHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

/**
 * 路由函数配置信息，相当于@RequestMapping
 * <p>
 * proxyBeanMethods:配置@Bean是否使用代理模式，false每次都是新对象，true每次都从容器中获取同一个实例
 *
 * @author cwd
 * @date 2022/1/14 上午10:05
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RouterFunctionConfiguration {
    //日志组件
    private final Logger log = LoggerFactory.getLogger(RouterFunctionConfiguration.class);

    private final ImageCodeHandler imageCodeHandler;


    /**
     * 验证码路由
     */
    @Bean
    public RouterFunction<ServerResponse> codeRouter() {
        log.info("请求路由:/code 配置完成");
        return RouterFunctions.route(
                RequestPredicates.path("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), imageCodeHandler
        );
    }
}
