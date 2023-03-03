package com.zkc.xcplus.content.api.controller;

import com.zkc.xcplus.content.model.dto.SaveTeachPlanDto;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.service.TeachPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TeachPlanController", description = "课程计划相关")
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
	
	@Operation(description = "添加/修改课程计划")
	@PostMapping("/save")
	public void saveTeachPlan(@RequestBody SaveTeachPlanDto dto) {
		teachPlanService.saveTeachPlan(dto);
	}
}
