package com.zkc.xcplus.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@EnableWebFluxSecurity
@Configuration
public class ResourceServerConfig {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	String jwkSetUri;
	
	@Autowired
	private WhiteListConfig whiteListConfig;
	
	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
		List<String> urls = whiteListConfig.getUrls();
		http.authorizeExchange((authorize) ->
						authorize.pathMatchers(urls.toArray(new String[urls.size()])).permitAll()
								.anyExchange().authenticated()
				)
				.oauth2ResourceServer().jwt();
		return http.build();
	}
	
}
