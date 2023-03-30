package com.zkc.xcplus.search.service.impl;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.search.dto.SearchCourseParamDto;
import com.zkc.xcplus.search.dto.SearchPageResultDto;
import com.zkc.xcplus.search.po.CourseIndex;
import com.zkc.xcplus.search.service.CourseSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourseSearchServiceImpl implements CourseSearchService {
	
	@Value("${elasticsearch.course.index}")
	private String courseIndex;
	
	@Value("${elasticsearch.course.source_fields}")
	private String sourceFields;
	
	@Autowired
	private ElasticsearchOperations operations;
	
	@Override
	public SearchPageResultDto<CourseIndex> queryCoursePublishIndex(PageParams pageParams, SearchCourseParamDto searchCourseParamDto) {
		return null;
	}
}
