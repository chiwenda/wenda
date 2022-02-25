package com.flipped.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.flipped.admin.api.entity.SysRole;
import com.flipped.admin.api.entity.SysRoleMenu;
import com.flipped.admin.mapper.SysRoleMapper;
import com.flipped.admin.mapper.SysRoleMenuMapper;
import com.flipped.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.common.core.contants.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    private final SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 通过角色ID，删除角色,并清空角色菜单缓存
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public Boolean removeRoleById(Long id) {
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>update().lambda().eq(SysRoleMenu::getRoleId, id));
        return this.removeById(id);
    }

}
