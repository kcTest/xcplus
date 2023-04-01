package com.zkc.xcplus.search.config;

import com.zkc.xcplus.base.config.BaseSpringDocConfig;
import com.zkc.xcplus.base.model.SwaggerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig extends BaseSpringDocConfig {
	
	@Value("${swagger.title}")
	private String title;
	
	@Value("${swagger.description}")
	private String description;
	
	@Override
	public SwaggerProperties swaggerProperties() {
		return SwaggerProperties.builder()
				.apiBasePackage("com.zkc.xcplus.content.api.controller")
				.title(title)
				.description(description)
				.version("1.0")
				.enableSecurity(true)
				.build();
	}
}