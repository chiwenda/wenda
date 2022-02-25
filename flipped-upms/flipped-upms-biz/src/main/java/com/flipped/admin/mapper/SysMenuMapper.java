package com.flipped.admin.mapper;

import com.flipped.admin.api.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 通过角色编号查询菜单
     *
     * @param roleIds 角色ID
     * @return
     */
    Set<SysMenu> listMenusByRoleId(Long roleIds);

}
