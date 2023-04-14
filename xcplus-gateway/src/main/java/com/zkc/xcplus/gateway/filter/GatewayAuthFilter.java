package com.zkc.xcplus.gateway.filter;

import com.zkc.xcplus.gateway.config.WhiteListConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class GatewayAuthFilter implements GlobalFilter, Ordered {
	
	@Autowired
	private WhiteListConfig whiteListConfig;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		BearerTokenServerAuthenticationEntryPoint之后
		ServerHttpRequest request = exchange.getRequest();
		String requestPath = request.getURI().getPath();
		PathMatcher pathMatcher = new AntPathMatcher();
		List<String> whiteList = whiteListConfig.getUrls();
		for (String url : whiteList) {
			if (pathMatcher.match(url, requestPath)) {
				//放行
				return chain.filter(exchange);
			}
		}
		return chain.filter(exchange);
	}
	
	/**
	 * 过滤器优先级最高
	 */
	@Override
	public int getOrder() {
		return 0;
	}
}
