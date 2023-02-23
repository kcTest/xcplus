package com.zkc.xcplus.system.service.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * No qualifying bean of type 'com.zkc.xcplus.content.service.dao.CourseBaseMapper'
 * Use this annotation to register MyBatis mapper interfaces when using Java Config
 */
@MapperScan("com.zkc.xcplus.system.service.dao")
@Configuration
public class MybatisPlusConfig {
	
	@Bean
	public MybatisPlusInterceptor getMybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}
	
}
