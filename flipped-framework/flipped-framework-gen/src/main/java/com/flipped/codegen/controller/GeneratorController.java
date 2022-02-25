package com.flipped.codegen.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flipped.codegen.entity.GenConfig;
import com.flipped.codegen.service.GeneratorService;
import com.flipped.common.core.utils.R;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author chiwenda
 * @date 2022/1/27 下午7:34
 */
@Api(value = "generator", tags = "代码生成模块")
@RestController
@RequiredArgsConstructor
@RequestMapping("/generator")
public class GeneratorController {
    private final GeneratorService generatorService;

    /**
     * 列表
     *
     * @param tableName 参数集
     * @param dsName    数据源编号
     * @return 数据库表
     */
    @GetMapping("/page")
    public R<IPage<List<Map<String, Object>>>> getPage(Page<?> page, String tableName, String dsName) {
        return R.ok(generatorService.getPage(page, tableName, dsName));
    }

    /**
     * 预览代码
     *
     * @param genConfig 数据表配置
     */
    @GetMapping("/preview")
    public R<Map<String, String>> previewCode(GenConfig genConfig) {
        return R.ok(generatorService.previewCode(genConfig));
    }

    /**
     * 生成代码
     */
    @SneakyThrows
    @PostMapping("/code")
    public void generatorCode(@RequestBody GenConfig genConfig, HttpServletResponse response) {
        byte[] data = generatorService.generatorCode(genConfig);
        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s.zip", genConfig.getTableName()));
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }
}
