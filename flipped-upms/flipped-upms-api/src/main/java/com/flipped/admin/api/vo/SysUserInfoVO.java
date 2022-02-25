package com.flipped.admin.api.vo;

import com.flipped.admin.api.entity.SysUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cwd
 * @date 2022/2/1810:18
 **/
@Data
public class SysUserInfoVO implements Serializable {
    /**
     * 用户基本信息
     */
    private SysUser sysUser;

    /**
     * 权限标识集合
     */
    private String[] permissions;

    /**
     * 角色集合
     */
    private Long[] roles;
}
