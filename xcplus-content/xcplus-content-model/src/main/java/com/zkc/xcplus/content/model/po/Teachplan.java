package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 课程计划
 */
@Schema(description = "课程计划")
@Data
@TableName(value = "teachplan")
public class Teachplan implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(description = "主键")
	private Long id;
	
	/**
	 * 课程计划名称
	 */
	@TableField(value = "pname")
	@Schema(description = "课程计划名称")
	private String pname;
	
	/**
	 * 课程计划父级Id
	 */
	@TableField(value = "parentid")
	@Schema(description = "课程计划父级Id")
	private Long parentid;
	
	/**
	 * 层级，分为1、2、3级
	 */
	@TableField(value = "grade")
	@Schema(description = "层级，分为1、2、3级")
	private Short grade;
	
	/**
	 * 课程类型:1视频、2文档
	 */
	@TableField(value = "media_type")
	@Schema(description = "课程类型:1视频、2文档")
	private String mediaType;
	
	/**
	 * 开始直播时间
	 */
	@TableField(value = "start_time")
	@Schema(description = "开始直播时间")
	private LocalDateTime startTime;
	
	/**
	 * 直播结束时间
	 */
	@TableField(value = "end_time")
	@Schema(description = "直播结束时间")
	private LocalDateTime endTime;
	
	/**
	 * 章节及课程时介绍
	 */
	@TableField(value = "description")
	@Schema(description = "章节及课程时介绍")
	private String description;
	
	/**
	 * 时长，单位时:分:秒
	 */
	@TableField(value = "timelength")
	@Schema(description = "时长，单位时:分:秒")
	private String timelength;
	
	/**
	 * 排序字段
	 */
	@TableField(value = "orderby")
	@Schema(description = "排序字段")
	private Integer orderby;
	
	/**
	 * 课程标识
	 */
	@TableField(value = "course_id")
	@Schema(description = "课程标识")
	private Long courseId;
	
	/**
	 * 课程发布标识
	 */
	@TableField(value = "course_pub_id")
	@Schema(description = "课程发布标识")
	private Long coursePubId;
	
	/**
	 * 状态（1正常  0删除）
	 */
	@TableField(value = "`status`")
	@Schema(description = "状态（1正常  0删除）")
	private Integer status;
	
	/**
	 * 是否支持试学或预览（试看）
	 */
	@TableField(value = "is_preview")
	@Schema(description = "是否支持试学或预览（试看）")
	private String isPreview;
	
	/**
	 * 创建时间
	 */
	@TableField(value = "create_date")
	@Schema(description = "创建时间")
	private LocalDateTime createDate;
	
	/**
	 * 修改时间
	 */
	@TableField(value = "change_date")
	@Schema(description = "修改时间")
	private LocalDateTime changeDate;
	
	private static final long serialVersionUID = 1L;
}