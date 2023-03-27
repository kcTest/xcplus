package com.zkc.xcplus.content.service;

import com.zkc.xcplus.content.model.dto.CoursePreviewDto;

import java.io.File;

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
	
	/**
	 * 课程发布前提交审核
	 *
	 * @param companyId 机构id
	 * @param courseId  课程id
	 */
	void submitForReview(Long companyId, Long courseId);
	
	/**
	 * 课程发布
	 *
	 * @param companyId 机构id
	 * @param courseId  课程id
	 */
	void coursePublish(Long companyId, Long courseId);
	
	/**
	 * 课程页面静态化
	 *
	 * @param courseId 课程id
	 * @return 静态文件
	 */
	File generateCourseHtml(Long courseId);
	
	/**
	 * 上传静态页面文件到minio
	 *
	 * @param courseId 课程id
	 * @param file     静态文件
	 */
	void uploadCourseHtml(long courseId, File file);
}
