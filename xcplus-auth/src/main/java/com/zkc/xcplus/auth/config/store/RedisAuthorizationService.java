package com.zkc.xcplus.auth.config.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OAuth2AuthorizationService(Token Store) OAuth2AuthorizationConsentService(OAuth2AuthorizationConsent STORE)
 * <p>
 * InMemoryOAuth2AuthorizationService  InMemoryOAuth2AuthorizationConsentService
 * is recommended ONLY to be used during development and testing
 * <p>
 * 暂时只定义CustomAuthorizationService
 */

@Component
public class RedisAuthorizationService implements OAuth2AuthorizationService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		long timeout = 10;
		if (hasState(authorization)) {
			String token = authorization.getAttribute(OAuth2ParameterNames.STATE);
			redisTemplate.setValueSerializer(RedisSerializer.java());
			redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.STATE, token), authorization, timeout, TimeUnit.MINUTES);
		}
		
		if (hasCode(authorization)) {
			OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
					.getToken(OAuth2AuthorizationCode.class);
			OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
			timeout = ChronoUnit.SECONDS.between(authorizationCodeToken.getIssuedAt(), authorizationCodeToken.getExpiresAt());
			redisTemplate.setValueSerializer(RedisSerializer.java());
			redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()), authorization, timeout, TimeUnit.SECONDS);
		}
		
		if (hasAccessToken(authorization)) {
			OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
			timeout = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
			redisTemplate.setValueSerializer(RedisSerializer.java());
			redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()), authorization, timeout, TimeUnit.SECONDS);
		}
		
		if (hasRefreshToken(authorization)) {
			OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
			timeout = ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
			redisTemplate.setValueSerializer(RedisSerializer.java());
			redisTemplate.opsForValue().set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()), authorization, timeout, TimeUnit.SECONDS);
		}
	}
	
	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		
		List<String> keys = new ArrayList<>();
		if (hasState(authorization)) {
			String token = authorization.getAttribute(OAuth2ParameterNames.STATE);
			keys.add(buildKey(OAuth2ParameterNames.STATE, token));
		}
		
		if (hasCode(authorization)) {
			OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
			OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
			keys.add(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()));
		}
		
		if (hasRefreshToken(authorization)) {
			OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
			keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()));
		}
		
		if (hasAccessToken(authorization)) {
			OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
			keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()));
		}
		redisTemplate.delete(keys);
	}
	
	@Override
	@Nullable
	public OAuth2Authorization findById(String id) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Nullable
	public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");
		redisTemplate.setValueSerializer(RedisSerializer.java());
		return (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(tokenType.getValue(), token));
	}
	
	private String buildKey(String type, String token) {
		return String.format("tokenStore:%s:%s", type, token);
	}
	
	private static boolean hasState(OAuth2Authorization authorization) {
		return Objects.nonNull(authorization.getAttribute(OAuth2ParameterNames.STATE));
	}
	
	private static boolean hasCode(OAuth2Authorization authorization) {
		return Objects.nonNull(authorization.getToken(OAuth2AuthorizationCode.class));
	}
	
	private static boolean hasRefreshToken(OAuth2Authorization authorization) {
		return Objects.nonNull(authorization.getRefreshToken());
	}
	
	private static boolean hasAccessToken(OAuth2Authorization authorization) {
		return Objects.nonNull(authorization.getAccessToken());
	}
}
