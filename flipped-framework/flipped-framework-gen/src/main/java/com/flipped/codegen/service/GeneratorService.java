package com.flipped.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flipped.codegen.entity.GenConfig;

import java.util.List;
import java.util.Map;

/**
 * 代码生成业务接口
 *
 * @author cwd
 * @date 2022/1/27 下午7:18
 */
public interface GeneratorService {
    /**
     * 生成代码
     * @param tableNames 表名称
     */
    byte[] generatorCode(GenConfig tableNames);

    /**
     * 分页查询表
     * @param page 分页信息
     * @param tableName 表名
     * @param name 数据源ID
     */
    IPage<List<Map<String, Object>>> getPage(Page<?> page, String tableName, String name);

    /**
     * 预览代码
     * @param genConfig 查询条件
     */
    Map<String, String> previewCode(GenConfig genConfig);

}
