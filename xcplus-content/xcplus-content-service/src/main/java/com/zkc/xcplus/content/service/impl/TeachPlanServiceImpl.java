package com.zkc.xcplus.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkc.xcplus.base.exception.CommonError;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.content.model.dto.SaveTeachPlanDto;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.model.po.Teachplan;
import com.zkc.xcplus.content.service.TeachPlanService;
import com.zkc.xcplus.content.service.dao.TeachplanMapper;
import org.springframework.beans.BeanUtils;
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
	
	@Override
	public void saveTeachPlan(SaveTeachPlanDto dto) {
		if (dto == null) {
			CustomException.cast(CommonError.OBJECT_NULL);
		}
		Long planId = dto.getId();
		if (planId == null) {
			//新增 第一级章 或 第二级节
			Teachplan teachplan = new Teachplan();
			BeanUtils.copyProperties(dto, teachplan);
			//新增时顺序为同级别最后一个
			teachplan.setOrderby(Math.toIntExact(getSiblingPlanCount(teachplan.getCourseId(), teachplan.getParentid())));
			teachplanMapper.insert(teachplan);
		} else {
			//修改时没有调整顺序
			Teachplan teachplan = teachplanMapper.selectById(planId);
			if (teachplan == null) {
				CustomException.cast("课程计划不存在");
			}
			BeanUtils.copyProperties(dto, teachplan);
			teachplanMapper.updateById(teachplan);
		}
	}
	
	private Long getSiblingPlanCount(Long courseId, Long parentId) {
		LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Teachplan::getCourseId, courseId);
		queryWrapper.eq(Teachplan::getParentid, parentId);
		return teachplanMapper.selectCount(queryWrapper);
	}
}
