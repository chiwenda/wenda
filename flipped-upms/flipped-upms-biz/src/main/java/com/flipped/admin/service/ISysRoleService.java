package com.flipped.admin.service;

import com.flipped.admin.api.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 通过角色ID，删除角色
     *
     * @param id
     * @return
     */
    Boolean removeRoleById(Long id);
}
