package com.zkc.xcplus.orders.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.zkc.xcplus")
@SpringBootApplication
public class OrderApp {
	
	public static void main(String[] args) {
		SpringApplication.run(OrderApp.class, args);
	}
}
