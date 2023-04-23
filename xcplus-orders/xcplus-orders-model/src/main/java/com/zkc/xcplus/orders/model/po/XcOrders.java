package com.zkc.xcplus.orders.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema
@Data
@TableName(value = "xc_orders")
public class XcOrders implements Serializable {
	/**
	 * 订单号
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@Schema(description = "订单号")
	private Long id;
	
	/**
	 * 总价
	 */
	@TableField(value = "total_price")
	@Schema(description = "总价")
	private Double totalPrice;
	
	/**
	 * 创建时间
	 */
	@TableField(value = "create_date")
	@Schema(description = "创建时间")
	private LocalDateTime createDate;
	
	/**
	 * 交易状态
	 */
	@TableField(value = "`status`")
	@Schema(description = "交易状态")
	private String status;
	
	/**
	 * 用户id
	 */
	@TableField(value = "user_id")
	@Schema(description = "用户id")
	private String userId;
	
	/**
	 * 订单类型
	 */
	@TableField(value = "order_type")
	@Schema(description = "订单类型")
	private String orderType;
	
	/**
	 * 订单名称
	 */
	@TableField(value = "order_name")
	@Schema(description = "订单名称")
	private String orderName;
	
	/**
	 * 订单描述
	 */
	@TableField(value = "order_descrip")
	@Schema(description = "订单描述")
	private String orderDescrip;
	
	/**
	 * 订单明细json
	 */
	@TableField(value = "order_detail")
	@Schema(description = "订单明细json")
	private String orderDetail;
	
	/**
	 * 外部系统业务id
	 */
	@TableField(value = "out_business_id")
	@Schema(description = "外部系统业务id")
	private String outBusinessId;
	
	@Serial
	private static final long serialVersionUID = 1L;
}