package com.zkc.xcplus.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zkc.xcplus.media.model.po.MediaFiles;
import com.zkc.xcplus.media.model.po.MediaProcess;
import com.zkc.xcplus.media.model.po.MediaProcessHistory;
import com.zkc.xcplus.media.service.MediaProcessService;
import com.zkc.xcplus.media.service.dao.MediaFilesMapper;
import com.zkc.xcplus.media.service.dao.MediaProcessHistoryMapper;
import com.zkc.xcplus.media.service.dao.MediaProcessMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class MediaProcessServiceImpl implements MediaProcessService {
	
	@Autowired
	private MediaProcessMapper mediaProcessMapper;
	
	@Autowired
	private MediaFilesMapper mediaFilesMapper;
	
	@Autowired
	private MediaProcessHistoryMapper mediaProcessHistoryMapper;
	
	@Override
	public List<MediaProcess> selectListByShardIndex(int shardTotal, int curShardIndex, int retCount) {
		return mediaProcessMapper.selectListByShardIndex(shardTotal, curShardIndex, retCount);
	}
	
	@Override
	public boolean startTask(long id) {
		return mediaProcessMapper.startTask(id) > 0;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
		
		MediaProcess task = mediaProcessMapper.selectById(taskId);
		if (task == null) {
			return;
		}
		//失败
		if ("3".equals(status)) {
			LambdaUpdateWrapper<MediaProcess> updateWrapper = new LambdaUpdateWrapper<>();
			updateWrapper.eq(MediaProcess::getId, taskId);
			updateWrapper.set(MediaProcess::getStatus, status);
			updateWrapper.set(MediaProcess::getErrormsg, errorMsg);
			updateWrapper.set(MediaProcess::getFailCount, task.getFailCount() + 1);
			mediaProcessMapper.update(task, updateWrapper);
			return;
		}
		//成功 
		if ("2".equals(status)) {
			//更新任务
			task.setStatus(status);
			task.setUrl(url);
			task.setFinishDate(LocalDateTime.now());
			mediaProcessMapper.updateById(task);
			
			//更新media_file的url
			MediaFiles file = mediaFilesMapper.selectById(fileId);
			LambdaUpdateWrapper<MediaFiles> uwMediaFiles = new LambdaUpdateWrapper<>();
			uwMediaFiles.eq(MediaFiles::getFileId, fileId);
			uwMediaFiles.set(MediaFiles::getUrl, url);
			mediaFilesMapper.update(file, uwMediaFiles);
			
			//插入历史记录
			MediaProcessHistory history = new MediaProcessHistory();
			BeanUtils.copyProperties(task, history);
			mediaProcessHistoryMapper.insert(history);
			
			//删除任务
			mediaProcessMapper.deleteById(taskId);
			
		}
		
	}
	
}
