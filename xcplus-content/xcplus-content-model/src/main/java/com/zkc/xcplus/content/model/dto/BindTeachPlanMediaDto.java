package com.zkc.xcplus.content.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * 绑定课程计划与媒资
 */
@Data
@ToString
public class BindTeachPlanMediaDto {
	
	@Schema(description = "媒体文件id", requiredMode = Schema.RequiredMode.REQUIRED)
	private String mediaId;
	
	@Schema(description = "媒体文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
	private String fileName;
	
	@Schema(description = "课程计划标识", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long teachPlanId;
	
}
