package com.zkc.xcplus.content.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zkc.xcplus.content.model.po.CourseBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseBaseInfoDto extends CourseBase {
	
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
	
	/**
	 * 大分类名称
	 */
	private String mtName;
	/**
	 * 小分类名称
	 */
	private String stName;
}
