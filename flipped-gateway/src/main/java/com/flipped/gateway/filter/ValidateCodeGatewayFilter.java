package com.flipped.gateway.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipped.common.core.contants.CacheConstants;
import com.flipped.common.core.contants.SecurityConstants;
import com.flipped.common.core.exception.ValidateCodeException;
import com.flipped.common.core.utils.R;
import com.flipped.common.core.utils.WebUtils;
import com.flipped.gateway.config.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * 验证码处理过滤器
 *
 * @author cwd
 * @date 2022/1/14 下午4:31
 * <p>
 * Oauth支持的5类 grant_type 及说明
 * <p>
 * <p>
 * authorization_code — 授权码模式(即先登录获取code,再获取token)
 * <p>
 * <p>
 * password — 密码模式(将用户名,密码传过去,直接获取token)
 * <p>
 * <p>
 * client_credentials — 客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向’服务端’获取资源)
 * <p>
 * <p>
 * implicit — 简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)
 */
@RequiredArgsConstructor
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory<Object> {

    private final Logger log = LoggerFactory.getLogger(ValidateCodeGatewayFilter.class);

    private final GatewayConfigProperties configProperties;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            //请求中是否包含登录请求
            boolean isAuthToken = CharSequenceUtil.containsIgnoreCase(request.getURI().getPath(),
                    SecurityConstants.OAUTH_TOKEN_URL);


            //不是登录请求放行
            if (!isAuthToken) {
                return chain.filter(exchange);
            }

            log.info("=====处理登录请求=====");

            //刷新access_token ，手机号登录（也可以这里进行校验） 直接向下执行
            String grantType = request.getQueryParams().getFirst("grant_type");
            if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
                return chain.filter(exchange);
            }

            //解析客户端id
            boolean isIgnoreClient = configProperties.getIgnoreClients().contains(WebUtils.getClientId(request));
            try {
                if (!isIgnoreClient) {
                    checkCode(request);
                }
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                final String errMsg = e.getMessage();
                return response.writeWith(Mono.create(monoSink -> {
                    try {
                        byte[] bytes = objectMapper.writeValueAsBytes(R.failed(errMsg));
                        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

                        monoSink.success(dataBuffer);
                    } catch (JsonProcessingException jsonProcessingException) {
                        log.error("对象输出异常", jsonProcessingException);
                        monoSink.error(jsonProcessingException);
                    }
                }));
            }
            return chain.filter(exchange);
        });
    }


    /**
     * 验证码校验
     *
     * @param request 请求
     */
    private void checkCode(ServerHttpRequest request) {
        String code = request.getQueryParams().getFirst("code");

        if (CharSequenceUtil.isBlank(code)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        String randomStr = request.getQueryParams().getFirst("randomStr");
        if (CharSequenceUtil.isBlank(randomStr)) {
            //随机码为空则获取手机号
            randomStr = request.getQueryParams().getFirst("mobile");
        }

        String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;

        //获取验证码缓存
        Object codeObj = redisTemplate.opsForValue().get(key);

        redisTemplate.delete(key);

        if (ObjectUtil.isEmpty(codeObj) || !code.equals(codeObj)) {
            throw new ValidateCodeException("验证码不合法");
        }
    }
}
