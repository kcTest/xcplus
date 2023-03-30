package com.zkc.xcplus.search.service;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.search.dto.SearchCourseParamDto;
import com.zkc.xcplus.search.dto.SearchPageResultDto;
import com.zkc.xcplus.search.po.CourseIndex;

/**
 * 课程搜索
 */
public interface CourseSearchService {
	
	
	/**
	 * 根据关键字搜索课程列表
	 *
	 * @param pageParams           分页参数
	 * @param searchCourseParamDto 搜索条件
	 * @return 课程列表
	 */
	SearchPageResultDto<CourseIndex> queryCoursePublishIndex(PageParams pageParams, SearchCourseParamDto searchCourseParamDto);
}
