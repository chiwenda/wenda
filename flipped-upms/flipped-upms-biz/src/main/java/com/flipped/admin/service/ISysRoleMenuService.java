package com.flipped.admin.service;

import com.flipped.admin.api.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 更新角色菜单
     *
     * @param role
     * @param roleId  角色
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return
     */
    Boolean saveRoleMenus(String role, Long roleId, String menuIds);
}
