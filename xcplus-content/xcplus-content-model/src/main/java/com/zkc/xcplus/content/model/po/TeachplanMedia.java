package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Schema
@Data
@TableName(value = "teachplan_media")
public class TeachplanMedia implements Serializable {
	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(description = "主键")
	private Long id;
	
	/**
	 * 媒资文件id
	 */
	@TableField(value = "media_id")
	@Schema(description = "媒资文件id")
	private String mediaId;
	
	/**
	 * 课程计划标识
	 */
	@TableField(value = "teachplan_id")
	@Schema(description = "课程计划标识")
	private Long teachplanId;
	
	/**
	 * 课程标识
	 */
	@TableField(value = "course_id")
	@Schema(description = "课程标识")
	private Long courseId;
	
	/**
	 * 媒资文件原始名称
	 */
	@TableField(value = "media_fileName")
	@Schema(description = "媒资文件原始名称")
	private String mediaFilename;
	
	/**
	 * 创建时间
	 */
	@TableField(value = "create_date")
	@Schema(description = "创建时间")
	private LocalDateTime createDate;
	
	/**
	 * 创建人
	 */
	@TableField(value = "create_people")
	@Schema(description = "创建人")
	private String createPeople;
	
	/**
	 * 修改人
	 */
	@TableField(value = "change_people")
	@Schema(description = "修改人")
	private String changePeople;
	
	private static final long serialVersionUID = 1L;
}