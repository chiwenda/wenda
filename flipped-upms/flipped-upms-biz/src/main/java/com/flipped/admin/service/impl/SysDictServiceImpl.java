package com.flipped.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.flipped.admin.api.entity.SysDict;
import com.flipped.admin.api.entity.SysDictItem;
import com.flipped.admin.mapper.SysDictItemMapper;
import com.flipped.admin.mapper.SysDictMapper;
import com.flipped.admin.service.ISysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.common.core.contants.CacheConstants;
import com.flipped.common.core.contants.DictTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {
    private final SysDictItemMapper dictItemMapper;

    /**
     * 根据ID 删除字典
     * @param id 字典ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public void removeDict(Long id) {
        SysDict dict = this.getById(id);
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(dict.getSystemFlag()), "系统内置字典项目不能删除");
        baseMapper.deleteById(id);
        dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getDictId, id));
    }

    /**
     * 更新字典
     * @param dict 字典
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, key = "#dict.type")
    public void updateDict(SysDict dict) {
        SysDict sysDict = this.getById(dict.getId());
        // 系统内置
        Assert.state(!DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystemFlag()), "系统内置字典项目不能修改");
        this.updateById(dict);
    }

    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public void clearDictCache() {

    }

}
