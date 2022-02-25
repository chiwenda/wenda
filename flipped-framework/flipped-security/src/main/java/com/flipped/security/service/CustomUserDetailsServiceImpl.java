package com.flipped.security.service;

import com.flipped.admin.api.dto.UserInfo;
import com.flipped.admin.api.feign.RemoteUserService;
import com.flipped.common.core.contants.CacheConstants;
import com.flipped.common.core.contants.SecurityConstants;
import com.flipped.common.core.utils.R;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户详细信息
 *
 * @author cwd
 * @date 2022/1/26 下午5:07
 */
@Primary
@Component
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final CacheManager cacheManager;
    private final RemoteUserService remoteUserService;

    public CustomUserDetailsServiceImpl(CacheManager cacheManager, RemoteUserService remoteUserService) {
        this.cacheManager = cacheManager;
        this.remoteUserService = remoteUserService;
    }

    /**
     * 手机号登录
     *
     * @param username 手机号
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null && cache.get(username) != null) {
            return (UserDetails) Objects.requireNonNull(cache.get(username)).get();
        }

        R<UserInfo> result = remoteUserService.info(username, SecurityConstants.FROM_IN);
        UserDetails userDetails = getUserDetails(result);
        if (cache != null) {
            cache.put(username, userDetails);
        }
        return userDetails;


    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
