package com.zkc.xcplus.search.controller;

import com.zkc.xcplus.search.service.CourseSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CourseSearchController", description = "课程搜索")
@RestController
@RequestMapping("/course")
public class CourseSearchController {
	
	@Autowired
	private CourseSearchService courseSearchService;
	
}
