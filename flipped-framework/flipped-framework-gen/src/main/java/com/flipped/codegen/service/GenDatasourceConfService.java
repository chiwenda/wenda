package com.flipped.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flipped.codegen.entity.GenDatasourceConf;

/**
 * 数据源操作接口
 *
 * @author cwd
 * @date 2022/1/27 上午10:01
 */
public interface GenDatasourceConfService extends IService<GenDatasourceConf> {
    /**
     * 保存数据源并且加密
     *
     */
    Boolean saveDsByEnc(GenDatasourceConf genDatasourceConf);

    /**
     * 更新数据源
     *
     */
    Boolean updateDsByEnc(GenDatasourceConf genDatasourceConf);

    /**
     * 更新动态数据的数据源列表
     *
     */
    void addDynamicDataSource(GenDatasourceConf datasourceConf);

    /**
     * 校验数据源配置是否有效
     *
     * @param datasourceConf 数据源信息
     * @return 有效/无效
     */
    Boolean checkDataSource(GenDatasourceConf datasourceConf);

    /**
     * 通过数据源名称删除
     *
     * @param dsId 数据源ID
     */
    Boolean removeByDsId(Integer dsId);
}
