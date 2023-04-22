package com.zkc.xcplus.learning.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.zkc.xcplus.learning.service.feignclient")
@ComponentScan(basePackages = "com.zkc.xcplus")
@SpringBootApplication
public class LearningApp {
	public static void main(String[] args) {
		SpringApplication.run(LearningApp.class, args);
	}
}
