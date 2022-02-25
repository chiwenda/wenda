package com.flipped.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flipped.admin.api.dto.UserDTO;
import com.flipped.admin.api.dto.UserInfo;
import com.flipped.admin.api.entity.SysUser;
import com.flipped.admin.api.vo.SysUserInfoVO;
import com.flipped.admin.api.vo.UserExcelVO;
import com.flipped.admin.api.vo.UserVO;
import com.flipped.admin.service.ISysUserService;
import com.flipped.common.core.utils.R;
import com.flipped.security.annotation.Inner;
import com.flipped.security.utils.SecurityUtils;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Api(value = "user", tags = "用户管理模块")
public class SysUserController {
    private final ISysUserService userService;

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping(value = {"/info"})
    public R<SysUserInfoVO> info() {
//        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, "admin"));
        if (user == null) {
            return R.failed("获取当前用户信息失败");
        }
        UserInfo userInfo = userService.getUserInfo(user);
        SysUserInfoVO vo = new SysUserInfoVO();
        vo.setSysUser(userInfo.getSysUser());
        vo.setRoles(userInfo.getRoles());
        vo.setPermissions(userInfo.getPermissions());
        return R.ok(vo);
    }

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @Inner
    @GetMapping("/info/{username}")
    public R<UserInfo> info(@PathVariable String username) {
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed(String.format("用户信息为空 %s", username));
        }
        return R.ok(userService.getUserInfo(user));
    }

    /**
     * 根据部门id，查询对应的用户 id 集合
     *
     * @param deptIds 部门id 集合
     * @return 用户 id 集合
     */
    @Inner
    @GetMapping("/ids")
    public R<List<Long>> listUserIdByDeptIds(@RequestParam("deptIds") Set<Long> deptIds) {
        return R.ok(userService.listUserIdByDeptIds(deptIds));
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id:\\d+}")
    public R<UserVO> user(@PathVariable Long id) {
        return R.ok(userService.getUserVoById(id));
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/details/{username}")
    public R<SysUser> user(@PathVariable String username) {
        SysUser condition = new SysUser();
        condition.setUsername(username);
        return R.ok(userService.getOne(new QueryWrapper<>(condition)));
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    public R<Boolean> userDel(@PathVariable Long id) {
        SysUser sysUser = userService.getById(id);
        return R.ok(userService.removeUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public R<Boolean> user(@RequestBody UserDTO userDto) {
        return R.ok(userService.saveUser(userDto));
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public R<Boolean> updateUser(@Valid @RequestBody UserDTO userDto) {
        return R.ok(userService.updateUser(userDto));
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @GetMapping("/page")
    public R<IPage<List<UserVO>>> getUserPage(Page page, UserDTO userDTO) {
        return R.ok(userService.getUserWithRolePage(page, userDTO));
    }

    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @return success/false
     */
    @PutMapping("/edit")
    public R<Boolean> updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        return R.ok(userService.updateUserInfo(userDto));
    }

    /**
     * @param username 用户名称
     * @return 上级部门用户列表
     */
    @GetMapping("/ancestor/{username}")
    public R<List<SysUser>> listAncestorUsers(@PathVariable String username) {
        return R.ok(userService.listAncestorUsersByUsername(username));
    }

    /**
     * 导出excel 表格
     *
     * @param userDTO 查询条件
     * @return
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_user_import_export')")
    public List<UserExcelVO> export(UserDTO userDTO) {
        return userService.listUser(userDTO);
    }

    /**
     * 导入用户
     *
     * @param excelVOList   用户列表
     * @param bindingResult 错误信息列表
     * @return R
     */
    @PostMapping("/import")
    @PreAuthorize("@pms.hasPermission('sys_user_import_export')")
    public R importUser(@RequestExcel List<UserExcelVO> excelVOList, BindingResult bindingResult) {
        return userService.importUser(excelVOList, bindingResult);
    }
}

