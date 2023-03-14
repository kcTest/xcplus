package com.zkc.xcplus.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkc.xcplus.base.exception.CommonError;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.content.model.dto.BindTeachPlanMediaDto;
import com.zkc.xcplus.content.model.dto.SaveTeachPlanDto;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.model.po.Teachplan;
import com.zkc.xcplus.content.model.po.TeachplanMedia;
import com.zkc.xcplus.content.service.TeachPlanService;
import com.zkc.xcplus.content.service.dao.TeachplanMapper;
import com.zkc.xcplus.content.service.dao.TeachplanMediaMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeachPlanServiceImpl implements TeachPlanService {
	
	@Autowired
	private TeachplanMapper teachplanMapper;
	
	@Autowired
	private TeachplanMediaMapper teachplanMediaMapper;
	
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
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public void bindMedia(BindTeachPlanMediaDto dto) {
		Long planId = dto.getTeachPlanId();
		Teachplan teachplan = teachplanMapper.selectById(planId);
		if (teachplan == null) {
			CustomException.cast("课程计划不存在");
		}
		if (teachplan.getGrade() != 2) {
			CustomException.cast("只允许第二季教学计划绑定媒资文件");
		}
		
		LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(TeachplanMedia::getTeachplanId, planId);
		teachplanMediaMapper.delete(queryWrapper);
		
		TeachplanMedia teachplanMedia = new TeachplanMedia();
		teachplanMedia.setMediaId(dto.getMediaId());
		teachplanMedia.setTeachplanId(planId);
		teachplanMedia.setMediaFilename(dto.getFileName());
		teachplanMedia.setCreateDate(LocalDateTime.now());
		teachplanMedia.setCourseId(teachplan.getCourseId());
		teachplanMediaMapper.insert(teachplanMedia);
	}
	
	private Long getSiblingPlanCount(Long courseId, Long parentId) {
		LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Teachplan::getCourseId, courseId);
		queryWrapper.eq(Teachplan::getParentid, parentId);
		return teachplanMapper.selectCount(queryWrapper);
	}
}
