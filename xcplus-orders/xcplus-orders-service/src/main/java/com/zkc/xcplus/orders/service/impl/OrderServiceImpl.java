package com.zkc.xcplus.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.base.utils.IdWorkerUtils;
import com.zkc.xcplus.base.utils.QRCodeUtil;
import com.zkc.xcplus.message.model.MqMessage;
import com.zkc.xcplus.message.service.MqMessageService;
import com.zkc.xcplus.orders.model.dto.AddOrderDto;
import com.zkc.xcplus.orders.model.dto.PayRecordDto;
import com.zkc.xcplus.orders.model.dto.PayStatusDto;
import com.zkc.xcplus.orders.model.po.XcOrders;
import com.zkc.xcplus.orders.model.po.XcOrdersGoods;
import com.zkc.xcplus.orders.model.po.XcPayRecord;
import com.zkc.xcplus.orders.model.po.XcUser;
import com.zkc.xcplus.orders.service.OrderService;
import com.zkc.xcplus.orders.service.UserInfoService;
import com.zkc.xcplus.orders.service.config.MyAlipayConfig;
import com.zkc.xcplus.orders.service.config.RabbitmqConfig;
import com.zkc.xcplus.orders.service.dao.XcOrdersGoodsMapper;
import com.zkc.xcplus.orders.service.dao.XcOrdersMapper;
import com.zkc.xcplus.orders.service.dao.XcPayRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private XcOrdersMapper ordersMapper;
	
	@Autowired
	private XcOrdersGoodsMapper ordersGoodsMapper;
	
	@Autowired
	private XcPayRecordMapper payRecordMapper;
	
	@Autowired
	@Lazy
	private OrderServiceImpl currentProxy;
	
	@Autowired
	private MqMessageService mqMessageService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${pay.qrcodeurl}")
	private String qrcodeUrl;
	
	@Value("${pay.alipay.APP_ID}")
	private String APP_ID;
	
	@Value("${pay.alipay.APP_PRIVATE_KEY}")
	private String APP_PRIVATE_KEY;
	
	@Value("${pay.alipay.APP_PUBLIC_KEY}")
	private String APP_PUBLIC_KEY;
	
	@Transactional
	@Override
	public PayRecordDto createOrder(AddOrderDto addOrderDto) {
		XcUser currentUser = userInfoService.getCurrentUser();
		String userid = currentUser.getId();
		
		//生成订单表 订单明细表
		XcOrders orders = saveOrders(userid, addOrderDto);
		
		//生成支付记录
		XcPayRecord payRecord = createPayRecord(orders.getId());
		Long payNo = payRecord.getPayNo();
		
		//生成二维码
		QRCodeUtil qrCodeUtil = new QRCodeUtil();
		//扫码支付二维码url
		String url = String.format(qrcodeUrl, payNo);
		//二维码图片
		String qrcode = qrCodeUtil.createQRCode(url, 200, 200);
		
		PayRecordDto payRecordDto = new PayRecordDto();
		BeanUtils.copyProperties(payRecord, payRecordDto);
		payRecordDto.setQrcode(qrcode);
		
		return payRecordDto;
	}
	
	private XcOrders saveOrders(String userid, AddOrderDto addOrderDto) {
		XcOrders orders = getOrderByBusinessId(addOrderDto.getOutBusinessId());
		//同一个选课记录只能有一个订单
		if (orders != null) {
			return orders;
		}
		
		//插入订单表
		orders = new XcOrders();
		//使用雪花算法生成订单号
		orders.setId(IdWorkerUtils.getInstance().nextId());
		orders.setTotalPrice(addOrderDto.getTotalPrice());
		orders.setCreateDate(LocalDateTime.now());
		//未支付
		orders.setStatus("600001");
		orders.setUserId(userid);
		//订单类型
		orders.setOrderType("60201");
		orders.setOrderName(addOrderDto.getOrderName());
		orders.setOrderDescrip(addOrderDto.getOrderDesc());
		orders.setOrderDetail(addOrderDto.getOrderDetail());
		orders.setOutBusinessId(addOrderDto.getOutBusinessId());
		int count = ordersMapper.insert(orders);
		if (count <= 0) {
			CustomException.cast("添加订单失败");
		}
		Long orderId = orders.getId();
		
		//插入订单明细
		String orderDetailJson = addOrderDto.getOrderDetail();
		List<XcOrdersGoods> ordersGoods = JSON.parseArray(orderDetailJson, XcOrdersGoods.class);
		for (XcOrdersGoods good : ordersGoods) {
			good.setOrderId(orderId);
			int count2 = ordersGoodsMapper.insert(good);
			if (count2 <= 0) {
				CustomException.cast("添加订单明细失败");
			}
		}
		
		return orders;
	}
	
	/**
	 * 根据订单数据生成支付记录
	 */
	private XcPayRecord createPayRecord(Long orderId) {
		XcOrders orders = ordersMapper.selectById(orderId);
		if (orders == null) {
			CustomException.cast("订单不存在");
		}
		String orderStatus = orders.getStatus();
		//支付已成功 不再生成支付记录
		if ("601002".equals(orderStatus)) {
			CustomException.cast("此订单已支付");
		}
		
		//添加支付记录
		XcPayRecord payRecord = new XcPayRecord();
		//商户订单号
		payRecord.setPayNo(IdWorkerUtils.getInstance().nextId());
		payRecord.setOrderId(orderId);
		payRecord.setOrderName(orders.getOrderName());
		payRecord.setTotalPrice(orders.getTotalPrice());
		payRecord.setCurrency("CNY");
		payRecord.setCreateDate(LocalDateTime.now());
		//未支付
		payRecord.setStatus("601001");
		payRecord.setUserId(orders.getUserId());
		int count = payRecordMapper.insert(payRecord);
		if (count <= 0) {
			CustomException.cast("插入支付记录失败");
		}
		return payRecord;
	}
	
	/**
	 * 根据业务id查询订单 业务id是选课记录表的主键
	 */
	private XcOrders getOrderByBusinessId(String outBusinessId) {
		LambdaQueryWrapper<XcOrders> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcOrders::getOutBusinessId, outBusinessId);
		return ordersMapper.selectOne(queryWrapper);
	}
	
	
	@Override
	public XcPayRecord getPayRecordByPayNo(String payNo) {
		LambdaQueryWrapper<XcPayRecord> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcPayRecord::getPayNo, payNo);
		return payRecordMapper.selectOne(queryWrapper);
	}
	
	@Override
	public PayRecordDto queryPayResult(String payNo) {
		//调用支付宝接口查询支付结果
		PayStatusDto payStatusDto = queryPayResultFromAlipay(payNo);
		//根据支付结果更新支付记录表和订单表的支付状态
		currentProxy.saveAlipayStatus(payStatusDto);
		
		//返回最新的支付记录
		PayRecordDto payRecordDto = new PayRecordDto();
		XcPayRecord payRecordByPayNo = getPayRecordByPayNo(payNo);
		BeanUtils.copyProperties(payRecordByPayNo, payRecordDto);
		
		return payRecordDto;
	}
	
	/**
	 * 请求支付宝查询支付结果
	 *
	 * @param payNo 支付记录id 商户订单号
	 * @return 支付结果
	 */
	private PayStatusDto queryPayResultFromAlipay(String payNo) {
		DefaultAlipayClient alipayClient = new DefaultAlipayClient(MyAlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, MyAlipayConfig.FORMAT,
				MyAlipayConfig.CHARSET, APP_PUBLIC_KEY, MyAlipayConfig.SIGNTYPE);
		
		AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("out_trade_no", payNo);
		queryRequest.setBizContent(jsonObject.toJSONString());
		
		String body = null;
		try {
			AlipayTradeQueryResponse queryResponse = alipayClient.execute(queryRequest);
			if (queryResponse.isSuccess()) {
				CustomException.cast("请求支付宝接口查询失败");
			}
			body = queryResponse.getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
			CustomException.cast("请求支付宝接口查询异常");
		}
		Map map = JSON.parseObject(body, Map.class);
		Map<String, Object> tradeQueryResponse = (Map) map.get("alipay_trade_query_reponse");
		
		//解析支付结果
		PayStatusDto payStatusDto = new PayStatusDto();
		payStatusDto.setOutTradeNo(payNo);
		//支付宝交易号
		payStatusDto.setTradeNo((String) tradeQueryResponse.get("trade_no"));
		//交易状态
		payStatusDto.setTradeStatus((String) tradeQueryResponse.get("trade_status"));
		payStatusDto.setAppId(APP_ID);
		//总金额
		payStatusDto.setTotalAmount((String) tradeQueryResponse.get("total_amount"));
		
		return payStatusDto;
	}
	
	/**
	 * 根据支付结果 更新支付记录及订单表支付状态
	 *
	 * @param payStatusDto 支付结果通知信息
	 */
	@Transactional
	@Override
	public void saveAlipayStatus(PayStatusDto payStatusDto) {
		//支付记录id
		String payNo = payStatusDto.getOutTradeNo();
		XcPayRecord payRecord = getPayRecordByPayNo(payNo);
		if (payRecord == null) {
			CustomException.cast("找不到相关支付记录");
		}
		//订单id
		Long orderId = payRecord.getOrderId();
		XcOrders orders = ordersMapper.selectById(orderId);
		if (orders == null) {
			CustomException.cast("找不到关联订单");
		}
		//支付状态
		String payStatus = payRecord.getStatus();
		//如果已经支付成功不再处理
		if ("601002".equals(payStatus)) {
			return;
		}
		//否则如果查询到支付成功 更新
		String tradeStatus = payStatusDto.getTradeStatus();
		if ("TRADE_SUCCESS".equals(tradeStatus)) {
			//更新支付记录支付状态
			payRecord.setStatus("601002");
			//更新支付宝订单号
			payRecord.setOutPayNo(payStatusDto.getTradeNo());
			//支付成功时间 //TODO
			payRecord.setPaySuccessTime(LocalDateTime.now());
			payRecordMapper.updateById(payRecord);
			
			//更新订单表支付状态
			orders.setStatus("600002");
			ordersMapper.updateById(orders);
			
			//消息写入数据库
			MqMessage message = mqMessageService.addMessage("payresult_notify", orders.getOutBusinessId(), orders.getOrderType(), null);
			//发送消息
			notifyPayResult(message);
		}
		
	}
	
	@Override
	public void notifyPayResult(MqMessage message) {
		//消息内容
		String jsonStr = JSON.toJSONString(message);
		//创建持久化消息
		Message mqMsg = MessageBuilder.withBody(jsonStr.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
		//消息id
		Long id = message.getId();
		//全局消息id
		CorrelationData correlationData = new CorrelationData(id.toString());
		//使用correlationData指定消息发送确认回调方法
		correlationData.getFuture().whenComplete((result, ex) -> {
			if (result.isAck()) {
				//消息成功发送到交换机
				log.debug("发送消息成功:{}", jsonStr);
				//将消息从数据库的消息表mq_message删除
				mqMessageService.completed(id);
			} else {
				log.debug("发送消息异常,消息内容：{}，异常：{}", jsonStr, ex.getMessage());
			}
		});
		//发送消息
		rabbitTemplate.convertAndSend(RabbitmqConfig.PAY_NOTIFY_EXCHANGE_FANOUT, "", mqMsg, correlationData);
	}
}
