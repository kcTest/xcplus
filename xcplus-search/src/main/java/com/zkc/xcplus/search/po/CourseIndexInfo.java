package com.zkc.xcplus.search.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 课程索引信息
 */
@Data
@ToString
public class CourseIndexInfo implements Serializable {
	/**
	 * 主键
	 */
	private Long id;
	
	/**
	 * 机构ID
	 */
	@Schema(description = "机构ID")
	private Long companyId;
	
	/**
	 * 公司名称
	 */
	@Schema(description = "公司名称")
	private String companyName;
	
	/**
	 * 课程名称
	 */
	@Schema(description = "课程名称")
	private String name;
	
	/**
	 * 适用人群
	 */
	@Schema(description = "适用人群")
	private String users;
	
	/**
	 * 标签
	 */
	@Schema(description = "标签")
	private String tags;
	
	/**
	 * 创建人
	 */
	@Schema(description = "创建人")
	private String username;
	
	/**
	 * 大分类
	 */
	@Schema(description = "大分类")
	private String mt;
	
	/**
	 * 大分类名称
	 */
	@Schema(description = "大分类名称")
	private String mtName;
	
	/**
	 * 小分类
	 */
	@Schema(description = "小分类")
	private String st;
	
	/**
	 * 小分类名称
	 */
	@Schema(description = "小分类名称")
	private String stName;
	
	/**
	 * 课程等级
	 */
	@Schema(description = "课程等级")
	private String grade;
	
	/**
	 * 教育模式
	 */
	@Schema(description = "教育模式")
	private String teachmode;
	
	/**
	 * 课程图片
	 */
	@Schema(description = "课程图片")
	private String pic;
	
	/**
	 * 课程介绍
	 */
	@Schema(description = "课程介绍")
	private String description;
	
	/**
	 * 发布时间
	 */
	@Schema(description = "发布时间")
	private String createDate;
	
	
	/**
	 * 发布状态
	 */
	@Schema(description = "发布状态")
	private String status;
	
	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;
	
	/**
	 * 收费规则，对应数据字典--203
	 */
	@Schema(description = "收费规则，对应数据字典--203")
	private String charge;
	
	/**
	 * 现价
	 */
	@Schema(description = "现价")
	private Double price;
	
	/**
	 * 原价
	 */
	@Schema(description = "原价")
	private Double originalPrice;
	
	/**
	 * 课程有效期天数
	 */
	@Schema(description = "课程有效期天数")
	private Integer validDays;
	
	private static final long serialVersionUID = 1L;
}
