package com.flipped.admin.controller;


import cn.hutool.core.lang.tree.Tree;
import com.flipped.admin.api.entity.SysMenu;
import com.flipped.admin.service.ISysMenuService;
import com.flipped.common.core.utils.R;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
@Api(value = "menu", tags = "菜单管理模块")
public class SysMenuController {
    private final ISysMenuService sysMenuService;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @param parentId 父节点ID
     * @return 当前用户的树形菜单
     */
    @GetMapping
    public R<List<Tree<Long>>> getUserMenu(Long parentId) {
        // 获取符合条件的菜单
        Set<SysMenu> menuSet = sysMenuService.findMenuByRoleId(1L);
//        Set<SysMenu> menuSet = SecurityUtils.getRoles().stream().map(sysMenuService::findMenuByRoleId)
//                .flatMap(Collection::stream).collect(Collectors.toSet());
        return R.ok(sysMenuService.filterMenu(menuSet, parentId));
    }

    /**
     * 返回树形菜单集合
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    public R<List<Tree<Long>>> getTree(boolean lazy, Long parentId) {
        return R.ok(sysMenuService.treeMenu(lazy, parentId));
    }

    /**
     * 返回角色的菜单集合
     *
     * @param roleId 角色ID
     * @return 属性集合
     */
    @GetMapping("/tree/{roleId}")
    public R<List<Long>> getRoleTree(@PathVariable Long roleId) {
        return R.ok(
                sysMenuService.findMenuByRoleId(roleId).stream().map(SysMenu::getMenuId).collect(Collectors.toList()));
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id:\\d+}")
    public R<SysMenu> getById(@PathVariable Long id) {
        return R.ok(sysMenuService.getById(id));
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return 含ID 菜单信息
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_add')")
    public R<SysMenu> save(@Valid @RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return R.ok(sysMenu);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public R<Boolean> removeById(@PathVariable Long id) {
        return R.ok(sysMenuService.removeMenuById(id));
    }

    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_edit')")
    public R<Boolean> update(@Valid @RequestBody SysMenu sysMenu) {
        return R.ok(sysMenuService.updateMenuById(sysMenu));
    }

    /**
     * 清除菜单缓存
     */
    @DeleteMapping("/cache")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public R clearMenuCache() {
        sysMenuService.clearMenuCache();
        return R.ok();
    }
}

