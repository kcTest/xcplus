package com.zkc.xcplus.auth.user.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
public class AuthParamsDto {
	
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 手机号
	 */
	private String cellphone;
	/**
	 * 验证码
	 */
	private String checkcode;
	/**
	 * 验证码key
	 */
	private String checkcodekey;
	/**
	 * 认证类型  password:用户名密码模式 sms:短信模式
	 */
	private String authType;
	/**
	 * 附加数据
	 */
	private Map<String, Object> payload = new HashMap<>();
}
