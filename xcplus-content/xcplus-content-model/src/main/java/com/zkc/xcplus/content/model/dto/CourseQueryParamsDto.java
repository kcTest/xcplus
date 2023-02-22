package com.zkc.xcplus.content.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseQueryParamsDto {
	
	/**
	 * 审核状态
	 */
	@Schema(description = "审核状态")
	private String auditStatus;
	
	/**
	 * 课程名称
	 */
	@Schema(description = "课程名称")
	private String courseName;
	
	/**
	 * 发布状态
	 */
	@Schema(description = "发布状态")
	private String publishStatus;
	
}
