package com.zkc.xcplus.content.model.dto;

import com.zkc.xcplus.content.model.po.Teachplan;
import com.zkc.xcplus.content.model.po.TeachplanMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 课程计划 分为两级 第一级 章
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeachPlanDto extends Teachplan {
	
	/**
	 * 课程关联的媒体信息
	 */
	private TeachplanMedia teachplanMedia;
	/**
	 * 第二级 节
	 */
	List<TeachPlanDto> childTreeNodes;
	
}
