package com.flipped.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flipped.codegen.entity.GenFormConf;

/**
 * 表单管理
 *
 * @author cwd
 * @date 2022/1/27 下午7:20
 */
public interface GenFormConfService extends IService<GenFormConf> {
    /**
     * 获取表单信息
     * @param dsName 数据源ID
     * @param tableName 表名称
     */
    String getForm(String dsName, String tableName);
}
