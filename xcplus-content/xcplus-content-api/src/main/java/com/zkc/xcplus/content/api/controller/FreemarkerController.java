package com.zkc.xcplus.content.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * freemaker页面
 */
@Tag(name = "FreemarkerController", description = "freemaker页面")
@Controller
@RequestMapping("/fm")
public class FreemarkerController {
	
	@Operation(summary = "测试")
	@GetMapping("/aa")
	public ModelAndView test() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("test");
		mv.addObject("display", "内容");
		return mv;
	}
	
}
