package com.zkc.xcplus.orders.model.dto;

import lombok.Data;

/**
 * 创建商品订单
 */
@Data
public class AddOrderDto {
	
	/**
	 * 总价
	 */
	private Double totalPrice;
	
	/**
	 * 订单类型
	 */
	private String orderType;
	
	/**
	 * 订单名称
	 */
	private String orderName;
	
	/**
	 * 订单描述
	 */
	private String orderDesc;
	
	/**
	 * 订单明细json
	 */
	private String orderDetail;
	
	/**
	 * 外部系统业务id
	 */
	private String outBusinessId;
	
}
