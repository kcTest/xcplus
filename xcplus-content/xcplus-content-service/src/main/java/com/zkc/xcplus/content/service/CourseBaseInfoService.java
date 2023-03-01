package com.zkc.xcplus.content.service;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.AddCourseDto;
import com.zkc.xcplus.content.model.dto.CourseBaseInfoDto;
import com.zkc.xcplus.content.model.dto.CourseQueryParamsDto;
import com.zkc.xcplus.content.model.dto.UpdateCourseDto;
import com.zkc.xcplus.content.model.po.CourseBase;

public interface CourseBaseInfoService {
	
	/**
	 * 查询课程
	 *
	 * @param pageParams           分页
	 * @param courseQueryParamsDto 查询条件
	 * @return 课程列表
	 */
	PageResult<CourseBase> courseBaseList(PageParams pageParams, CourseQueryParamsDto courseQueryParamsDto);
	
	/**
	 * 
	 * @param companyId 机构ID
	 * @param addCourseDto 新增课程信息
	 * @return 课程基本信息
	 */
	CourseBaseInfoDto add(Long companyId, AddCourseDto addCourseDto);
	
	CourseBaseInfoDto get(Long id);
	
	CourseBaseInfoDto update(Long companyId,UpdateCourseDto dto);
}
