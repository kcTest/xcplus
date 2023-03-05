package com.zkc.xcplus.content.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.zkc.xcplus")
@SpringBootApplication
public class ContentServiceApp {
	public static void main(String[] args) {
		SpringApplication.run(ContentServiceApp.class, args);
	}
}
