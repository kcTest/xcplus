package com.zkc.xcplus.media.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.zkc.xcplus")
@SpringBootApplication
public class MediaApiApp {
	public static void main(String[] args) {
		SpringApplication.run(MediaApiApp.class, args);
	}
}

