package com.zkc.xcplus.auth.config;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.zkc.xcplus.auth.config.pwd.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.zkc.xcplus.auth.config.pwd.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/oauth2
 * EnableWebSecurity  exposing a SecurityFilterChain
 */
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class AuthorizationServerConfig {
	
	@Value("${oauth2.client.clientid}")
	private String clientId;
	
	@Value("${oauth2.client.clientsecret}")
	private String clientSecret;
	
	@Value("${oauth2.client.redirecturi}")
	private String redirectUri;
	
	@Value("${oauth2.jwt.password}")
	private String password;
	
	@Value("${oauth2.jwt.alias}")
	private String alias;
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//请求最先匹配到的SecurityFilterChain有效 
		//默认配置 authorization code 访问授权相关端点需要进行用户登录授权 需要配置formLogin 
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.apply(http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.tokenEndpoint((tokenEndpoint) -> tokenEndpoint.accessTokenRequestConverter(
						new DelegatingAuthenticationConverter(Arrays.asList(
								new OAuth2AuthorizationCodeAuthenticationConverter(),
								new OAuth2RefreshTokenAuthenticationConverter(),
								new OAuth2ClientCredentialsAuthenticationConverter(),
								new OAuth2ResourceOwnerPasswordAuthenticationConverter()))
				)));
		SecurityFilterChain securityFilterChain = http.formLogin(Customizer.withDefaults()).build();
		//添加自定义 authenticationProvider
		//authenticationManager.authenticate->ProviderManager.authenticate->for (AuthenticationProvider provider : getProviders())
		//result = provider.authenticate(authentication);
		//				if (result != null) {
		//					copyDetails(authentication, result);
		//					break;
		//				}
		http.authenticationProvider(createOAuth2ResourceOwnerPasswordAuthenticationProvider(http));
		return securityFilterChain;
	}
	
	/**
	 * OAuth2TokenEndpointConfigurer.createDefaultAuthenticationProviders
	 */
	@SuppressWarnings("unchecked")
	private AuthenticationProvider createOAuth2ResourceOwnerPasswordAuthenticationProvider(HttpSecurity http) {
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
		OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);
		return new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator);
	}
	
	/**
	 * 默认配置
	 */
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.authorizationEndpoint("/oauth2/authorize")
				.tokenEndpoint("/oauth2/token")
				.jwkSetEndpoint("/oauth2/jwks")
				.tokenRevocationEndpoint("/oauth2/revoke")
				.tokenIntrospectionEndpoint("/oauth2/introspect")
				.oidcClientRegistrationEndpoint("/connect/register")
				.oidcUserInfoEndpoint("/userinfo").build();
	}
	
	@Bean
	public SecurityFilterChain standardSecurityFilterChain(HttpSecurity http) throws Exception {
		//authenticate with a username/password，  
		//Reading the Username & Password ,(Form \ Basic)
		//Basic HTTP Authentication  请求头: Authorization: Basic+Base64(用户名:密码)
		//Form Login 登录读取用户名和密码
		// 不包含context-path  其余url路径如果匹配到也需要身份验证 需要配置formLogin 
		http.authorizeHttpRequests(authorize -> {
					authorize
							//默认formLogin/login 允许访问   默认错误页面 允许访问 
							.requestMatchers("/login").permitAll()
							.requestMatchers("/error/**").permitAll()
							.anyRequest().authenticated();
				}
		);
		return http.formLogin(Customizer.withDefaults()).build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		//与加密密码时匹配  登录后使用该PasswordEncoder加密明文密码与用户已加密密码比较；client_secret同样
		//default uses strength 10
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient client = RegisteredClient
				.withId(UUID.randomUUID().toString())
				.clientId(clientId)
				//ClientSecretAuthenticationProvider PasswordEncoder
				.clientSecret(clientSecret)
				//在请求参数中
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				//请求code时scope匹配
				.scope("all")
				.tokenSettings(TokenSettings.builder()
						.authorizationCodeTimeToLive(Duration.ofMinutes(10))
						.accessTokenTimeToLive(Duration.ofDays(1))
						.refreshTokenTimeToLive(Duration.ofDays(2))
						.reuseRefreshTokens(false).build())
				//必填 code\token 追加到url
				.redirectUri(redirectUri)
				.build();
		return new InMemoryRegisteredClientRepository(client);
	}
	
	/**
	 * TokenSettings中token默认使用jwt形式  需定义JWKSource 从本地jks文件中获取秘钥
	 * If a JwtEncoder @Bean or JWKSource<SecurityContext> @Bean is registered,
	 * then a JwtGenerator is additionally composed in the DelegatingOAuth2TokenGenerator.
	 */
	@Bean
	public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new JWKSource<SecurityContext>() {
			@Override
			public List<JWK> get(JWKSelector jwkSelector, SecurityContext securityContext) throws KeySourceException {
				return jwkSelector.select(jwkSet);
			}
		};
	}
	
	@Bean
	public JwtDecoder jwtDecoder(KeyPair keyPair) {
		return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
	}
	
	@Bean
	public KeyPair generateRsaKey() {
		char[] pwdCharArr = password.toCharArray();
		KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), pwdCharArr);
		return factory.getKeyPair(alias, pwdCharArr);
	}
	
}
