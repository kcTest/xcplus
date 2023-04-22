package com.zkc.xcplus.learning.api.controller;

import com.zkc.xcplus.base.model.RestResponse;
import com.zkc.xcplus.learning.service.LearningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "LearningController", description = "在线学习相关接口")
@RestController
@RequestMapping("/open/learn")
public class LearningController {
	
	@Autowired
	private LearningService learningService;
	
	@Operation(summary = "添加选课")
	@PostMapping("/getvideo/{courseId}/{teachPlanId}/{mediaId}")
	public RestResponse<String> addChooseCourse(@PathVariable("courseId") Long courseId, @PathVariable("teachPlanId") Long teachPlanId, @PathVariable("mediaId") String mediaId) {
		return learningService.getVideo(courseId, teachPlanId, mediaId);
	}
	
}
