package com.zkc.xcplus.media.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkc.xcplus.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MediaProcessMapper extends BaseMapper<MediaProcess> {
	
	/**
	 * 数据id%分片总数的结果如果等于当前分片索引  当前分片处理该任务
	 *
	 * @param shardTotal    xxl-job执行器分片总数
	 * @param curShardIndex 当前执行器索引
	 * @param retCount      每次获取的任务数量
	 * @return 任务列表
	 */
	@Select("select *\n" +
			"from media_process\n" +
			"where id % #{shardTotal} = #{curShardIndex}\n" +
			"  and (status = 1 or status = 3)\n" +
			"  and fail_count < 3\n" +
			"limit #{retCount};")
	List<MediaProcess> selectListByShardIndex(@Param("shardTotal") int shardTotal, @Param("curShardIndex") int curShardIndex, @Param("retCount") int retCount);
	
	/**
	 * 开启任务 数据库乐观锁
	 *
	 * @param id 任务id
	 * @return 更新行数
	 */
	@Update("update media_process\n" +
			"set status='4'\n" +
			"where (status = '1' or status = '3')\n" +
			"  and fail_count < 3\n" +
			"  and id = #{id};")
	int startTask(@Param("id") long id);
}