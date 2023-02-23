package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.CourseQueryParamsDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pczkc
 */
@Tag(name = "CourseBaseController", description = "课程管理")
@RestController
@RequestMapping("/coursebase")
public class CourseBaseController {
	
	@Autowired
	private CourseBaseInfoService courseBaseInfoService;
	
	@Operation(summary = "课程查询")
	@PostMapping("/list")
	public PageResult<CourseBase> list(PageParams pageParams, @RequestBody CourseQueryParamsDto dto) {
		
		return courseBaseInfoService.courseBaseList(pageParams, dto);
	}
}
