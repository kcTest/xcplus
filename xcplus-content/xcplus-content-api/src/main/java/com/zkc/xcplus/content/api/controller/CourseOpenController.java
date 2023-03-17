package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.content.model.dto.CoursePreviewDto;
import com.zkc.xcplus.content.service.CoursePublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CourseOpenController", description = "公开接口 用于获取课程信息")
@RestController
@RequestMapping("/open/course")
public class CourseOpenController {
	
	@Autowired
	private CoursePublishService coursePublishService;
	
	@Operation(summary = "根据课程id查询课程信息")
	@GetMapping("/whole/{courseId}")
	public CoursePreviewDto preview(@PathVariable("courseId") Long courseId) {
		return coursePublishService.getCoursePreviewInfo(courseId);
	}
	
}
