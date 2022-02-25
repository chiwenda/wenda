package com.flipped.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flipped.admin.api.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 通过用户ID，查询角色信息
     * @param userId
     * @return
     */
    List<SysRole> listRolesByUserId(Long userId);
}
