package com.flipped.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flipped.admin.api.entity.SysRole;
import com.flipped.admin.api.vo.RoleVo;
import com.flipped.admin.service.ISysRoleMenuService;
import com.flipped.admin.service.ISysRoleService;
import com.flipped.common.core.utils.R;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@Api(value = "role", tags = "角色管理模块")
public class SysRoleController {
    private final ISysRoleService sysRoleService;

    private final ISysRoleMenuService sysRoleMenuService;

    /**
     * 通过ID查询角色信息
     *
     * @param id ID
     * @return 角色信息
     */
    @GetMapping("/{id:\\d+}")
    public R<SysRole> getById(@PathVariable Long id) {
        return R.ok(sysRoleService.getById(id));
    }

    /**
     * 添加角色
     *
     * @param sysRole 角色信息
     * @return success、false
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_role_add')")
    public R<Boolean> save(@Valid @RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole 角色信息
     * @return success/false
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_role_edit')")
    public R<Boolean> update(@Valid @RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.updateById(sysRole));
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_role_del')")
    public R<Boolean> removeById(@PathVariable Long id) {
        return R.ok(sysRoleService.removeRoleById(id));
    }

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    public R<List<SysRole>> listRoles() {
        return R.ok(sysRoleService.list(Wrappers.emptyWrapper()));
    }

    /**
     * 分页查询角色信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public R<IPage<SysRole>> getRolePage(Page page) {
        return R.ok(sysRoleService.page(page, Wrappers.emptyWrapper()));
    }

    /**
     * 更新角色菜单
     *
     * @param roleVo 角色对象
     * @return success、false
     */
    @PutMapping("/menu")
    @PreAuthorize("@pms.hasPermission('sys_role_perm')")
    public R<Boolean> saveRoleMenus(@RequestBody RoleVo roleVo) {
        SysRole sysRole = sysRoleService.getById(roleVo.getRoleId());
        return R.ok(sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode(), roleVo.getRoleId(), roleVo.getMenuIds()));
    }
}

