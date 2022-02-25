package com.flipped.security.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;


/**
 * 改造 {@link BearerTokenExtractor} 对公开权限的请求不进行校验
 *
 * @author cwd
 * @date 2022/2/168:56
 **/
public class CustomBearerTokenExtractor extends BearerTokenExtractor {

    private final PathMatcher pathMatcher;

    private final PermitAllUrlProperties permitAllUrlProperties;

    public CustomBearerTokenExtractor(PermitAllUrlProperties permitAllUrlProperties) {
        this.permitAllUrlProperties = permitAllUrlProperties;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public Authentication extract(HttpServletRequest request) {
        boolean match = permitAllUrlProperties.getUrls().stream()
                .anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));
        return match ? null : super.extract(request);
    }
}
