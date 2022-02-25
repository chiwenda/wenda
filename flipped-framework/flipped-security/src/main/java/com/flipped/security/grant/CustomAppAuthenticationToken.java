package com.flipped.security.grant;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author chiwenda
 * @date 2022/1/29 上午9:18
 */
public class CustomAppAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    //验证码/密码
    private String code;

    public CustomAppAuthenticationToken(Object principal, String code) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.principal = principal;
        this.code = code;
    }

    public CustomAppAuthenticationToken(UserDetails sysUser) {
        super(sysUser.getAuthorities());
        this.principal = sysUser;
        super.setAuthenticated(true); // 设置认证成功 必须
    }

    @Override
    public Object getCredentials() {
        return this.code;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
