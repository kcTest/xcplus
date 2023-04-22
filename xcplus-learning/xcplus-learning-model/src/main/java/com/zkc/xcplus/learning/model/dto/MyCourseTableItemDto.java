package com.zkc.xcplus.learning.model.dto;

import com.zkc.xcplus.learning.model.po.XcCourseTables;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyCourseTableItemDto extends XcCourseTables {
	
	/**
	 * 最近学习时间
	 */
	private LocalDateTime learnDate;
	
	/**
	 * 学习时长
	 */
	private Long learnLength;
	
	/**
	 * 章节id
	 */
	private Long teachPlanId;
	
	/**
	 * 章节名称
	 */
	private String teachPlanName;
}
