package com.zkc.xcplus.base.config;

import com.zkc.xcplus.base.model.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

/**
 * swagger基础配置
 */
public abstract class BaseSpringDocConfig {
	
	@Bean
	public OpenAPI openAPI() {
		
		SwaggerProperties properties = swaggerProperties();
		
		OpenAPI openAPI = new OpenAPI()
				.info(new Info()
						.title(properties.getTitle())
						.version(properties.getVersion())
						.description(properties.getDescription()));
		if (properties.isEnableSecurity()) {
			openAPI.components(new Components()
							.addSecuritySchemes("k1",
									new SecurityScheme()
											.type(SecurityScheme.Type.APIKEY)
											.in(SecurityScheme.In.HEADER)
											.name("Authorization")))
					.addSecurityItem(new SecurityRequirement().addList("k1"));
		}
		
		return openAPI;
	}
	
	
	public abstract SwaggerProperties swaggerProperties();
}

