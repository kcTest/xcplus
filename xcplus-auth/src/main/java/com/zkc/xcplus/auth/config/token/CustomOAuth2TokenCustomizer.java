package com.zkc.xcplus.auth.config.token;

import com.zkc.xcplus.auth.user.model.dto.CustomUser;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

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
	}
}
