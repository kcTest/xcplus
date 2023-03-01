package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.service.TeachPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Schema(description = "课程计划相关")
@RestController
@RequestMapping("/teachplan")
public class TeachPlanController {
	
	@Autowired
	private TeachPlanService teachPlanService;
	
	@Operation(description = "获取课程计划")
	@GetMapping("/{courseId}/tree-nodes")
	public List<TeachPlanDto> getTreeNodes(@PathVariable Long courseId) {
		return teachPlanService.getTreeNodes(courseId);
	}
}
