package com.zkc.xcplus.content.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkc.xcplus.content.model.dto.CourseCategoryTreeDto;
import com.zkc.xcplus.content.model.po.CourseCategory;

import java.util.List;

public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
	
	List<CourseCategoryTreeDto> getTreeNodes();
}