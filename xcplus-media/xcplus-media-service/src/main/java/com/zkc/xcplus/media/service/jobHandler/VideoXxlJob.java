package com.zkc.xcplus.media.service.jobHandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zkc.xcplus.base.utils.Mp4VideoUtil;
import com.zkc.xcplus.media.model.po.MediaProcess;
import com.zkc.xcplus.media.service.MediaFileService;
import com.zkc.xcplus.media.service.MediaProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 视频格式转换
 */
@Component
@Slf4j
public class VideoXxlJob {
	
	
	@Autowired
	private MediaProcessService mediaProcessService;
	
	@Autowired
	private MediaFileService mediaFileService;
	
	/**
	 * ffmpeg的路径
	 */
	@Value("${videoprocess.ffmpegpath}")
	private String ffmpegPath;
	
	/**
	 * 不分片上传  先使用默认桶测试
	 */
	@Value("${minio.bucket.default}")
	private String defaultBucket;
	
	/**
	 * 分片广播任务
	 */
	@XxlJob("videoJobHandler")
	public void videoJobHandler() throws Exception {
		
		// 分片参数
		int shardIndex = XxlJobHelper.getShardIndex();
		int shardTotal = XxlJobHelper.getShardTotal();
		
		/*
		查询符合条件的任务
	    使用多线程并行处理任务 
	    确定cpu数量 作为每次最多获取的任务数量
		*/
		int processors = Runtime.getRuntime().availableProcessors();
		List<MediaProcess> taskLst = mediaProcessService.selectListByShardIndex(shardTotal, shardIndex, processors);
		int size = taskLst.size();
		ExecutorService threadPool = Executors.newFixedThreadPool(size);
		CountDownLatch countDownLatch = new CountDownLatch(size);
		for (MediaProcess task : taskLst) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Long taskId = task.getId();
						String fileId = task.getFileId();
						String bucket = task.getBucket();
						String filePath = task.getFilePath();
						
						//抢夺开启任务
						boolean success = mediaProcessService.startTask(taskId);
						if (!success) {
							log.debug("@@@1@@@抢占任务失败 任务id:{}", taskId);
							return;
						}
						log.info("@@@1@@@抢夺任务成功 ID:{}", taskId);
						
						//下载视频到本地
						File fileFromMinio = mediaFileService.downloadFileFromMinio(defaultBucket, filePath);
						if (fileFromMinio == null) {
							String errorMsg = "@@@2@@@视频处理失败 视频下载出错";
							log.debug(errorMsg);
							mediaProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, errorMsg);
							return;
						}
						log.info("@@@2@@@下载视频到本地 objectname:{}", filePath);
						
						//avi临时视频下载目录 源avi视频的路径 / avi临时视频转换后存放目录
						String sourceAviPath = fileFromMinio.getAbsolutePath();
						String ext = ".mp4";
						// avi临时视频转换后路径
						String targetFilePath = sourceAviPath.substring(0, sourceAviPath.lastIndexOf(".")) + ext;
						//开始视频转换，成功将返回success
						Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegPath, sourceAviPath, targetFilePath);
						String s = videoUtil.generateMp4();
						if (!"success".equals(s)) {
							String errorMsg = String.format("@@@3@@@视频处理失败: %s", s);
							log.debug(errorMsg);
							mediaProcessService.saveProcessFinishStatus(taskId, "2", fileId, null, errorMsg);
							return;
						}
						log.info("@@@3@@@视频转码成功 文件路径:{}", targetFilePath);
						
						//上传到minio
						String newFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + ext;
						boolean upload = mediaFileService.addLocalFileToMinio(bucket, "video/mp4", targetFilePath, newFilePath);
						if (!upload) {
							String errorMsg = "@@@4@@@视频处理失败: 上传到minio失败";
							log.debug(errorMsg);
							mediaProcessService.saveProcessFinishStatus(taskId, "2", fileId, null, errorMsg);
							return;
						}
						log.info("@@@4@@@MP4视频上传到minio成功  taskId: {}", taskId);
						
						//保存完成状态
						String url = "/" + defaultBucket + "/" + newFilePath;
						mediaProcessService.saveProcessFinishStatus(taskId, "2", fileId, url, null);
						log.info("@@@5@@@已保存完成状态 url:{}", url);
					} finally {
						//成功或失败后计数减一
						countDownLatch.countDown();
					}
				}
			});
		}
		//最多等待30min
		countDownLatch.await(30, TimeUnit.MINUTES);
	}
	
}