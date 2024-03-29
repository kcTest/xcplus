package com.zkc.xcplus.content.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 指定上级包名 扫描到其它项目的config（去除org.springframework.boot.autoconfigure.AutoConfiguration.imports）
 */
@ComponentScan("com.zkc.xcplus")
@SpringBootApplication
@EnableFeignClients(basePackages = "com.zkc.xcplus.content.service.feignclient")
public class ContentApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ContentApiApplication.class, args);
	}
}
