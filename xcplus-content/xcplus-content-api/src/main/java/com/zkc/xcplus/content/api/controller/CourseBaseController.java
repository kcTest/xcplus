package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.CourseQueryParamsDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CourseBaseController", description = "课程查询")
@RestController("/coursebase")
public class CourseBaseController {
	
	@Operation(summary = "获取课程列表")
	@PostMapping("/list")
	public PageResult<CourseBase> list(PageParams pageParams, @RequestBody CourseQueryParamsDto queryParamsDto) {
		return null;
	}
}
