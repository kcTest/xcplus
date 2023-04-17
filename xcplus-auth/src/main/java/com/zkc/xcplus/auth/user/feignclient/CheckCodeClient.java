package com.zkc.xcplus.auth.user.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "checkcode", path = "/checkcode", fallbackFactory = CheckCodeClientFallbackFactory.class)
public interface CheckCodeClient {
	
	@PostMapping(value = "/verify")
	public Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code);
}
