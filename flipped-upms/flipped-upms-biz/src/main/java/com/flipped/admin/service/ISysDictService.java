package com.flipped.admin.service;

import com.flipped.admin.api.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
public interface ISysDictService extends IService<SysDict> {
    /**
     * 根据ID 删除字典
     *
     * @param id
     * @return
     */
    void removeDict(Long id);

    /**
     * 更新字典
     *
     * @param sysDict 字典
     * @return
     */
    void updateDict(SysDict sysDict);

    /**
     * 清除缓存
     */
    void clearDictCache();
}
