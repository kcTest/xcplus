package com.zkc.xcplus.checkcode.model;

import lombok.Data;

/**
 * 生成验证码所需参数
 */
@Data
public class CheckCodeParamsDto {
	
	/**
	 * 验证码类型
	 */
	private String checkCodeType;
	
	/**
	 * 业务参数
	 */
	private String param1;
	private String param2;
	private String param3;
}
