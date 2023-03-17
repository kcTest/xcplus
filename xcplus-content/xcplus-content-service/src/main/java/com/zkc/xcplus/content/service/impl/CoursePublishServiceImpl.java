package com.zkc.xcplus.content.service.impl;

import com.zkc.xcplus.content.model.dto.CourseBaseInfoDto;
import com.zkc.xcplus.content.model.dto.CoursePreviewDto;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import com.zkc.xcplus.content.service.CoursePublishService;
import com.zkc.xcplus.content.service.TeachPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursePublishServiceImpl implements CoursePublishService {
	
	@Autowired
	private CourseBaseInfoService courseBaseInfoService;
	
	@Autowired
	private TeachPlanService teachPlanService;
	
	@Override
	public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
		CoursePreviewDto ret = new CoursePreviewDto();
		
		//基本信息
		CourseBaseInfoDto baseInfo = courseBaseInfoService.get(courseId);
		//课程计划
		List<TeachPlanDto> plan = teachPlanService.getTreeNodes(courseId);
		
		ret.setCourseBaseInfo(baseInfo);
		ret.setTeachPlans(plan);
		return ret;
	}
}
