package com.zkc.xcplus.content.api.config;

import com.zkc.xcplus.base.config.BaseSpringDocConfig;
import com.zkc.xcplus.base.model.SwaggerProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig extends BaseSpringDocConfig {
	@Override
	public SwaggerProperties swaggerProperties() {
		return SwaggerProperties.builder()
				.apiBasePackage("com.zkc.xcplus.content.api.controller")
				.title("在线内容管理系统")
				.description("在线内容管理系统后台接口文档")
				.version("1.0")
				.enableSecurity(true)
				.build();
	}
}
