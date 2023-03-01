package com.zkc.xcplus.content.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeachplanMapper extends BaseMapper<Teachplan> {
	/**
	 * 查询课程计划
	 */
	List<TeachPlanDto> getTreeNodes(@Param("courseId") Long courseId);
}