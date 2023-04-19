package com.zkc.xcplus.auth.config.token;

import com.zkc.xcplus.auth.user.model.dto.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * If the OAuth2TokenGenerator is not provided as a @Bean or is not configured through the OAuth2AuthorizationServerConfigurer,
 * an OAuth2TokenCustomizer<JwtEncodingContext> @Bean will automatically be configured with a JwtGenerator.
 */
@Component
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
	
	@Override
	public void customize(JwtEncodingContext context) {
		JwtClaimsSet.Builder claims = context.getClaims();
		CustomUser customUser = (CustomUser) context.getPrincipal().getPrincipal();
		claims.claim("userinfo", customUser.getXcUser());
		//使用jwt形式token不会使用权限信息 需要单独设置 如果不使用默认name:SCOPE SCP以及前缀  资源服务器需自定义JwtAuthenticationConverter提取权限信息
		claims.claim("authorities", customUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
	}
}
