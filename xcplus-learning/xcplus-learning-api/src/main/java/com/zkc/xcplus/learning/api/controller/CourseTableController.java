package com.zkc.xcplus.learning.api.controller;

import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.learning.model.dto.MyCourseTablesQueryParams;
import com.zkc.xcplus.learning.model.dto.XcCourseTablesDto;
import com.zkc.xcplus.learning.model.po.XcChooseCourse;
import com.zkc.xcplus.learning.model.po.XcCourseTables;
import com.zkc.xcplus.learning.service.CourseTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CourseTableController", description = "我的课程表接口")
@RestController
@RequestMapping("/choosecourse")
public class CourseTableController {
	
	@Autowired
	private CourseTableService courseTableService;
	
	@Operation(summary = "添加选课")
	@PostMapping("/{courseId}")
	public XcChooseCourse addChooseCourse(@PathVariable("courseId") Long courseId) {
		return courseTableService.addChooseCourse(courseId);
	}
	
	@Operation(summary = "查询当前用户指定课程的学习资格")
	@PostMapping("/learnstatus/{courseId}")
	public XcCourseTablesDto getLearningStatus(@PathVariable("courseId") Long courseId) {
		return courseTableService.getLearningStatus(courseId);
	}
	
	@Operation(summary = "获取我的课程表")
	@PostMapping("/mycoursetable")
	public PageResult<XcCourseTables> getLearningStatus(MyCourseTablesQueryParams params) {
		return courseTableService.myCourseTables(params);
	}
	
}
