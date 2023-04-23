package com.zkc.xcplus.orders.service;

import com.zkc.xcplus.message.model.MqMessage;
import com.zkc.xcplus.orders.model.dto.AddOrderDto;
import com.zkc.xcplus.orders.model.dto.PayRecordDto;
import com.zkc.xcplus.orders.model.dto.PayStatusDto;
import com.zkc.xcplus.orders.model.po.XcPayRecord;

/**
 * 订单相关接口
 */
public interface OrderService {
	
	/**
	 * 创建商品订单
	 *
	 * @param addOrderDto 订单信息
	 * @return 包含支付二维码的支付记录
	 */
	PayRecordDto createOrder(AddOrderDto addOrderDto);
	
	/**
	 * 查询支付记录
	 *
	 * @param payNo 系统交易订单号 支付记录id
	 * @return 支付记录
	 */
	XcPayRecord getPayRecordByPayNo(String payNo);
	
	/**
	 * 请求支付宝查询支付结果
	 *
	 * @param payNo 系统交易订单号 支付记录id
	 * @return 支付结果
	 */
	PayRecordDto queryPayResult(String payNo);
	
	/**
	 * 保存支付状态
	 *
	 * @param payStatusDto 支付结果通知信息
	 */
	void saveAlipayStatus(PayStatusDto payStatusDto);
	
	/**
	 * 发送支付通知结果
	 *
	 * @param message 支付消息
	 */
	void notifyPayResult(MqMessage message);
}
