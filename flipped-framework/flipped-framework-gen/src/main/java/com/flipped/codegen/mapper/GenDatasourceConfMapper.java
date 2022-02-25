package com.flipped.codegen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flipped.codegen.entity.GenDatasourceConf;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源持久层操作
 *
 * @author cwd
 * @date 2022/1/27 上午11:05
 */
@Mapper
public interface GenDatasourceConfMapper extends BaseMapper<GenDatasourceConf> {
}
