package com.zkc.xcplus.media.service;

import com.zkc.xcplus.media.model.po.MediaProcess;

import java.util.List;

public interface MediaProcessService {
	
	List<MediaProcess> selectListByShardIndex(int shardTotal, int curShardIndex, int retCount);
	
	boolean startTask(long id);
	
	/**
	 * 保存任务处理结果
	 *
	 * @param taskId   任务id
	 * @param status   任务状态
	 * @param fileId   文件id
	 * @param url      文件url
	 * @param errorMsg 错误信息
	 */
	void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
