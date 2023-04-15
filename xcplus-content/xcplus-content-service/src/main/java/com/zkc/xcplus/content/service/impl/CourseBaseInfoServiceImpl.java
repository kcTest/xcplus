package com.zkc.xcplus.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkc.xcplus.base.exception.CommonError;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.AddCourseDto;
import com.zkc.xcplus.content.model.dto.CourseBaseInfoDto;
import com.zkc.xcplus.content.model.dto.CourseQueryParamsDto;
import com.zkc.xcplus.content.model.dto.UpdateCourseDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import com.zkc.xcplus.content.model.po.CourseCategory;
import com.zkc.xcplus.content.model.po.CourseMarket;
import com.zkc.xcplus.content.model.po.XcUser;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import com.zkc.xcplus.content.service.CourseMarketService;
import com.zkc.xcplus.content.service.UserInfoService;
import com.zkc.xcplus.content.service.dao.CourseBaseMapper;
import com.zkc.xcplus.content.service.dao.CourseCategoryMapper;
import com.zkc.xcplus.content.service.dao.CourseMarketMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
	
	@Autowired
	private CourseBaseMapper courseBaseMapper;
	
	@Autowired
	private CourseMarketMapper courseMarketMapper;
	
	@Autowired
	private CourseMarketService courseMarketService;
	
	@Autowired
	private CourseCategoryMapper courseCategoryMapper;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Override
	public PageResult<CourseBase> courseBaseList(PageParams pageParams, CourseQueryParamsDto courseQueryParamsDto) {
		//分页
		Long pageNo = pageParams.getPageNo();
		Long pageSize = pageParams.getPageSize();
		Page<CourseBase> page = new Page<>(pageNo, pageSize);
		
		//查询条件 名称模糊查询  审核状态 发布状态
		LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
		String courseName = courseQueryParamsDto.getCourseName();
		String auditStatus = courseQueryParamsDto.getAuditStatus();
		String status = courseQueryParamsDto.getPublishStatus();
		//条件,列,值
		wrapper.like(StringUtils.isNotEmpty(courseName), CourseBase::getName, courseName);
		wrapper.eq(StringUtils.isNotEmpty(auditStatus), CourseBase::getAuditStatus, auditStatus);
		wrapper.eq(StringUtils.isNotEmpty(status), CourseBase::getStatus, status);
		
		//返回
		Page<CourseBase> selectPage = courseBaseMapper.selectPage(page, wrapper);
		return new PageResult<>(selectPage.getRecords(), selectPage.getTotal(), pageNo, pageSize);
	}
	
	@Override
	public CourseBaseInfoDto add(AddCourseDto dto) {
		//参数校验
		XcUser user = userInfoService.getCurrentUser();
		if (user == null) {
			CustomException.cast(CommonError.UNAUTHORIZED);
		}
		if (dto == null) {
			CustomException.cast(CommonError.REQUEST_NULL);
		}
		Long companyId = Long.valueOf(user.getCompanyId());
		//组装课程基本信息插入 其余信息页面填写传入
		CourseBase courseBase = new CourseBase();
		BeanUtils.copyProperties(dto, courseBase);
		courseBase.setCompanyId(companyId);
		courseBase.setCreateDate(LocalDateTime.now());
		courseBase.setCreatePeople(user.getName());
		courseBase.setAuditStatus("202002");
		courseBase.setStatus("203001");
		int countCourseBase = courseBaseMapper.insert(courseBase);
		if (countCourseBase <= 0) {
			CustomException.cast("课程基本信息插入失败");
		}
		//组装课程营销信息插入 
		CourseMarket courseMarket = new CourseMarket();
		BeanUtils.copyProperties(dto, courseMarket);
		courseMarket.setId(courseBase.getId());
		boolean b = updateCourseMarket(courseMarket);
		if (!b) {
			CustomException.cast("课程营销信息插入失败");
		}
		
		return get(courseBase.getId());
	}
	
	@Override
	public CourseBaseInfoDto get(Long id) {
		if (id == null || id <= 0) {
			CustomException.cast(CommonError.REQUEST_NULL);
		}
		CourseBaseInfoDto dto = new CourseBaseInfoDto();
		CourseBase courseBase = courseBaseMapper.selectById(id);
		CourseMarket courseMarket = courseMarketMapper.selectById(id);
		if (courseBase == null || courseMarket == null) {
			CustomException.cast("课程信息不存在");
		}
		BeanUtils.copyProperties(courseBase, dto);
		BeanUtils.copyProperties(courseMarket, dto);
		
		//根据分类id填充分类名称
		String mt = courseBase.getMt();
		String st = courseBase.getSt();
		CourseCategory categoryMt = courseCategoryMapper.selectById(mt);
		CourseCategory categorySt = courseCategoryMapper.selectById(st);
		if (categoryMt != null) {
			dto.setMtName(categoryMt.getName());
		}
		if (categorySt != null) {
			dto.setStName(categorySt.getName());
		}
		return dto;
	}
	
	@Transactional
	@Override
	public CourseBaseInfoDto update(UpdateCourseDto dto) {
		XcUser user = userInfoService.getCurrentUser();
		if (user == null) {
			CustomException.cast(CommonError.UNAUTHORIZED);
		}
		if (dto == null) {
			CustomException.cast(CommonError.REQUEST_NULL);
		}
		Long courseId = dto.getId();
		
		//更新课程信息
		CourseBase courseBase = courseBaseMapper.selectById(courseId);
		if (courseBase == null) {
			CustomException.cast("课程不存在");
		}
		if (!courseBase.getCompanyId().toString().equals(user.getCompanyId())) {
			CustomException.cast("只能修改本机构的课程");
		}
		BeanUtils.copyProperties(dto, courseBase);
		courseBase.setChangeDate(LocalDateTime.now());
		courseBase.setChangePeople(user.getName());
		int countCourseBase = courseBaseMapper.updateById(courseBase);
		if (countCourseBase == 0) {
			CustomException.cast("课程更新失败");
		}
		
		CourseMarket courseMarket = new CourseMarket();
		BeanUtils.copyProperties(dto, courseMarket);
		boolean b = updateCourseMarket(courseMarket);
		if (!b) {
			CustomException.cast("课程营销信息更新失败");
		}
		
		return get(courseId);
	}
	
	/**
	 * 更新营销信息
	 */
	private boolean updateCourseMarket(CourseMarket courseMarket) {
		if ("201001".equals(courseMarket.getCharge()) && (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0)) {
			CustomException.cast("收费课程价格不能为空");
		}
		boolean success = courseMarketService.saveOrUpdate(courseMarket);
		if (!success) {
			CustomException.cast("课程营销信息更新失败");
		}
		return true;
	}
	
}
