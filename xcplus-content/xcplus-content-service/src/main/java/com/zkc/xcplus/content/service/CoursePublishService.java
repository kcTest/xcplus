package com.zkc.xcplus.content.service;

import com.zkc.xcplus.content.model.dto.CoursePreviewDto;

/**
 * 课程发布
 */
public interface CoursePublishService {
	
	/**
	 * 根据课程id返回课程信息
	 *
	 * @param courseId 课程id
	 * @return 预览页面所需课程相关信息
	 */
	CoursePreviewDto getCoursePreviewInfo(Long courseId);
}
