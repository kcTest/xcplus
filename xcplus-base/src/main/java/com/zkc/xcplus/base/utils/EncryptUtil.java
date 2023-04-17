package com.zkc.xcplus.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class EncryptUtil {
	
	public static String encodeBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}
	
	public static String encodeUTF8StringBase64(String str) {
		String encoded = null;
		try {
			encoded = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			log.warn("字符串编码异常", e);
		}
		return encoded;
	}
}
