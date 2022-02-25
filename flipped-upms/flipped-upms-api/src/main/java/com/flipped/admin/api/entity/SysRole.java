package com.flipped.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.cwd.flipped.mybatis.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 角色表
 * @author cwd
 * @date 2022/1/26 下午4:37
 */
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "角色编号")
    private Long roleId;

    @NotBlank(message = "角色名称 不能为空")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @NotBlank(message = "角色标识 不能为空")
    @ApiModelProperty(value = "角色标识")
    private String roleCode;

    @NotBlank(message = "角色描述 不能为空")
    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    /**
     * 删除标识（0-正常,1-删除）
     */
    @TableLogic
    private String delFlag;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
