package com.zkc.xcplus.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkc.xcplus.message.model.MqMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MqMessageMapper extends BaseMapper<MqMessage> {
	@Select("select *\n" +
			"from mq_message\n" +
			"where id % #{shardTotal} = #{shardIdx}\n" +
			"  and state = '0'\n" +
			"  and message_type = #{messageType}\n" +
			"limit #{count};")
	List<MqMessage> selectListByShardIndex(@Param("shardIdx") int shardIdx, @Param("shardTotal") int shardTotal, @Param("messageType") String messageType, @Param("count") int count);
}