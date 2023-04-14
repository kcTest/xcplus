package com.zkc.xcplus.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@EnableWebFluxSecurity
@Configuration
public class ResourceServerConfig {
	
	@Autowired
	private WhiteListConfig whiteListConfig;
	
	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
		List<String> urls = whiteListConfig.getUrls();
		http.authorizeExchange((authorize) ->
						authorize.pathMatchers(urls.toArray(new String[urls.size()])).permitAll()
								.anyExchange().authenticated()
				)//Enables JWT Resource Server support.
				.oauth2ResourceServer().jwt();
//      默认ReactiveAuthenticationManager->DelegatingReactiveAuthenticationManager		
//		认证失败返回处理 Reactive
//		默认 ServerAuthenticationFailureHandler->
//		ServerAuthenticationEntryPointFailureHandler-> 默认 BearerTokenServerAuthenticationEntryPoint
		return http.build();
	}
	
}
