package com.zkc.xcplus.learning.service;


import com.zkc.xcplus.base.model.RestResponse;

/**
 * 在线学习相关接口
 */
public interface LearningService {
	
	/**
	 * 获取教学视频URL
	 *
	 * @param courseId    课程id
	 * @param teachPlanId 课程计划id
	 * @param mediaId     视频文件id
	 * @return 视频URL
	 */
	RestResponse<String> getVideo(Long courseId, String mediaId);
}
