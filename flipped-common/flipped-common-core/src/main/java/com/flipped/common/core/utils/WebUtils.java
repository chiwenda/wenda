package com.flipped.common.core.utils;


import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONUtil;
import com.flipped.common.core.exception.CheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Web工具类
 *
 * @author cwd
 * @date 2022/1/14 下午5:19
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    private final static Logger log = LoggerFactory.getLogger(WebUtils.class);

    private final static String BASIC_ = "Basic ";

    private final static String UNKNOWN = "unknown";

    /**
     * 判断是否ajax请求 spring ajax 返回含有 ResponseBody 或者 RestController注解
     *
     * @param handlerMethod HandlerMethod
     * @return 是否ajax请求
     */
    public static boolean isBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = ClassUtils.getAnnotation(handlerMethod, ResponseBody.class);
        return null != responseBody;
    }

    /**
     * 读取cookie
     *
     * @param name cookie name
     * @return cookie value
     */
    public static String getCookieVal(String name) {
        if (WebUtils.getRequest().isPresent()) {
            return getCookieVal(WebUtils.getRequest().get(), name);
        }
        return null;
    }

    public static Optional<HttpServletRequest> getRequest() {
        return Optional
                .ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }


    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     */
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param key      cookie key
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置cookie
     *
     * @param response        HttpServletResponse
     * @param name            cookie name
     * @param value           cookie value
     * @param maxAgeInSeconds maxage
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return {HttpServletResponse}
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 返回json
     *
     * @param response HttpServletResponse
     * @param result   结果对象
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 返回json
     *
     * @param response    HttpServletResponse
     * @param result      结果对象
     * @param contentType contentType
     */
    public static void renderJson(HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        try (PrintWriter out = response.getWriter()) {
            out.append(JSONUtil.toJsonStr(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 从request 获取CLIENT_ID
     *
     * @return
     */
    public static String getClientId(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String result = null;
        try {
            result = splitClient(header)[0];
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return result;
    }


    public static String getClientId(HttpServletRequest request) {
        if (WebUtils.getRequest().isPresent()) {
            String header = WebUtils.getRequest().get().getHeader(HttpHeaders.AUTHORIZATION);
            String result = null;
            try {
                result = splitClient(header)[0];
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
            return result;
        }
        return null;
    }

    @NotNull
    private static String[] splitClient(String header) throws UnsupportedEncodingException {
        if (header == null || !header.startsWith(BASIC_)) {
            throw new CheckedException("请求头中client信息为空");
        }
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new CheckedException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new CheckedException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }


}
