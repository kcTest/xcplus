package com.zkc.xcplus.content.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCourseDto extends AddCourseDto {
	
	/**
	 * 课程ID
	 */
	@Schema(description = "课程ID", required = true)
	@NotNull(message = "课程id不能为空")
	private Long id;
}
