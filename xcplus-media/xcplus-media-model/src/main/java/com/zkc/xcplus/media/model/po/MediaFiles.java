package com.zkc.xcplus.media.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 媒资信息
 */
@Schema(description = "媒资信息")
@Data
@TableName(value = "media_files")
public class MediaFiles implements Serializable {
	/**
	 * 文件id,md5值
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(description = "文件id,md5值")
	private String id;
	
	/**
	 * 机构ID
	 */
	@TableField(value = "company_id")
	@Schema(description = "机构ID")
	private Long companyId;
	
	/**
	 * 机构名称
	 */
	@TableField(value = "company_name")
	@Schema(description = "机构名称")
	private String companyName;
	
	/**
	 * 文件名称
	 */
	@TableField(value = "filename")
	@Schema(description = "文件名称")
	private String filename;
	
	/**
	 * 文件类型（图片、文档，视频）
	 */
	@TableField(value = "file_type")
	@Schema(description = "文件类型（图片、文档，视频）")
	private String fileType;
	
	/**
	 * 标签
	 */
	@TableField(value = "tags")
	@Schema(description = "标签")
	private String tags;
	
	/**
	 * 存储目录
	 */
	@TableField(value = "bucket")
	@Schema(description = "存储目录")
	private String bucket;
	
	/**
	 * 存储路径
	 */
	@TableField(value = "file_path")
	@Schema(description = "存储路径")
	private String filePath;
	
	/**
	 * 文件id
	 */
	@TableField(value = "file_id")
	@Schema(description = "文件id")
	private String fileId;
	
	/**
	 * 媒资文件访问地址
	 */
	@TableField(value = "url")
	@Schema(description = "媒资文件访问地址")
	private String url;
	
	/**
	 * 上传人
	 */
	@TableField(value = "username")
	@Schema(description = "上传人")
	private String username;
	
	/**
	 * 上传时间
	 */
	@TableField(value = "create_date")
	@Schema(description = "上传时间")
	private LocalDateTime createDate;
	
	/**
	 * 修改时间
	 */
	@TableField(value = "change_date")
	@Schema(description = "修改时间")
	private LocalDateTime changeDate;
	
	/**
	 * 状态,1:正常，0:不展示
	 */
	@TableField(value = "`status`")
	@Schema(description = "状态,1:正常，0:不展示")
	private String status;
	
	/**
	 * 备注
	 */
	@TableField(value = "remark")
	@Schema(description = "备注")
	private String remark;
	
	/**
	 * 审核状态
	 */
	@TableField(value = "audit_status")
	@Schema(description = "审核状态")
	private String auditStatus;
	
	/**
	 * 审核意见
	 */
	@TableField(value = "audit_mind")
	@Schema(description = "审核意见")
	private String auditMind;
	
	/**
	 * 文件大小
	 */
	@TableField(value = "file_size")
	@Schema(description = "文件大小")
	private Long fileSize;
	
	private static final long serialVersionUID = 1L;
}