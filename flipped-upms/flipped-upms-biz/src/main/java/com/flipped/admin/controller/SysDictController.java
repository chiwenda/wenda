package com.flipped.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flipped.admin.api.entity.SysDict;
import com.flipped.admin.api.entity.SysDictItem;
import com.flipped.admin.service.ISysDictItemService;
import com.flipped.admin.service.ISysDictService;
import com.flipped.common.core.contants.CacheConstants;
import com.flipped.common.core.utils.R;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict")
@Api(value = "dict", tags = "字典管理模块")
public class SysDictController {
    private final ISysDictItemService sysDictItemService;

    private final ISysDictService sysDictService;

    /**
     * 通过ID查询字典信息
     *
     * @param id ID
     * @return 字典信息
     */
    @GetMapping("/{id:\\d+}")
    public R<SysDict> getById(@PathVariable Long id) {
        return R.ok(sysDictService.getById(id));
    }

    /**
     * 分页查询字典信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public R<IPage<SysDict>> getDictPage(Page page, SysDict sysDict) {
        return R.ok(sysDictService.page(page, Wrappers.query(sysDict)));
    }

    /**
     * 通过字典类型查找字典
     *
     * @param type 类型
     * @return 同类型字典
     */
    @GetMapping("/type/{type}")
    @Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type")
    public R<List<SysDictItem>> getDictByType(@PathVariable String type) {
        return R.ok(sysDictItemService.list(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getType, type)));
    }

    /**
     * 添加字典
     *
     * @param sysDict 字典信息
     * @return success、false
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dict_add')")
    public R<Boolean> save(@Valid @RequestBody SysDict sysDict) {
        return R.ok(sysDictService.save(sysDict));
    }

    /**
     * 删除字典，并且清除字典缓存
     *
     * @param id ID
     * @return R
     */
    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    public R removeById(@PathVariable Long id) {
        sysDictService.removeDict(id);
        return R.ok();
    }

    /**
     * 修改字典
     *
     * @param sysDict 字典信息
     * @return success/false
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_dict_edit')")
    public R updateById(@Valid @RequestBody SysDict sysDict) {
        sysDictService.updateDict(sysDict);
        return R.ok();
    }

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param sysDictItem 字典项
     * @return
     */
    @GetMapping("/item/page")
    public R<IPage<SysDictItem>> getSysDictItemPage(Page page, SysDictItem sysDictItem) {
        return R.ok(sysDictItemService.page(page, Wrappers.query(sysDictItem)));
    }

    /**
     * 通过id查询字典项
     *
     * @param id id
     * @return R
     */
    @GetMapping("/item/{id:\\d+}")
    public R<SysDictItem> getDictItemById(@PathVariable("id") Long id) {
        return R.ok(sysDictItemService.getById(id));
    }

    /**
     * 新增字典项
     *
     * @param sysDictItem 字典项
     * @return R
     */
    @PostMapping("/item")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R<Boolean> save(@RequestBody SysDictItem sysDictItem) {
        return R.ok(sysDictItemService.save(sysDictItem));
    }

    /**
     * 修改字典项
     *
     * @param sysDictItem 字典项
     * @return R
     */
    @PutMapping("/item")
    public R updateById(@RequestBody SysDictItem sysDictItem) {
        sysDictItemService.updateDictItem(sysDictItem);
        return R.ok();
    }

    /**
     * 通过id删除字典项
     *
     * @param id id
     * @return R
     */
    @DeleteMapping("/item/{id:\\d+}")
    public R removeDictItemById(@PathVariable Long id) {
        sysDictItemService.removeDictItem(id);
        return R.ok();
    }

    @DeleteMapping("/cache")
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    public R clearDictCache() {
        sysDictService.clearDictCache();
        return R.ok();
    }
}

