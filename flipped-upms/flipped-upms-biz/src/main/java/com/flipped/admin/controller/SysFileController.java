package com.flipped.admin.controller;


import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 文件管理表 前端控制器
 * </p>
 *
 * @author cwd
 * @since 2022-01-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-file")
@Api(value = "sys-file", tags = "文件管理")
public class SysFileController {

}

