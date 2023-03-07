package com.zkc.xcplus.media.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryMediaFilesDto {
	
	@Schema(description = "文件名称")
	private String filename;
	
	@Schema(description = "文件类型")
	private String fileType;
	
	@Schema(description = "审核状态")
	private String auditStatus;
	
}
