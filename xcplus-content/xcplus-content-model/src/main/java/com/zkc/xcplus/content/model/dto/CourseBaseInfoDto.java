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
	@Schema(description = "收费规则，对应数据字典")
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
	
	/**
	 * 大分类名称
	 */
	private String mtName;
	/**
	 * 小分类名称
	 */
	private String stName;
}
