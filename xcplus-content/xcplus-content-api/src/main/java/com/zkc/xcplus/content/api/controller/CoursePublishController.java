package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.content.model.dto.CoursePreviewDto;
import com.zkc.xcplus.content.model.po.CoursePublish;
import com.zkc.xcplus.content.service.CoursePublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Tag(name = "CoursePublishController", description = "课程发布")
@Controller
@RequestMapping("/coursepublish")
public class CoursePublishController {
	
	@Autowired
	private CoursePublishService coursePublishService;
	
	@Operation(summary = "课程发布预览")
	@GetMapping("/preview/{courseId}")
	public ModelAndView preview(@PathVariable("courseId") Long courseId) {
		ModelAndView mv = new ModelAndView();
		CoursePreviewDto previewInfo = coursePublishService.getCoursePreviewInfo(courseId);
		mv.setViewName("course_template");
		//${model.*}
		mv.addObject("model", previewInfo);
		return mv;
	}
	
	@Operation(summary = "课程提交审核")
	@PostMapping("/submitforreview/{courseId}")
	@ResponseBody
	public void submitForReview(@PathVariable("courseId") Long courseId) {
		//TODO 获取机构id
		Long companyId = 1L;
		coursePublishService.submitForReview(companyId, courseId);
	}
	
	@Operation(summary = "课程发布")
	@ResponseBody
	@PostMapping("/{courseId}")
	public void coursePublish(@PathVariable("courseId") Long courseId) {
		//TODO 获取机构id
		Long companyId = 1L;
		coursePublishService.coursePublish(companyId, courseId);
	}
	
	@Operation(summary = "获取课程发布信息")
	@ResponseBody
	@GetMapping("/info/{courseId}")
	public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
		return coursePublishService.getCoursePublishCache(courseId);
	}
	
}
