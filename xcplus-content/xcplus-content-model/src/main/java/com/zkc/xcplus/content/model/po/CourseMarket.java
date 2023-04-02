package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程营销信息
 */
@Schema(description = "课程营销信息")
@Data
@TableName(value = "course_market")
public class CourseMarket implements Serializable {
	/**
	 * 主键，课程id
	 */
	@TableId(value = "id", type = IdType.NONE)
	@Schema(description = "主键，课程id")
	private Long id;
	
	/**
	 * 收费规则，对应数据字典
	 */
	@TableField(value = "charge")
	@Schema(description = "收费规则，对应数据字典")
	private String charge;
	
	/**
	 * 现价 类型暂时与数据库保持一致
	 */
	@TableField(value = "price")
	@Schema(description = "现价")
	private Double price;
	
	/**
	 * 原价 
	 */
	@TableField(value = "original_price")
	@Schema(description = "原价")
	private Double originalPrice;
	
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
	
	private static final long serialVersionUID = 1L;
}