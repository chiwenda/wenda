package com.flipped.security.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 扩展用户信息
 * @author cwd
 * @date 2022/1/26 下午4:55
 */
public class User extends org.springframework.security.core.userdetails.User {

    /**
     * 用户ID
     */
    private final Long id;

    /**
     * 部门ID
     */
    private final Long deptId;

    /**
     * 手机号
     */
    private final String phone;


    public User(Long id, Long deptId, String username, String password, String phone, boolean enabled,
                boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.deptId = deptId;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public String getPhone() {
        return phone;
    }
}
