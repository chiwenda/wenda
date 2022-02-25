package com.flipped.admin.mapper;

import com.flipped.admin.api.entity.SysDeptRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 部门关系表 Mapper 接口
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Mapper
public interface SysDeptRelationMapper extends BaseMapper<SysDeptRelation> {
    /**
     * 删除部门节点关系
     * @param deptRelation 待删除的某一个部门节点
     */
    void deleteDeptRelations(SysDeptRelation deptRelation);

    /**
     * 删除部门节点关系,同时删除所有关联此部门子节点的部门关系
     * @param id 待删除的部门节点ID
     */
    void deleteDeptRelationsById(Long id);

    /**
     * 新增部门节点关系
     * @param deptRelation 待新增的部门节点关系
     */
    void insertDeptRelations(SysDeptRelation deptRelation);
}
