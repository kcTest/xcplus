package com.zkc.xcplus.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.content.model.dto.CourseBaseInfoDto;
import com.zkc.xcplus.content.model.dto.CoursePreviewDto;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import com.zkc.xcplus.content.model.po.CourseMarket;
import com.zkc.xcplus.content.model.po.CoursePublishPre;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import com.zkc.xcplus.content.service.CoursePublishService;
import com.zkc.xcplus.content.service.TeachPlanService;
import com.zkc.xcplus.content.service.dao.CourseBaseMapper;
import com.zkc.xcplus.content.service.dao.CourseMarketMapper;
import com.zkc.xcplus.content.service.dao.CoursePublishPreMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CoursePublishServiceImpl implements CoursePublishService {
	
	@Autowired
	private CourseBaseInfoService courseBaseInfoService;
	
	@Autowired
	private TeachPlanService teachPlanService;
	
	@Autowired
	private CourseMarketMapper courseMarketMapper;
	
	@Autowired
	private CoursePublishPreMapper coursePublishPreMapper;
	
	@Autowired
	private CourseBaseMapper courseBaseMapper;
	
	@Override
	public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
		CoursePreviewDto ret = new CoursePreviewDto();
		
		//基本信息
		CourseBaseInfoDto baseInfo = courseBaseInfoService.get(courseId);
		//课程计划
		List<TeachPlanDto> plan = teachPlanService.getTreeNodes(courseId);
		
		ret.setCourseBaseInfo(baseInfo);
		ret.setTeachPlans(plan);
		return ret;
	}
	
	@Transactional
	@Override
	public void submitForReview(Long companyId, Long courseId) {
		//约束
		CourseBaseInfoDto baseInfo = courseBaseInfoService.get(courseId);
		if (baseInfo == null) {
			CustomException.cast("课程未找到");
		}
		if (baseInfo.getCompanyId().longValue() != companyId.longValue()) {
			CustomException.cast("只能提交本机构课程");
		}
		if ("202003".equals(baseInfo.getAuditStatus())) {
			CustomException.cast("课程已提交");
		}
		if (!StringUtils.hasText(baseInfo.getPic())) {
			CustomException.cast("请上传课程图片");
		}
		List<TeachPlanDto> planDtoList = teachPlanService.getTreeNodes(courseId);
		if (planDtoList == null || planDtoList.size() == 0) {
			CustomException.cast("请添加课程计划");
		}
		//插入课程预发布表
		CoursePublishPre coursePublishPre = new CoursePublishPre();
		//基本
		BeanUtils.copyProperties(baseInfo, coursePublishPre);
		//营销信息
		CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
		String courseMarketJson = JSON.toJSONString(courseMarket);
		coursePublishPre.setMarket(courseMarketJson);
		//计划
		String planJson = JSON.toJSONString(planDtoList);
		coursePublishPre.setTeachplan(planJson);
		//已提交
		coursePublishPre.setStatus("202003");
		coursePublishPre.setCreateDate(LocalDateTime.now());
		
		//已存在更新
		CoursePublishPre coursePublishPreOld = coursePublishPreMapper.selectById(courseId);
		if (coursePublishPreOld == null) {
			coursePublishPreMapper.insert(coursePublishPre);
		} else {
			coursePublishPreMapper.updateById(coursePublishPre);
		}
		
		//修改课程基本信息表状态
		CourseBase courseBase = courseBaseMapper.selectById(courseId);
		//已提交
		courseBase.setAuditStatus("202003");
		courseBaseMapper.updateById(courseBase);
	}
}
