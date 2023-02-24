package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.content.model.dto.CourseCategoryTreeDto;
import com.zkc.xcplus.content.service.CourseCategoryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Schema(description = "课程分类信息")
@RestController
@RequestMapping("/coursecategory")
public class CourseCategoryController {
	
	@Autowired
	private CourseCategoryService courseCategoryService;
	
	@GetMapping("/treeNodes")
	public List<CourseCategoryTreeDto> getTreeNodes(@Parameter(description = "父节点id", required = true) String parentId) {
		return courseCategoryService.getTreeNodes(parentId);
	}
	
}
