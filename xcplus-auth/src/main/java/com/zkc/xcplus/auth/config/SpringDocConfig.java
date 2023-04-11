package com.zkc.xcplus.auth.config;

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
				.apiBasePackage("com.zkc.xcplus.oauth.controller")
				.title(title)
				.description(description)
				.version("1.0")
				.enableSecurity(true)
				.build();
	}
}
