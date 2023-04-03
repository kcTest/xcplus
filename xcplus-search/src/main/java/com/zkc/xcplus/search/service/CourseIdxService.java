package com.zkc.xcplus.search.service;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.search.dto.CourseIdxSearchParamDto;
import com.zkc.xcplus.search.dto.IdxSearchResultDto;
import com.zkc.xcplus.search.po.CourseIndexInfo;

/**
 * 课程索引管理
 */
public interface CourseIdxService {
	
	
	/**
	 * 添加课程索引
	 *
	 * @param courseIndexInfo 课程索引
	 * @return 添加是否成功
	 */
	boolean addCourseIdx(CourseIndexInfo courseIndexInfo);
	
	/**
	 * 更新课程索引
	 *
	 * @param courseIndexInfo 课程索引
	 * @return 添更新是否成功
	 */
	boolean updateCourseIdx(CourseIndexInfo courseIndexInfo);
	
	/**
	 * 删除课程索引
	 *
	 * @param id 课程id
	 * @return 删除索引是否成功
	 */
	boolean deleteCourseIdx(String id);
	
	
	/**
	 * 根据关键字搜索课程列表
	 *
	 * @param pageParams           分页参数
	 * @param searchCourseParamDto 搜索条件
	 * @return 课程列表
	 */
	IdxSearchResultDto<CourseIndexInfo> queryCoursePublishIndex(PageParams pageParams, CourseIdxSearchParamDto searchCourseParamDto);
}
