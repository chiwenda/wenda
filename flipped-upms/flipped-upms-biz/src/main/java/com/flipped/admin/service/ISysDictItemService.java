package com.flipped.admin.service;

import com.flipped.admin.api.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字典项 服务类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return
     */
    void removeDictItem(Long id);

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return
     */
    void updateDictItem(SysDictItem item);
}
