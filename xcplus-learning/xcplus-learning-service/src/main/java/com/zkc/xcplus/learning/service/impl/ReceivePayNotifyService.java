package com.zkc.xcplus.learning.service.impl;

import com.alibaba.fastjson.JSON;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.learning.service.CourseTableService;
import com.zkc.xcplus.learning.service.config.RabbitmqConfig;
import com.zkc.xcplus.message.model.MqMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivePayNotifyService {
	
	@Autowired
	private CourseTableService courseTableService;
	
	@RabbitListener(queues = RabbitmqConfig.PAY_NOTIFY_QUEUE)
	public void receive(Message message) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//解析消息
		byte[] body = message.getBody();
		String msgStr = new String(body);
		MqMessage mqMessage = JSON.parseObject(msgStr, MqMessage.class);
		//选课记录id
		String chooseCourseId = mqMessage.getBusinessKey1();
		//订单类型[{"code":"60201","desc":"购买课程"},{"code":"60202","desc":"学习资料"}]
		String orderType = mqMessage.getBusinessKey2();
		if ("60201".equals(orderType)) {
			boolean b = courseTableService.saveChooseCourseSuccess(chooseCourseId);
			if (!b) {
				CustomException.cast("选课记录成功状态更新失败");
			}
		}
	}
}
