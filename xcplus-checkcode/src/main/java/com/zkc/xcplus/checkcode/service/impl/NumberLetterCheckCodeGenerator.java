package com.zkc.xcplus.checkcode.service.impl;

import com.zkc.xcplus.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 包含数字字母的验证码生成器
 */
@Component
public class NumberLetterCheckCodeGenerator implements CheckCodeService.CheckCodeGenerator {
	
	@Override
	public String generate(int length) {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int charIdx = random.nextInt(36);
			sb.append(str.charAt(charIdx));
		}
		return sb.toString();
	}
}
