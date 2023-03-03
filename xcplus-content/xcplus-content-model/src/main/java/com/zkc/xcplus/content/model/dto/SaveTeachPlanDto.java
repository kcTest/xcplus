package com.zkc.xcplus.content.model.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString
public class SaveTeachPlanDto {
	
	/**
	 * 课程计划ID
	 */
	private Long id;
	
	/**
	 * 课程计划名称
	 */
	private String pname;
	
	/**
	 * 课程计划父级id
	 */
	private Long parentid;
	
	/**
	 * 层级 1\2\3
	 */
	private Short grade;
	
	/**
	 * 课程类型: 1视频 2文档
	 */
	private String mediaType;
	
	/**
	 * 开始直播时间
	 */
	private LocalDateTime startTime;
	
	/**
	 * 直播结束时间
	 */
	private LocalDateTime endTime;
	
	/**
	 * 课程ID
	 */
	private Long courseId;
	
	
	/**
	 * 课程发布标识
	 */
	private Long coursePubId;
	
	/**
	 * 是否支持预览
	 */
	private String isPreview;
	
}
