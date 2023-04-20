package com.zkc.xcplus.auth.config.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

@Component
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		/**
		 * ClientSettings.builder().requireAuthorizationConsent(true)
		 * OAuth2AuthorizationCodeRequestAuthenticationProvider->OAuth2AuthorizationConsentAuthenticationProvider
		 */
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		String id = getId(authorizationConsent);
		this.redisTemplate.opsForValue().set(id, authorizationConsent, 10, TimeUnit.MINUTES);
	}
	
	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		String id = getId(authorizationConsent);
		this.redisTemplate.delete(id);
	}
	
	@Override
	@Nullable
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		String id = getId(registeredClientId, principalName);
		return (OAuth2AuthorizationConsent) this.redisTemplate.opsForValue().get(id);
	}
	
	private static String getId(OAuth2AuthorizationConsent authorizationConsent) {
		return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}
	
	private static String getId(String registeredClientId, String principalName) {
		return String.format("consentStore:%s:%s", registeredClientId, principalName);
	}
	
}
