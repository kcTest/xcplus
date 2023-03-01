package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.AddCourseDto;
import com.zkc.xcplus.content.model.dto.CourseBaseInfoDto;
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
@Tag(name = "CourseBaseInfoController", description = "课程基本信息管理")
@RestController
@RequestMapping("/coursebaseinfo")
public class CourseBaseInfoController {
	
	@Autowired
	private CourseBaseInfoService courseBaseInfoService;
	
	@Operation(summary = "课程查询")
	@PostMapping("/list")
	public PageResult<CourseBase> list(PageParams pageParams, @RequestBody CourseQueryParamsDto dto) {
		return courseBaseInfoService.courseBaseList(pageParams, dto);
	}
	
	@Operation(summary = "新增课程")
	@PostMapping("/add")
	public CourseBaseInfoDto add(@RequestBody AddCourseDto addCourseDto) {
		
		//TODO 获取机构名称和ID
		
		Long companyId = 1L;
		return courseBaseInfoService.add(companyId, addCourseDto);
	}
	
}
