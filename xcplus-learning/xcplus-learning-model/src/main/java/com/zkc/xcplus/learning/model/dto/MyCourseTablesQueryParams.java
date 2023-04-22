package com.zkc.xcplus.learning.model.dto;

import lombok.Data;

@Data
public class MyCourseTablesQueryParams {
	
	/**
	 * 用户id
	 */
	String userId;
	
	/**
	 * 课程类型
	 * 700001 免费课程
	 * 700002 收费课程
	 */
	private String courseType;
	
	/**
	 * 排序类型
	 * 1 按学习时间排序
	 * 2 按加入时间排序
	 */
	private String sortType;
	
	/**
	 * 过期类型
	 * 1 即将过去
	 * 2 已经过期
	 */
	private String expireTypes;
	
	/**
	 * 当前页码
	 */
	private int pageNo = 1;
	int startIdx = 0;
	/**
	 * 每页记录数
	 */
	int pageSize = 4;
}
