package com.zkc.xcplus.content.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "新增课程信息")
public class AddCourseDto {
	
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
	 * 收费规则，对应数据字典
	 */
	@TableField(value = "charge")
	@Schema(description = "收费规则，对应数据字典")
	private String charge;
	
	/**
	 * 现价
	 */
	@TableField(value = "price")
	@Schema(description = "现价")
	private Float price;
	
	/**
	 * 原价
	 */
	@TableField(value = "original_price")
	@Schema(description = "原价")
	private Float originalPrice;
	
	/**
	 * 咨询qq
	 */
	@TableField(value = "qq")
	@Schema(description = "咨询qq")
	private String qq;
	
	/**
	 * 微信
	 */
	@TableField(value = "wechat")
	@Schema(description = "微信")
	private String wechat;
	
	/**
	 * 电话
	 */
	@TableField(value = "phone")
	@Schema(description = "电话")
	private String phone;
	
	/**
	 * 有效期天数
	 */
	@TableField(value = "valid_days")
	@Schema(description = "有效期天数")
	private Integer validDays;
}
