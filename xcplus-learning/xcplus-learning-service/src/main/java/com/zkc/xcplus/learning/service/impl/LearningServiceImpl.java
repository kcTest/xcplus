package com.zkc.xcplus.learning.service.impl;

import com.zkc.xcplus.base.model.RestResponse;
import com.zkc.xcplus.content.model.po.CoursePublish;
import com.zkc.xcplus.learning.model.dto.XcCourseTablesDto;
import com.zkc.xcplus.learning.service.CourseTableService;
import com.zkc.xcplus.learning.service.LearningService;
import com.zkc.xcplus.learning.service.UserInfoService;
import com.zkc.xcplus.learning.service.feignclient.ContentServiceClient;
import com.zkc.xcplus.learning.service.feignclient.MediaServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningServiceImpl implements LearningService {
	
	@Autowired
	private ContentServiceClient contentServiceClient;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private MediaServiceClient mediaServiceClient;
	
	@Autowired
	private CourseTableService courseTableService;
	
	@Override
	public RestResponse<String> getVideo(Long courseId, Long teachPlanId, String mediaId) {
		
		//查询课程信息
		CoursePublish coursePublish = contentServiceClient.getCoursePublish(courseId);
		if (coursePublish == null) {
			return RestResponse.validfail("课程不存在");
		}
		//如果免费直接返回视频地址
		if ("201000".equals(coursePublish.getCharge())) {
			return mediaServiceClient.getPlayUrlByMediaId(mediaId);
		}
		
		try {
			userInfoService.getCurrentUser();
		} catch (Exception e) {
			return RestResponse.validfail("课程需登录购买");
		}
		
		//获取学习资格
		XcCourseTablesDto courseTablesDto = courseTableService.getLearningStatus(courseId);
		String learningStatus = courseTablesDto.getLearningStatus();
		if ("702002".equals(learningStatus)) {
			return RestResponse.validfail("无法学习,因为没有选课或选课后没有支付");
		} else if ("702003".equals(learningStatus)) {
			return RestResponse.validfail("已过期需要申请或重新支付");
		} else {
			//有学习资格 返回视频播放地址
			return mediaServiceClient.getPlayUrlByMediaId(mediaId);
		}
	}
}
