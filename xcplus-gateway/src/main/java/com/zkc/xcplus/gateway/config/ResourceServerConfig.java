package com.zkc.xcplus.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@EnableWebFluxSecurity//gateway使用webflux
@Configuration
public class ResourceServerConfig {
	
	@Autowired
	private WhiteListConfig whiteListConfig;
	
	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
		List<String> urls = whiteListConfig.getUrls();
		http.authorizeExchange((authorize) ->
						authorize.pathMatchers(urls.toArray(new String[0])).permitAll()
								.anyExchange().authenticated()
				)
				.csrf().disable()//白名单POST方法Invalid CSRF Token 'null' was found on the request   禁用
				.oauth2ResourceServer().jwt();//Enables JWT Resource Server support.
//      默认ReactiveAuthenticationManager->DelegatingReactiveAuthenticationManager		
//		认证失败返回处理 Reactive
//		默认 ServerAuthenticationFailureHandler->
//		ServerAuthenticationEntryPointFailureHandler-> 默认 BearerTokenServerAuthenticationEntryPoint
		return http.build();
	}
	
}
