package com.zkc.xcplus.checkcode.model;

import lombok.Data;

/**
 * 验证码生成结果
 */
@Data
public class CheckCodeResultDto {
	
	/**
	 * 用于验证
	 */
	private String key;
	
	/**
	 * 混淆后的内容
	 */
	private String aliasing;
}
