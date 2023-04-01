package com.zkc.xcplus.search.controller;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.search.dto.CourseIdxSearchParamDto;
import com.zkc.xcplus.search.dto.IdxSearchResultDto;
import com.zkc.xcplus.search.po.CourseIndexInfo;
import com.zkc.xcplus.search.service.CourseIdxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CourseIdxController", description = "课程信息索引管理")
@RestController
@RequestMapping("/courseidx")
public class CourseIdxController {
	
	@Autowired
	private CourseIdxService courseIdxSearchService;
	
	@Operation(summary = "课程信息索引添加")
	@PostMapping("/add")
	public boolean add(@RequestBody CourseIndexInfo courseIndexInfo) {
		return courseIdxSearchService.addCourseIdx(courseIndexInfo);
	}
	
	@Operation(summary = "搜索课程信息索引")
	@GetMapping("/list")
	public IdxSearchResultDto<CourseIndexInfo> list(PageParams pageParams, CourseIdxSearchParamDto dto) {
		return courseIdxSearchService.queryCoursePublishIndex(pageParams, dto);
	}
	
}
