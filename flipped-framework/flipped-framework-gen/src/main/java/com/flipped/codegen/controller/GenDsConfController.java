package com.flipped.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flipped.codegen.entity.GenDatasourceConf;
import com.flipped.codegen.service.GenDatasourceConfService;
import com.flipped.common.core.utils.R;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源管理
 *
 * @author cwd
 * @date 2022/1/27 上午9:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dsconf")
@Api(value = "dsconf", tags = "数据源管理模块")
public class GenDsConfController {
    private final GenDatasourceConfService datasourceConfService;

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param datasourceConf 数据源表
     */
    @GetMapping("/page")
    public R<IPage<GenDatasourceConf>> getSysDatasourceConfPage(Page page, GenDatasourceConf datasourceConf) {
        return R.ok(datasourceConfService.page(page, Wrappers.query(datasourceConf)));
    }

    /**
     * 查询全部数据源
     */
    @GetMapping("/list")
    public R<List<GenDatasourceConf>> list() {
        return R.ok(datasourceConfService.list());
    }

    /**
     * 通过id查询数据源表
     *
     * @param id id
     * @return R
     */
    @GetMapping("/{id}")
    public R<GenDatasourceConf> getById(@PathVariable("id") Integer id) {
        return R.ok(datasourceConfService.getById(id));
    }

    /**
     * 新增数据源表
     *
     * @param datasourceConf 数据源表
     * @return R
     */
    @PostMapping
    public R<Boolean> save(@RequestBody GenDatasourceConf datasourceConf) {
        return R.ok(datasourceConfService.saveDsByEnc(datasourceConf));
    }

    /**
     * 修改数据源表
     *
     * @param conf 数据源表
     * @return R
     */
    @PutMapping
    public R<Boolean> updateById(@RequestBody GenDatasourceConf conf) {
        return R.ok(datasourceConfService.updateDsByEnc(conf));
    }

    /**
     * 通过id删除数据源表
     *
     * @param id id
     * @return R
     */
    @DeleteMapping("/{id}")
    public R<Boolean> removeById(@PathVariable Integer id) {
        return R.ok(datasourceConfService.removeByDsId(id));
    }

}
