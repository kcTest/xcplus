package com.zkc.xcplus.gateway.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "whitelist")
public class WhiteListConfig {
	
	private List<String> urls;
}
