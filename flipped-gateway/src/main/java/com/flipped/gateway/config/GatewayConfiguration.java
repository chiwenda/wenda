package com.flipped.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipped.gateway.filter.PasswordDecoderFilter;
import com.flipped.gateway.filter.RequestGlobalFilter;
import com.flipped.gateway.filter.ValidateCodeGatewayFilter;
import com.flipped.gateway.handler.GlobalExceptionHandler;
import com.flipped.gateway.handler.ImageCodeHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 网关配置
 *
 * @author cwd
 * @date 2022/1/23 下午8:37
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {
    @Bean
    public CorsFilter corsFilter() {
        //1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
//        config.addAllowedOrigin("http://localhost:8080");
        //是否发送 Cookie
        config.setAllowCredentials(true);
        //放行哪些请求方式
        config.addAllowedMethod("*");
        //放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        //暴露哪些头部信息
        config.addExposedHeader("*");
        //2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        //3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }

    /**
     * 密码解密过滤
     *
     * @param configProperties 配置属性
     * @return 密码解密过滤器
     */
    @Bean
    public PasswordDecoderFilter passwordDecoderFilter(GatewayConfigProperties configProperties) {
        return new PasswordDecoderFilter(configProperties);
    }


    /**
     * 验证码过滤
     * @param configProperties 配置属性
     * @param objectMapper
     * @param redisTemplate 缓存
     * @return
     */
    @Bean
    public ValidateCodeGatewayFilter validateCodeGatewayFilter(GatewayConfigProperties configProperties,
                                                               ObjectMapper objectMapper,
                                                               RedisTemplate<String, Object> redisTemplate) {
        return new ValidateCodeGatewayFilter(configProperties, objectMapper, redisTemplate);
    }

    @Bean
    public RequestGlobalFilter requestGlobalFilter(){
        return new RequestGlobalFilter();
    }


    /**
     * 全局异常处理
     * @param objectMapper
     * @return
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }

    @Bean
    public ImageCodeHandler imageCodeHandler(RedisTemplate<String, Object> redisTemplate) {
        return new ImageCodeHandler(redisTemplate);
    }

}
