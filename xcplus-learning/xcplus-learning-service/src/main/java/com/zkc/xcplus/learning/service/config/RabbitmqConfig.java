package com.zkc.xcplus.learning.service.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
	
	/**
	 * 交换机名称
	 */
	public static final String PAY_NOTIFY_EXCHANGE_FANOUT = "pay_notify_exchange_fanout";
	/**
	 * 支付结果通知消息类型
	 */
	public static final String MESSAGE_TYPE = "pay_result_notify";
	/**
	 * 支付通知队列名称
	 */
	public static final String PAY_NOTIFY_QUEUE = "pay_notify_queue";
	
	/**
	 * 声明交换机 持久化
	 */
	@Bean(PAY_NOTIFY_EXCHANGE_FANOUT)
	public FanoutExchange payNotifyExchangeFanout() {
		return new FanoutExchange(PAY_NOTIFY_EXCHANGE_FANOUT, true, false);
	}
	
	/**
	 * 支付通知队列，且持久化
	 */
	@Bean(PAY_NOTIFY_QUEUE)
	public Queue payNotifyQueue() {
		return QueueBuilder.durable(PAY_NOTIFY_QUEUE).build();
	}
	
	/**
	 * 绑定交换机和队列
	 */
	@Bean
	public Binding binding(@Qualifier(PAY_NOTIFY_EXCHANGE_FANOUT) FanoutExchange fanoutExchange, @Qualifier(PAY_NOTIFY_QUEUE) Queue queue) {
		return BindingBuilder.bind(queue).to(fanoutExchange);
	}
	
}
