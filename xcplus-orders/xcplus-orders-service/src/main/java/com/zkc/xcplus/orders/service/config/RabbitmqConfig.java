package com.zkc.xcplus.orders.service.config;

import com.zkc.xcplus.message.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitmqConfig implements ApplicationContextAware {
	
	/**
	 * 交换机
	 */
	public static final String PAY_NOTIFY_EXCHANGE_FANOUT = "pay_notify_exchange_fanout";
	
	/**
	 * 支付通知消息类型
	 */
	public static final String MESSAGE_TYPE = "pay_result_notify";
	
	/**
	 * 支付通知队列
	 */
	public static final String PAY_NOTIFY_QUEUE = "pay_notify_queue";
	
	/**
	 * 声明交换机 持久化
	 */
	@Bean(PAY_NOTIFY_EXCHANGE_FANOUT)
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(PAY_NOTIFY_EXCHANGE_FANOUT, true, false);
	}
	
	/**
	 * 声明通知队列 持久化
	 */
	@Bean(PAY_NOTIFY_QUEUE)
	public Queue queue() {
		return QueueBuilder.durable(PAY_NOTIFY_QUEUE).build();
	}
	
	/**
	 * 交换机与队列绑定
	 */
	@Bean
	public Binding binding(@Qualifier(PAY_NOTIFY_EXCHANGE_FANOUT) FanoutExchange fanoutExchange,
						   @Qualifier(PAY_NOTIFY_QUEUE) Queue queue) {
		return BindingBuilder.bind(queue).to(fanoutExchange);
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
		MqMessageService mqMessageService = applicationContext.getBean(MqMessageService.class);
		
		rabbitTemplate.setReturnsCallback(ret -> log.info("消息发送失败，应答码：{}，原因：{}，交换机：{}，路由键：{}，消息：{}",
				ret.getReplyCode(), ret.getReplyText(), ret.getExchange(), ret.getRoutingKey(), ret.getMessage()));
	}
}
