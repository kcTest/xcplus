package com.zkc.xcplus.content.service.jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.content.model.dto.CourseBaseInfoDto;
import com.zkc.xcplus.content.model.po.CoursePublish;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import com.zkc.xcplus.content.service.CoursePublishService;
import com.zkc.xcplus.content.service.dao.CoursePublishMapper;
import com.zkc.xcplus.content.service.po.CourseIndexInfo;
import com.zkc.xcplus.message.model.MqMessage;
import com.zkc.xcplus.message.service.MessageProcessAbstract;
import com.zkc.xcplus.message.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 课程发布相关任务处理
 */
@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {
	
	@Autowired
	private CoursePublishService coursePublishService;
	
	@Autowired
	private CoursePublishMapper coursePublishMapper;
	
	@XxlJob("CoursePublishJobHandler")
	public void coursePublishJobHandler() {
		int shardIndex = XxlJobHelper.getShardIndex();
		int shardTotal = XxlJobHelper.getShardTotal();
		
		process(shardIndex, shardTotal, "course_publish", 30, 60);
	}
	
	@Override
	public boolean execute(MqMessage msg) {
		//获取消息的业务信息
		String businessKey1 = msg.getBusinessKey1();
		long courseId = Long.parseLong(businessKey1);
		
		generateCourseHtml(msg, courseId);
		saveCourseIndex(msg, courseId);
		saveCourseCache(msg, courseId);
		
		return true;
	}
	
	/**
	 * 课程页面静态化上传到minio
	 *
	 * @param msg      消息
	 * @param courseId 课程id
	 */
	private void generateCourseHtml(MqMessage msg, long courseId) {
		//任务幂等性处理
		Long msgId = msg.getId();
		MqMessageService messageService = this.getMqMessageService();
		int stageOne = messageService.getStageOne(msgId);
		if (stageOne > 0) {
			log.debug("页面静态化已完成,不需要处理");
			return;
		}
		
		//处理
		File file = coursePublishService.generateCourseHtml(courseId);
		if (file == null) {
			CustomException.cast("生成静态页面为空");
		}
		//最后由媒资服务上传文件
		coursePublishService.uploadCourseHtml(courseId, file);
		
		//更新一阶段完成状态
		messageService.completeStageOne(msgId);
	}
	
	/**
	 * 保存课程索引到ES
	 *
	 * @param msg      消息
	 * @param courseId 课程id
	 */
	private void saveCourseIndex(MqMessage msg, long courseId) {
		//任务幂等性处理
		Long msgId = msg.getId();
		MqMessageService messageService = this.getMqMessageService();
		int stageTwo = messageService.getStageTwo(msgId);
		if (stageTwo > 0) {
			log.debug("保存课程索引到ES已完成,不需要处理");
			return;
		}
		
		//处理
		CoursePublish coursePublish = coursePublishMapper.selectById(courseId);
		CourseIndexInfo indexInfo = new CourseIndexInfo();
		BeanUtils.copyProperties(coursePublish, indexInfo);
		coursePublishService.addCourseIdx(indexInfo);
		
		//更新二阶段完成状态
		messageService.completeStageTwo(msgId);
	}
	
	/**
	 * 课程缓存保存到redis
	 *
	 * @param msg      消息
	 * @param courseId 课程id
	 */
	private void saveCourseCache(MqMessage msg, long courseId) {
		//任务幂等性处理
		Long msgId = msg.getId();
		MqMessageService messageService = this.getMqMessageService();
		int stageThree = messageService.getStageThree(msgId);
		if (stageThree > 0) {
			log.debug("保存课程缓存到Redis已完成,不需要处理");
			return;
		}
		
		//处理
		int i = 1 / 0;
		
		//更新三阶段完成状态
		messageService.completeStageThree(msgId);
	}
	
}
