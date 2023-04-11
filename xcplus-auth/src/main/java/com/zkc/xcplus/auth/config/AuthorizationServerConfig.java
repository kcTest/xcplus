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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
		//默认配置 authorization code 访问授权相关端点需要进行用户登录授权 需要配置formLogin 
//		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();
		RequestMatcher endpointsMatcher = configurer.getEndpointsMatcher();
		http
				.securityMatcher(endpointsMatcher)
				.authorizeHttpRequests(authorize ->
						authorize.requestMatchers("/oauth2/token")
								.permitAll()
								.anyRequest().authenticated()
				)
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
				.apply(configurer);
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
		http.authenticationProvider(createOAuth2ResourceOwnerPasswordAuthenticationProvider(http));
		return securityFilterChain;//顺序?
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
		// 不包含context-path  其余url路径如果匹配到也需要身份验证 需要配置formLogin 
		http.authorizeHttpRequests(authorize -> {
					authorize
							//默认formLogin/login 允许访问   默认错误页面 允许访问 
							.requestMatchers("/login").permitAll()
							.requestMatchers("/error/**").permitAll()
							.requestMatchers("/jwk/**").permitAll()
							.anyRequest().authenticated();
				}
		);
		return http.formLogin(Customizer.withDefaults()).build();
	}
	
	/**
	 * 临时
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("test").password("123").authorities("r1", "r2").build());
		return manager;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient client = RegisteredClient
				.withId(UUID.randomUUID().toString())
				.clientId(clientId)
				.clientSecret(clientSecret)
				//在请求参数中
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.PASSWORD)
				//请求code时scope匹配
				.scope("all")
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(Duration.ofHours(2))
						.refreshTokenTimeToLive(Duration.ofDays(3))
						.reuseRefreshTokens(false).build())
				//必填 code\token 追加到url
				.redirectUri(redirectUri)
				.build();
		return new InMemoryRegisteredClientRepository(client);
	}
	
	/**
	 * token使用jwt形式  从本地jks文件中获取秘钥
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
