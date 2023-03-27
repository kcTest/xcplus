package com.zkc.xcplus.message.service;

import com.zkc.xcplus.message.model.MqMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public abstract class MessageProcessAbstract {
	
	@Autowired
	private MqMessageService mqMessageService;
	
	/**
	 * 抽象任务处理方法
	 *
	 * @param shardIdx    当前分片的索引
	 * @param shardTotal  分片总数
	 * @param messageType 任务类型
	 * @param count       每次处理任务个数
	 * @param timeout     超时时间
	 */
	public void process(int shardIdx, int shardTotal, String messageType, int count, long timeout) {
		try {
			
			//扫描消息表获取任务
			List<MqMessage> messageList = mqMessageService.getMessageList(shardIdx, shardTotal, messageType, count);
			int size = messageList.size();
			log.debug("查询待处理消息{}条", size);
			if (size <= 0) {
				return;
			}
			
			//创建线程池
			ExecutorService threadPool = Executors.newFixedThreadPool(size);
			//计数器
			CountDownLatch countDownLatch = new CountDownLatch(size);
			messageList.forEach(msg -> {
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						log.debug("开始任务:{}", msg);
						//处理任务
						try {
							boolean result = execute(msg);
							if (result) {
								log.debug("任务执行成功:{}", msg);
								//更新任务状态,删除消息表记录,添加到历史记录表
								boolean completed = mqMessageService.completed(msg.getId());
								if (completed) {
									log.debug("任务执行成功:{}", msg);
								} else {
									log.debug("任务执行失败:{}", msg);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							log.debug("任务执行异常:{},任务:{}", e.getMessage(), msg);
						} finally {
							countDownLatch.countDown();
						}
						log.debug("结束任务:{}", msg);
					}
				});
			});
			
			countDownLatch.await(timeout, TimeUnit.SECONDS);
			log.debug("任务处理结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行任务的具体逻辑
	 *
	 * @param msg 消息内容
	 * @return 执行是否成功
	 */
	protected abstract boolean execute(MqMessage msg);
}
