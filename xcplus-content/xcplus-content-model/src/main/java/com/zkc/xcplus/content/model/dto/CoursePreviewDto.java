package com.zkc.xcplus.content.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CoursePreviewDto {
	
	/**
	 * 课程基本信息 营销信息
	 */
	CourseBaseInfoDto courseBaseInfo;
	
	/**
	 * 课程计划信息
	 */
	List<TeachPlanDto> teachPlans;
	
}
