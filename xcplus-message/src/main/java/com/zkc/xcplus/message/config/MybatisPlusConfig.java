package com.zkc.xcplus.message.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 重命名MybatisPlusConfig 否则默认名字和api-service的config冲突
 */
@Configuration("MybatisPlusConfigForMessage")
@MapperScan("com.zkc.xcplus.message.dao")
public class MybatisPlusConfig {
}
