package com.zkc.xcplus.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.zkc.xcplus.gateway.config.RestErrorResponse;
import com.zkc.xcplus.gateway.config.WhiteListConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
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
		ServerHttpResponse response = exchange.getResponse();
		String requestPath = request.getURI().getPath();
		PathMatcher pathMatcher = new AntPathMatcher();
		List<String> whiteList = whiteListConfig.getUrls();
		for (String url : whiteList) {
			if (pathMatcher.match(url, requestPath)) {
				//放行
				return chain.filter(exchange);
			}
		}
		
		//提取token
		String tokenStr = extractToken(request);
		if (StringUtils.hasText(tokenStr)) {
			try {
				JWT jwt = JWTParser.parse(tokenStr);
				String userinfo =JSON.toJSONString(jwt.getJWTClaimsSet().getClaim("userinfo"));
				log.info("令牌用户信息：{}", userinfo);
				//ReadOnlyHttpHeaders 转变类型  将信息放入header 
				ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().header("userinfo", userinfo).build();
				exchange = exchange.mutate().request(serverHttpRequest).build();
			} catch (ParseException e) {
				return buildErrResponse("令牌解析异常", response);
			}
		}
		return chain.filter(exchange);
	}
	
	private String extractToken(ServerHttpRequest request) {
		String headerValue = request.getHeaders().getFirst("Authorization");
		if (!StringUtils.hasText(headerValue)) {
			return null;
		}
		final String JWT_TOKEN_PREFIX = "Bearer ";
		if (!headerValue.contains(JWT_TOKEN_PREFIX)) {
			return null;
		}
		return headerValue.replace(JWT_TOKEN_PREFIX, "");
	}
	
	/**
	 * 设置并返回错误响应
	 */
	private Mono<Void> buildErrResponse(String msg, ServerHttpResponse response) {
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
		String jsonStr = JSON.toJSONString(new RestErrorResponse(msg));
		byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		Mono<DataBuffer> bufferMono = Mono.just(buffer);
		return response.writeWith(bufferMono);
	}
	
	/**
	 * 过滤器优先级最高
	 */
	@Override
	public int getOrder() {
		return 0;
	}
}
