package com.flipped.gateway.filter;

import com.flipped.common.core.contants.SecurityConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;


/**
 * 全局拦截器，作用所有的微服务
 *
 * @author cwd
 * @date 2022/1/23 下午8:53
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗 2. 重写StripPrefix = 1,支持全局
 * <p>
 * 支持swagger添加X-Forwarded-Prefix header （F SR2 已经支持，不需要自己维护）
 */
public class RequestGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> headers.remove(SecurityConstants.FROM)).build();
        addOriginalRequestUrl(exchange, request.getURI());
        String rawPath = request.getURI().getRawPath();
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(1L)
                .collect(Collectors.joining("/"));
        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
