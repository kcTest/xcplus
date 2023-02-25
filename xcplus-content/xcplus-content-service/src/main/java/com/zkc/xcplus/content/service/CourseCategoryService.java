package com.zkc.xcplus.content.service;

import com.zkc.xcplus.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategoryService {
	
	/**
	 * 查询课程分类
	 *
	 * @return 课程分类 树形结构
	 */
	List<CourseCategoryTreeDto> getTreeNodes(String parentId);
}
