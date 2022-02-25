package com.flipped.codegen.entity;

import lombok.Data;

import java.util.List;

/**
 * 表属性
 * information_schema数据库中保存了所有表的字段
 *
 * @author cwd
 * @date 2022/1/27 上午10:48
 */
@Data
public class TableEntity {

    /**
     * 名称
     */
    private String tableName;

    /**
     * 备注
     */
    private String comments;

    /**
     * 主键
     */
    private ColumnEntity pk;

    /**
     * 列名
     */
    private List<ColumnEntity> columns;

    /**
     * 驼峰类型
     */
    private String caseClassName;

    /**
     * 普通类型
     */
    private String lowerClassName;
}
