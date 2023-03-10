package com.zkc.xcplus.content.service;

import com.zkc.xcplus.content.model.dto.BindTeachPlanMediaDto;
import com.zkc.xcplus.content.model.dto.SaveTeachPlanDto;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;

import java.util.List;

public interface TeachPlanService {
	List<TeachPlanDto> getTreeNodes(Long courseId);
	
	void saveTeachPlan(SaveTeachPlanDto dto);
	
	void bindMedia(BindTeachPlanMediaDto dto);
}
