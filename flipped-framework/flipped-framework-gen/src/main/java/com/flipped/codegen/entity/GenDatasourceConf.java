package com.flipped.codegen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cwd.flipped.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据源表
 *
 * @author cwd
 * @date 2022/1/27 上午10:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("gen_datasource_conf")
public class GenDatasourceConf extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * jdbcUrl
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 删除标记
     */
    @TableLogic
    private String delFlag;


}
