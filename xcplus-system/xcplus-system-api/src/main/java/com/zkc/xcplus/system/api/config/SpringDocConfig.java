package com.zkc.xcplus.system.api.config;

import com.zkc.xcplus.base.config.BaseSpringDocConfig;
import com.zkc.xcplus.base.model.SwaggerProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig extends BaseSpringDocConfig {
	@Override
	public SwaggerProperties swaggerProperties() {
		return SwaggerProperties.builder()
				.apiBasePackage("com.zkc.xcplus.system.api.controller")
				.title("在线内容管理系统数据字典")
				.description("在线内容管理系统数据字典接口文档")
				.version("1.0")
				.enableSecurity(true)
				.build();
	}
}
