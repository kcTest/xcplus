package com.zkc.xcplus.message.service.impl;

import com.zkc.xcplus.message.dao.MqMessageHistoryMapper;
import com.zkc.xcplus.message.dao.MqMessageMapper;
import com.zkc.xcplus.message.model.MqMessage;
import com.zkc.xcplus.message.model.MqMessageHistory;
import com.zkc.xcplus.message.service.MqMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @service-
 * @Autowired private MqMessageService
 * <p>
 * MybatisPlusConfig-
 * @Autowired private MqMessageMapper
 */
@Service
public class MqMessageServiceImpl implements MqMessageService {
	
	@Autowired
	private MqMessageMapper mqMessageMapper;
	
	@Autowired
	private MqMessageHistoryMapper mqMessageHistoryMapper;
	
	@Override
	public MqMessage addMessage(String messageType, String businessKey1, String businessKey2, String businessKey3) {
		MqMessage message = new MqMessage();
		message.setMessageType(messageType);
		message.setBusinessKey1(businessKey1);
		message.setBusinessKey2(businessKey2);
		message.setBusinessKey3(businessKey3);
		int count = mqMessageMapper.insert(message);
		if (count > 0) {
			return message;
		} else {
			return null;
		}
	}
	
	@Override
	public List<MqMessage> getMessageList(int shardIdx, int shardTotal, String messageType, int count) {
		return mqMessageMapper.selectListByShardIndex(shardIdx, shardTotal, messageType, count);
	}
	
	@Override
	public int getStageOne(Long msgId) {
		return Integer.parseInt(mqMessageMapper.selectById(msgId).getStageState1());
	}
	
	@Override
	public int getStageTwo(Long msgId) {
		return Integer.parseInt(mqMessageMapper.selectById(msgId).getStageState2());
		
	}
	
	@Override
	public int getStageThree(Long msgId) {
		return Integer.parseInt(mqMessageMapper.selectById(msgId).getStageState3());
		
	}
	
	@Override
	public int getStageFour(Long msgId) {
		return Integer.parseInt(mqMessageMapper.selectById(msgId).getStageState3());
		
	}
	
	@Override
	public int completeStageOne(Long msgId) {
		MqMessage mqMessage = new MqMessage();
		mqMessage.setStageState1("1");
		mqMessage.setId(msgId);
		return mqMessageMapper.updateById(mqMessage);
	}
	
	@Override
	public int completeStageTwo(Long msgId) {
		MqMessage mqMessage = new MqMessage();
		mqMessage.setStageState2("1");
		mqMessage.setId(msgId);
		return mqMessageMapper.updateById(mqMessage);
	}
	
	@Override
	public int completeStageThree(Long msgId) {
		MqMessage mqMessage = new MqMessage();
		mqMessage.setStageState3("1");
		mqMessage.setId(msgId);
		return mqMessageMapper.updateById(mqMessage);
	}
	
	@Override
	public int completeStageFour(Long msgId) {
		MqMessage mqMessage = new MqMessage();
		mqMessage.setStageState3("1");
		mqMessage.setId(msgId);
		return mqMessageMapper.updateById(mqMessage);
	}
	
	@Override
	public boolean completed(Long id) {
		
		MqMessage mqMessage = new MqMessage();
		mqMessage.setState("1");
		mqMessage.setId(id);
		int count = mqMessageMapper.updateById(mqMessage);
		if (count > 0) {
			mqMessage = mqMessageMapper.selectById(id);
			//添加历史记录并移除任务
			MqMessageHistory messageHistory = new MqMessageHistory();
			BeanUtils.copyProperties(mqMessage, messageHistory);
			mqMessageHistoryMapper.insert(messageHistory);
			mqMessageMapper.deleteById(id);
			return true;
		}
		return false;
	}
	
}
