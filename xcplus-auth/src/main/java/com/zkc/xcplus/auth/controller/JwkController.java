//package com.zkc.xcplus.auth.controller;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.KeyPair;
//import java.security.interfaces.RSAPublicKey;
//import java.util.Map;
//
//@Tag(name = "JwkController", description = "jwk")
//@RestController
//public class JwkController {
//	
//	@Autowired
//	private KeyPair keyPair;
//	
//	@Operation(summary = "获取公钥")
//	@GetMapping("/jwk")
//	public Map<String, Object> jwk() {
//		RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).build();
//		return new JWKSet(rsaKey).toJSONObject();
//	}
//	
//}
