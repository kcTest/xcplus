package com.zkc.xcplus.content.service.impl;

import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.service.TeachPlanService;
import com.zkc.xcplus.content.service.dao.TeachplanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachPlanServiceImpl implements TeachPlanService {
	
	@Autowired
	private TeachplanMapper teachplanMapper;
	
	@Override
	public List<TeachPlanDto> getTreeNodes(Long courseId) {
		return teachplanMapper.getTreeNodes(courseId);
	}
}
