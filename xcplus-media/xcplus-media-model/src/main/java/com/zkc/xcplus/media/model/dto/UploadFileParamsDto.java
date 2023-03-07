package com.zkc.xcplus.media.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UploadFileParamsDto {
	
	/**
	 * 文件名称
	 */
	@Schema(description = "文件名称")
	private String filename;
	
	/**
	 * 内容类型
	 */
	@Schema(description = "内容类型")
	private String contentType;
	
	/**
	 * 文件类型（图片、文档，视频）
	 */
	@Schema(description = "文件类型（图片、文档，视频）")
	private String fileType;
	
	/**
	 * 文件大小
	 */
	@Schema(description = "文件大小")
	private Long fileSize;
	
	/**
	 * 标签
	 */
	@Schema(description = "标签")
	private String tags;
	
	/**
	 * 上传人
	 */
	@Schema(description = "上传人")
	private String username;
	
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;
	
	
}
