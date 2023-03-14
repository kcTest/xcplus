package com.zkc.xcplus.media.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema
@Data
@TableName(value = "media_process_history")
public class MediaProcessHistory implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	
	/**
	 * 文件标识
	 */
	@TableField(value = "file_id")
	@Schema(description = "文件标识")
	private String fileId;
	
	/**
	 * 文件名称
	 */
	@TableField(value = "filename")
	@Schema(description = "文件名称")
	private String filename;
	
	/**
	 * 存储源
	 */
	@TableField(value = "bucket")
	@Schema(description = "存储源")
	private String bucket;
	
	/**
	 * 状态,1:未处理，2：处理成功  3处理失败
	 */
	@TableField(value = "`status`")
	@Schema(description = "状态,1:未处理，2：处理成功  3处理失败")
	private String status;
	
	/**
	 * 上传时间
	 */
	@TableField(value = "create_date")
	@Schema(description = "上传时间")
	private LocalDateTime createDate;
	
	/**
	 * 完成时间
	 */
	@TableField(value = "finish_date")
	@Schema(description = "完成时间")
	private LocalDateTime finishDate;
	
	/**
	 * 媒资文件访问地址
	 */
	@TableField(value = "url")
	@Schema(description = "媒资文件访问地址")
	private String url;
	
	/**
	 * 文件路径
	 */
	@TableField(value = "file_path")
	@Schema(description = "文件路径")
	private String filePath;
	
	/**
	 * 失败原因
	 */
	@TableField(value = "errormsg")
	@Schema(description = "失败原因")
	private String errormsg;
	
	/**
	 * 失败次数
	 */
	@TableField(value = "fail_count")
	@Schema(description = "失败次数")
	private Integer failCount;
	
	private static final long serialVersionUID = 1L;
}