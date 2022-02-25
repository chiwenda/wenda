package com.flipped.admin.api.dto;

import com.flipped.admin.api.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author cwd
 * @date 2022/2/1810:26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDto extends SysUser {
    /**
     * 角色ID
     */
    private List<Long> role;

    private Long deptId;

    /**
     * 新密码
     */
    private String newpassword1;
}
