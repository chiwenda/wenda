package com.flipped.admin.service;

import com.flipped.admin.api.entity.SysDept;
import com.flipped.admin.api.entity.SysDeptRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 部门关系表 服务类
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
public interface ISysDeptRelationService extends IService<SysDeptRelation> {

    /**
     * 新建部门关系
     *
     * @param sysDept 部门
     */
    void saveDeptRelation(SysDept sysDept);

    /**
     * 通过ID删除部门关系
     *
     * @param id
     */
    void removeDeptRelationById(Long id);

    /**
     * 更新部门关系
     *
     * @param relation
     */
    void updateDeptRelation(SysDeptRelation relation);

}
