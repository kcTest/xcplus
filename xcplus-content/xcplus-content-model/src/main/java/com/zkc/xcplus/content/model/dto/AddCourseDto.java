package com.zkc.xcplus.content.model.dto;

import com.zkc.xcplus.base.exception.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "新增课程信息")
public class AddCourseDto {
	
	/**
	 * 课程名称
	 */
	@NotEmpty(message = "新增课程名称不能为空", groups = {ValidationGroups.Insert.class})
	@NotEmpty(message = "课程名称不能为空", groups = {ValidationGroups.Update.class})
	@Schema(description = "课程名称", requiredMode = Schema.RequiredMode.REQUIRED)
	private String name;
	
	/**
	 * 适用人群
	 */
	@NotEmpty(message = "适用人群不能为空")
	@Size(message = "适用人群内容过少", min = 10)
	@Schema(description = "适用人群", requiredMode = Schema.RequiredMode.REQUIRED)
	private String users;
	
	/**
	 * 课程标签
	 */
	@Schema(description = "课程标签")
	private String tags;
	
	/**
	 * 大分类
	 */
	@NotEmpty(message = "课程分类不能为空")
	@Schema(description = "大分类", requiredMode = Schema.RequiredMode.REQUIRED)
	private String mt;
	
	/**
	 * 小分类
	 */
	@NotEmpty(message = "课程分类不能为空")
	@Schema(description = "小分类", requiredMode = Schema.RequiredMode.REQUIRED)
	private String st;
	
	/**
	 * 课程等级
	 */
	@NotEmpty(message = "课程分类不能为空")
	@Schema(description = "课程等级", requiredMode = Schema.RequiredMode.REQUIRED)
	private String grade;
	
	/**
	 * 教育模式(common普通，record 录播，live直播等）
	 */
	@NotEmpty(message = "教育模式不能为空")
	@Schema(description = "教育模式(common普通，record 录播，live直播等）", requiredMode = Schema.RequiredMode.REQUIRED)
	private String teachmode;
	
	/**
	 * 课程介绍
	 */
	@Schema(description = "课程介绍")
	private String description;
	
	/**
	 * 课程图片
	 */
	@Schema(description = "课程图片", requiredMode = Schema.RequiredMode.REQUIRED)
	private String pic;
	
	/**
	 * 收费规则，对应数据字典
	 */
	@NotEmpty(message = "收费规则不能为空")
	@Schema(description = "收费规则，对应数据字典", requiredMode = Schema.RequiredMode.REQUIRED)
	private String charge;
	
	/**
	 * 现价
	 */
	@Schema(description = "现价")
	private Float price;
	
	/**
	 * 原价
	 */
	@Schema(description = "原价")
	private Float originalPrice;
	
	/**
	 * 咨询qq
	 */
	@Schema(description = "咨询qq")
	private String qq;
	
	/**
	 * 微信
	 */
	@Schema(description = "微信")
	private String wechat;
	
	/**
	 * 电话
	 */
	@Schema(description = "电话")
	private String phone;
	
	/**
	 * 有效期天数
	 */
	@Schema(description = "有效期天数")
	private Integer validDays;
}
