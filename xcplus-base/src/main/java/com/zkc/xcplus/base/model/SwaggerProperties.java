package com.zkc.xcplus.base.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SwaggerProperties {
	
	/**
	 * 应用名称
	 */
	private String name;
	
	/**
	 * API文档生成基础路径
	 */
	private String apiBasePackage;
	
	/**
	 * 是否启用登录认证
	 */
	private boolean enableSecurity;
	
	/**
	 * 文档标题
	 */
	private String title;
	
	/**
	 * 文档描述
	 */
	private String description;
	
	/**
	 * 文档版本
	 */
	private String version;
}