package com.zkc.xcplus.system.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.zkc.xcplus")
@SpringBootApplication
public class SystemApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SystemApiApplication.class, args);
	}
}
