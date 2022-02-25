package com.flipped.admin.mapper;

import com.flipped.admin.api.entity.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * 关联dept——relation
     *
     * @return 数据列表
     */
    List<SysDept> listDepts();
}
