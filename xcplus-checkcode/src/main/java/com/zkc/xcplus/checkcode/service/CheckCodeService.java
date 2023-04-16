package com.zkc.xcplus.checkcode.service;

import com.zkc.xcplus.checkcode.model.CheckCodeParamsDto;
import com.zkc.xcplus.checkcode.model.CheckCodeResultDto;

public interface CheckCodeService {
	
	
	/**
	 * 生成验证码
	 *
	 * @param dto 生成验证码所需参数
	 * @return 验证码生成结果
	 */
	CheckCodeResultDto generate(CheckCodeParamsDto dto);
	
	/**
	 * 校验验证码
	 *
	 * @param key  验证key
	 * @param code 验证码
	 * @return 是否有效
	 */
	boolean verify(String key, String code);
	
	/**
	 * 验证码生成器
	 */
	interface CheckCodeGenerator {
		
		/**
		 * 生成验证码
		 *
		 * @param length 长度
		 * @return 验证码内容
		 */
		String generate(int length);
	}
	
	/**
	 * 验证码Key生成器
	 */
	interface KeyGenerator {
		
		/**
		 * 生成Key
		 *
		 * @param prefix 前缀
		 * @return Key
		 */
		String generate(String prefix);
	}
	
	/**
	 * 验证码存储
	 */
	interface CheckCodeStore {
		
		/**
		 * 向缓存设置key
		 *
		 * @param key    Key
		 * @param value  验证码内容
		 * @param expire 过期时间 单位秒
		 */
		void set(String key, String value, Integer expire);
		
		/**
		 * 获取验证码
		 *
		 * @param key 验证码对应的key
		 * @return 验证码
		 */
		String get(String key);
		
		/**
		 * 移除验证码
		 *
		 * @param key 验证码对应的key
		 */
		void remove(String key);
	}
}
