package com.zkc.xcplus.checkcode.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 谷歌图形验证码配置
 */
@Configuration
public class KaptchaConfig {
	
	@Bean
	public DefaultKaptcha producer() {
		Properties properties = new Properties();
		properties.put(Constants.KAPTCHA_BORDER, "no");
		properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
		properties.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "25");
		properties.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "10");
		properties.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
		properties.put(Constants.KAPTCHA_IMAGE_HEIGHT, "34");
		properties.put(Constants.KAPTCHA_IMAGE_WIDTH, "138");
		properties.put(Constants.KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
		
		Config config = new Config(properties);
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		defaultKaptcha.setConfig(config);
		
		return defaultKaptcha;
	}
}
