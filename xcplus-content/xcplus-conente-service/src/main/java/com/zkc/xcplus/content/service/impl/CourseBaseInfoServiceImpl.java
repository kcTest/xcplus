package com.zkc.xcplus.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.CourseQueryParamsDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import com.zkc.xcplus.content.service.dao.CourseBaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
	
	@Autowired
	private CourseBaseMapper courseBaseMapper;
	
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
		wrapper.like(StringUtils.hasText(courseName), CourseBase::getName, courseName);
		wrapper.eq(StringUtils.hasText(auditStatus), CourseBase::getAuditStatus, auditStatus);
		wrapper.eq(StringUtils.hasText(status), CourseBase::getStatus, status);
		
		//返回
		Page<CourseBase> selectPage = courseBaseMapper.selectPage(page, wrapper);
		return new PageResult<>(selectPage.getRecords(), selectPage.getTotal(), pageNo, pageSize);
	}
}
