package com.zkc.xcplus.system.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域请求设置
 */
@Configuration
public class GlobalCorsConfig {
	
	@Bean
	public CorsFilter getCorsFilter() {
		
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedMethod("*");
		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		//允许携带cookie
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource configurationSourceSource = new UrlBasedCorsConfigurationSource();
		configurationSourceSource.registerCorsConfiguration("/**", configuration);
		
		return new CorsFilter(configurationSourceSource);
	}
}
