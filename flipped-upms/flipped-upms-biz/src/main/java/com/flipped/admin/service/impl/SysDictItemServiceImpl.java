package com.flipped.admin.service.impl;

import com.flipped.admin.api.entity.SysDict;
import com.flipped.admin.api.entity.SysDictItem;
import com.flipped.admin.mapper.SysDictItemMapper;
import com.flipped.admin.service.ISysDictItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.admin.service.ISysDictService;
import com.flipped.common.core.contants.CacheConstants;
import com.flipped.common.core.contants.DictTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>
 * 字典项 服务实现类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    private final ISysDictService dictService;

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public void removeDictItem(Long id) {
        // 根据ID查询字典ID
        SysDictItem dictItem = this.getById(id);
        SysDict dict = dictService.getById(dictItem.getDictId());
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag()), "系统内置字典项目不能删除");
        this.removeById(id);
    }

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#item.type")
    public void updateDictItem(SysDictItem item) {
        // 查询字典
        SysDict dict = dictService.getById(item.getDictId());
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag()), "系统内置字典项目不能修改");
        this.updateById(item);
    }

}
