package com.zkc.xcplus.auth.controller;

import com.zkc.xcplus.auth.uccenter.dao.XcUserMapper;
import com.zkc.xcplus.auth.uccenter.model.XcUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TestController", description = "授权")
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
	
	@Autowired
	private XcUserMapper xcUserMapper;
	
	@Operation(summary = "模拟登录成功")
	@GetMapping(value = "/success")
	public String loginSuccess() {
		return "登录成功";
	}
	
	@Operation(summary = "/user/{id}")
	public XcUser getUser(@PathVariable("id") String id) {
		return xcUserMapper.selectById(id);
	}
	
	/**
	 * Method Security
	 * Expression-Based Access Control
	 */
	@GetMapping("/r1")
	@PreAuthorize("hasAuthority('r1')")
	public String r1() {
		return "访问资源r1";
	}
	
	@GetMapping("/r2")
	@PreAuthorize("hasAuthority('r2')")
	public String r2() {
		return "访问资源r2";
	}
}
