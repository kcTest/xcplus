package com.zkc.xcplus.checkcode.controller;

import com.zkc.xcplus.checkcode.model.CheckCodeParamsDto;
import com.zkc.xcplus.checkcode.model.CheckCodeResultDto;
import com.zkc.xcplus.checkcode.service.CheckCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CheckCodeController", description = "验证码服务接口")
@RestController
public class CheckCodeController {
	
	@Resource(name = "PicCheckCodeService")
	private CheckCodeService picCheckCodeService;
	
	@Operation(summary = "生成验证码信息")
	@Parameters({
			@Parameter(name = "name", description = "业务名称", required = true, in = ParameterIn.QUERY, content = @Content(schema = @Schema(type = "String"))),
			@Parameter(name = "key", description = "验证码key", required = true, in = ParameterIn.QUERY, content = @Content(schema = @Schema(type = "String"))),
			@Parameter(name = "code", description = "验证码", required = true, in = ParameterIn.QUERY, content = @Content(schema = @Schema(type = "String")))
	})
	@PostMapping("/pic")
	public CheckCodeResultDto generatePicCheckCode(CheckCodeParamsDto dto) {
		return picCheckCodeService.generate(dto);
	}
	
	@Operation(summary = "校验")
	@PostMapping("/verify")
	public Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code) {
		return picCheckCodeService.verify(key, code);
	}
}
