package com.zkc.xcplus.learning.model.dto;

import com.zkc.xcplus.learning.model.po.XcCourseTables;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class XcCourseTablesDto extends XcCourseTables {
	
	/**
	 * 学习资格
	 */
	private String learnStatus;
}
