package com.zkc.xcplus.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.zkc.xcplus")
@SpringBootApplication
public class GatewayApp {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApp.class, args);
	}
}
