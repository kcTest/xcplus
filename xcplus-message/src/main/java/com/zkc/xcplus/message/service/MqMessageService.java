package com.zkc.xcplus.message.service;

import com.zkc.xcplus.message.model.MqMessage;

import java.util.List;

public interface MqMessageService {
	
	/**
	 * 向消息表写入数据
	 *
	 * @param messageType  消息类型代码
	 * @param businessKey1 关联业务信息
	 * @param businessKey2 关联业务信息
	 * @param businessKey3 关联业务信息
	 * @return 消息
	 */
	MqMessage addMessage(String messageType, String businessKey1, String businessKey2, String businessKey3);
	
	List<MqMessage> getMessageList(int shardIdx, int shardTotal, String messageType, int count);
	
	/**
	 * 查询一阶段任务执行状态
	 *
	 * @param msgId 任务id
	 * @return 状态
	 */
	int getStageOne(Long msgId);
	
	
	/**
	 * 查询二阶段任务执行状态
	 *
	 * @param msgId 任务id
	 * @return 状态
	 */
	int getStageTwo(Long msgId);
	
	
	/**
	 * 查询三阶段任务执行状态
	 *
	 * @param msgId 任务id
	 * @return 状态
	 */
	int getStageThree(Long msgId);
	
	/**
	 * 查询四阶段任务执行状态
	 *
	 * @param msgId 任务id
	 * @return 状态
	 */
	int getStageFour(Long msgId);
	
	/**
	 * 更新一阶段任务状态为完成
	 *
	 * @param msgId 消息id
	 */
	int completeStageOne(Long msgId);
	
	/**
	 * 更新二阶段任务状态为完成
	 *
	 * @param msgId 消息id
	 */
	int completeStageTwo(Long msgId);
	
	/**
	 * 更新三阶段任务状态为完成
	 *
	 * @param msgId 消息id
	 */
	int completeStageThree(Long msgId);
	
	/**
	 * 更新四阶段任务状态
	 *
	 * @param msgId 消息id
	 */
	int completeStageFour(Long msgId);
	
	/**
	 * 更新任务状态为完成
	 *
	 * @param id 任务id
	 * @return 更新状态
	 */
	boolean completed(Long id);
}
