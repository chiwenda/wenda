package com.flipped.codegen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cwd.flipped.mybatis.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生成记录
 *
 * @author cwd
 * @date 2022/1/27 上午10:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("gen_form_conf")
@ApiModel(value = "生成记录")
public class GenFormConf extends BaseEntity {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 表名称
     */
    @ApiModelProperty(value = "表名称")
    private String tableName;

    /**
     * 表单信息
     */
    @ApiModelProperty(value = "表单信息")
    private String formInfo;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记")
    private String delFlag;
}
