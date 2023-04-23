package com.zkc.xcplus.orders.model.dto;

import lombok.Data;

/**
 * 支付结果数据 用于接收支付结果通知处理逻辑
 */
@Data
public class PayStatusDto {
	
	/**
	 * 商户订单号
	 */
	private String outTradeNo;
	/**
	 * 支付宝交易号
	 */
	private String tradeNo;
	/**
	 * 交易状态
	 */
	private String tradeStatus;
	/**
	 * 应用id
	 */
	private String appId;
	/**
	 * 总金额
	 */
	private String totalAmount;
}
