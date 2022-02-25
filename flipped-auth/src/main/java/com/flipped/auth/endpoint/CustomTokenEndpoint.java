package com.flipped.auth.endpoint;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flipped.common.core.contants.CacheConstants;
import com.flipped.common.core.contants.CommonConstants;
import com.flipped.common.core.contants.SecurityConstants;
import com.flipped.common.core.utils.R;
import com.flipped.common.core.utils.SpringContextHolder;
import com.flipped.security.annotation.Inner;
import com.flipped.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cwd
 * @date 2022/2/1413:01
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class CustomTokenEndpoint {
    private final ClientDetailsService clientDetailsService;

    private final TokenStore tokenStore;

    private final RedisTemplate<String, Object> redisTemplate;

    private final CacheManager cacheManager;


    /**
     * 认证页面
     *
     * @param modelAndView modelAndView
     * @param error        表单登录失败处理回调的错误信息
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
        modelAndView.setViewName("ftl/login");
        modelAndView.addObject("error", error);
        return modelAndView;
    }

    /**
     * 确认授权页面
     *
     * @param request      request
     * @param session      session
     * @param modelAndView modelAndView
     * @return ModelAndView
     */
    public ModelAndView confirm(HttpServletRequest request, HttpSession session, ModelAndView modelAndView) {
        Map<String, Object> scopeList = (Map<String, Object>) request.getAttribute("scopes");
        modelAndView.addObject("scopeList", scopeList.keySet());
        Object auth = session.getAttribute("authorizationRequest");
        if (null != auth) {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            modelAndView.addObject("app", clientDetails.getAdditionalInformation());
            modelAndView.addObject("user", SecurityUtils.getUser());
        }
        modelAndView.setViewName("ftl/confirm");
        return modelAndView;
    }

    @DeleteMapping("/logout")
    public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StrUtil.isBlank(authHeader)) {
            return R.ok();
        }
        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
        return removeToken(tokenValue);
    }


    @Inner
    @DeleteMapping("/{token}")
    public R<Boolean> removeToken(@PathVariable("token") String token) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
            return R.ok();
        }

        OAuth2Authentication auth2Authentication = tokenStore.readAuthentication(accessToken);

        // 清空用户信息
        Objects.requireNonNull(cacheManager.getCache(CacheConstants.USER_DETAILS)).evict(auth2Authentication.getName());

        // 清空access token
        tokenStore.removeAccessToken(accessToken);

        // 清空 refresh token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);

        // 处理自定义退出事件，保存相关日志
        SpringContextHolder.publishEvent(new LogoutSuccessEvent(auth2Authentication));
        return R.ok();
    }


    /**
     * 查询token
     *
     * @param params 分页参数
     * @return
     */
    @Inner
    @PostMapping("/page")
    public R<Page> tokenList(@RequestBody Map<String, Object> params) {
        // 根据分页参数获取对应数据
        String key = String.format("%sauth_to_access:*", CacheConstants.PROJECT_OAUTH_ACCESS);
        int current = MapUtil.getInt(params, CommonConstants.CURRENT);
        int size = MapUtil.getInt(params, CommonConstants.SIZE);
        Set<String> keys = redisTemplate.keys(key);
        List<String> pages = keys.stream().skip((current - 1) * size).limit(size).collect(Collectors.toList());
        Page result = new Page(current, size);
        result.setRecords(redisTemplate.opsForValue().multiGet(pages));
        result.setTotal(keys.size());
        return R.ok(result);
    }
}
