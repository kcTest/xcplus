package com.zkc.xcplus.orders.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.orders.model.dto.AddOrderDto;
import com.zkc.xcplus.orders.model.dto.PayRecordDto;
import com.zkc.xcplus.orders.model.dto.PayStatusDto;
import com.zkc.xcplus.orders.model.po.XcPayRecord;
import com.zkc.xcplus.orders.service.OrderService;
import com.zkc.xcplus.orders.service.config.MyAlipayConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "OrderController", description = "订单支付接口")
@Slf4j
@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Value("${pay.alipay.APPID}")
	private String APPID;
	
	@Value("${pay.alipay.PRIVATE_KEY}")
	private String PRIVATE_KEY;
	
	@Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
	private String ALIPAY_PUBLIC_KEY;
	
	@Operation(summary = "生成支付二维码")
	@PostMapping("/generatepaycode")
	@ResponseBody
	public PayRecordDto generatePayCode(@RequestBody AddOrderDto dto) {
		//二维码链接指向/requestpay接口  电脑使用模拟器 安装沙箱支付宝扫码 可以识别本机地址
		return orderService.createOrder(dto);
	}
	
	@Operation(summary = "扫码下单接口")
	@GetMapping("/requestpay")
	public void requestPay(String payNo, HttpServletResponse response) throws AlipayApiException, IOException {
		XcPayRecord payRecord = orderService.getPayRecordByPayNo(payNo);
		if (payRecord == null) {
			CustomException.cast("支付记录不存在");
		}
		String payStatus = payRecord.getStatus();
		if ("601002".equals(payStatus)) {
			CustomException.cast("已支付,无需重复支付");
		}
		
		DefaultAlipayClient alipayClient = new DefaultAlipayClient(MyAlipayConfig.URL, APPID, PRIVATE_KEY, MyAlipayConfig.FORMAT
				, MyAlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY, MyAlipayConfig.SIGNTYPE);
		AlipayTradeWapPayRequest wapPayRequest = new AlipayTradeWapPayRequest();
		wapPayRequest.setNotifyUrl("");
		//填充业务参数
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no", payNo);
		bizContent.put("total_amount", payRecord.getTotalPrice());
		bizContent.put("subject", payRecord.getOrderName());
		bizContent.put("product_code", "QUICK_WAP_WAY");
		wapPayRequest.setBizContent(bizContent.toString());
		AlipayTradeWapPayResponse wapPayResponse = alipayClient.pageExecute(wapPayRequest);
		response.setContentType("text/html;charset" + MyAlipayConfig.CHARSET);
		if (wapPayResponse.isSuccess()) {
			String body = wapPayResponse.getBody();
			response.getWriter().write(body);
			response.getWriter().flush();
		} else {
			String body = wapPayResponse.getMsg();
			log.debug("下单接口调用失败,{}", body);
			response.getWriter().write(body);
			response.getWriter().flush();
		}
	}
	
	@Operation(summary = "查询支付结果")
	@GetMapping("/payresult")
	@ResponseBody
	public PayRecordDto payresult(String payNo) {
		//查询支付结果
		return orderService.queryPayResult(payNo);
	}
	
	//接收支付宝通知
	@PostMapping("/paynotify")
	public void paynotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
		//获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (String keyName : requestParams.keySet()) {
			String[] values = requestParams.get(keyName);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(keyName, valueStr);
		}
		boolean verifyResult = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, MyAlipayConfig.CHARSET, "RSA2");
		if (verifyResult) {//验证成功
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
			//交易金额
			String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
			if ("TRADE_SUCCESS".equals(trade_status)) {
				//更新支付记录表的支付状态为成功，订单表的状态为成功
				PayStatusDto payStatusDto = new PayStatusDto();
				payStatusDto.setTradeStatus(trade_status);
				payStatusDto.setTradeNo(trade_no);
				payStatusDto.setOutTradeNo(out_trade_no);
				payStatusDto.setTotalAmount(total_amount);
				payStatusDto.setAppId(APPID);
				orderService.saveAlipayStatus(payStatusDto);
			}
			response.getWriter().write("success");
		} else {//验证失败
			response.getWriter().write("fail");
		}
	}
	
}
