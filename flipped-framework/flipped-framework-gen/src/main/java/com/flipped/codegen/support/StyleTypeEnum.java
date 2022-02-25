package com.flipped.codegen.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代码生成风格
 *
 * @author cwd
 * @date 2022/1/27 上午10:31
 */
@Getter
@AllArgsConstructor
public enum StyleTypeEnum {
    /**
     * 前端类型-avue 风格
     */
    AVUE("0", "avue 风格"),

    /**
     * 前端类型-element 风格
     */
    ELEMENT("1", "element 风格");

    /**
     * 类型
     */
    private String style;

    /**
     * 描述
     */
    private String description;

}
