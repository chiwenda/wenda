package com.flipped.admin.api.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户excel 对应的实体
 *
 * @author lengleng
 * @date 2021/8/4
 */
@Data
public class UserExcelVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空")
	private String username;

	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	private String phone;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	private String deptName;

	/**
	 * 角色列表
	 */
	@NotBlank(message = "角色不能为空")
	private String roleNameList;

	/**
	 * 锁定标记
	 */
	private String lockFlag;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
