package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.CourseQueryParamsDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "CourseBaseController", description = "课程管理")
@RestController
@RequestMapping("/coursebase")
public class CourseBaseController {
	
	@Operation(summary = "课程查询")
	@PostMapping("/list")
	public PageResult<CourseBase> list(PageParams pageParams, @RequestBody CourseQueryParamsDto queryParamsDto) {
		CourseBase courseBase = new CourseBase();
		courseBase.setName("测试名称");
		courseBase.setCreateDate(LocalDateTime.now());
		List<CourseBase> courseBases = new ArrayList<>();
		courseBases.add(courseBase);
		PageResult<CourseBase> pageResult = new PageResult<>(courseBases, 1, 1, 10);
		return pageResult;
	}
}
