package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 课程基本信息
 */
@Schema(description = "课程基本信息")
@Data
@TableName(value = "course_base")
public class CourseBase implements Serializable {
	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(description = "主键")
	private Long id;
	
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
	 * 课程名称
	 */
	@TableField(value = "`name`")
	@Schema(description = "课程名称")
	private String name;
	
	/**
	 * 适用人群
	 */
	@TableField(value = "users")
	@Schema(description = "适用人群")
	private String users;
	
	/**
	 * 课程标签
	 */
	@TableField(value = "tags")
	@Schema(description = "课程标签")
	private String tags;
	
	/**
	 * 大分类
	 */
	@TableField(value = "mt")
	@Schema(description = "大分类")
	private String mt;
	
	/**
	 * 小分类
	 */
	@TableField(value = "st")
	@Schema(description = "小分类")
	private String st;
	
	/**
	 * 课程等级
	 */
	@TableField(value = "grade")
	@Schema(description = "课程等级")
	private String grade;
	
	/**
	 * 教育模式(common普通，record 录播，live直播等）
	 */
	@TableField(value = "teachmode")
	@Schema(description = "教育模式(common普通，record 录播，live直播等）")
	private String teachmode;
	
	/**
	 * 课程介绍
	 */
	@TableField(value = "description")
	@Schema(description = "课程介绍")
	private String description;
	
	/**
	 * 课程图片
	 */
	@TableField(value = "pic")
	@Schema(description = "课程图片")
	private String pic;
	
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
	
	/**
	 * 创建人
	 */
	@TableField(value = "create_people")
	@Schema(description = "创建人")
	private String createPeople;
	
	/**
	 * 更新人
	 */
	@TableField(value = "change_people")
	@Schema(description = "更新人")
	private String changePeople;
	
	/**
	 * 审核状态
	 */
	@TableField(value = "audit_status")
	@Schema(description = "审核状态")
	private String auditStatus;
	
	/**
	 * 课程发布状态 未发布  已发布 下线
	 */
	@TableField(value = "`status`")
	@Schema(description = "课程发布状态 未发布  已发布 下线")
	private String status;
	
	private static final long serialVersionUID = 1L;
}