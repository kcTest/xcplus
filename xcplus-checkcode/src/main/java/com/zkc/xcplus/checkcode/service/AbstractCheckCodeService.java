package com.zkc.xcplus.checkcode.service;

import com.zkc.xcplus.checkcode.model.CheckCodeParamsDto;
import com.zkc.xcplus.checkcode.model.CheckCodeResultDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 验证码接口
 */
@Slf4j
public abstract class AbstractCheckCodeService implements CheckCodeService {
	
	protected CheckCodeGenerator checkCodeGenerator;
	protected KeyGenerator keyGenerator;
	protected CheckCodeStore checkCodeStore;
	
	public abstract void setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator);
	
	public abstract void setKeyGenerator(KeyGenerator keyGenerator);
	
	public abstract void setCheckCodeStore(CheckCodeStore checkCodeStore);
	
	@Override
	public abstract CheckCodeResultDto generate(CheckCodeParamsDto dto);
	
	public GenerateResult generate(CheckCodeParamsDto dto, Integer length, String prefix, Integer expire) {
		String code = checkCodeGenerator.generate(length);
		log.debug("生成验证码：{}", code);
		String key = keyGenerator.generate(prefix);
		//存储验证码
		checkCodeStore.set(key, code, expire);
		//返回验证码生成结果
		GenerateResult result = new GenerateResult();
		result.setKey(key);
		result.setCode(code);
		return result;
	}
	
	@Override
	public boolean verify(String key, String code) {
		if (!StringUtils.hasText(key) || !StringUtils.hasText(code)) {
			return false;
		}
		String storeCode = checkCodeStore.get(key);
		if (storeCode == null) {
			return false;
		}
		boolean result = storeCode.equalsIgnoreCase(code);
		if (result) {
			checkCodeStore.remove(key);
		}
		return result;
	}
	
	@Data
	protected class GenerateResult {
		String key;
		String code;
	}
}
