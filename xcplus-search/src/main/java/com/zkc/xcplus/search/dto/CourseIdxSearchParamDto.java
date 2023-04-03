package com.zkc.xcplus.search.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 课程搜索参数
 */
@Data
@ToString
public class CourseIdxSearchParamDto {
	
	/**
	 * 关键字
	 */
	private String keywords;
	
	/**
	 * 大分类
	 */
	private String mt;
	
	/**
	 * 小分类
	 */
	private String st;
	
	/**
	 * 难度等级
	 */
	private String grade;
}
