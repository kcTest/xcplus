package com.zkc.xcplus.learning.service;

import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.learning.model.dto.MyCourseTablesQueryParams;
import com.zkc.xcplus.learning.model.dto.XcChooseCourseDto;
import com.zkc.xcplus.learning.model.dto.XcCourseTablesDto;
import com.zkc.xcplus.learning.model.po.XcCourseTables;

/**
 * 选课相关接口
 */
public interface CourseTableService {
	
	/**
	 * 添加选课
	 *
	 * @param courseId 课程id
	 * @return 选课信息
	 */
	XcChooseCourseDto addChooseCourse(Long courseId);
	
	/**
	 * 判断学习资格
	 *
	 * @param courseId 课程id
	 * @return 课程表信息
	 */
	XcCourseTablesDto getLearningStatus(Long courseId);
	
	/**
	 * 购买后更新选课记录成功状态 并且生成我的课程表
	 *
	 * @param chooseCourseId 选课记录id
	 * @return 是否保存成功
	 */
	boolean saveChooseCourseSuccess(String chooseCourseId);
	
	/**
	 * 获取我的课程表
	 *
	 * @param params 查询条件
	 * @return 课程表列表
	 */
	PageResult<XcCourseTables> myCourseTables(MyCourseTablesQueryParams params);
}
