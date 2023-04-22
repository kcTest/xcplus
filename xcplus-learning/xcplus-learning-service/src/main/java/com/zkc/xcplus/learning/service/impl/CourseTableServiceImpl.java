package com.zkc.xcplus.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.po.CoursePublish;
import com.zkc.xcplus.content.model.po.XcUser;
import com.zkc.xcplus.learning.model.dto.MyCourseTablesQueryParams;
import com.zkc.xcplus.learning.model.dto.XcChooseCourseDto;
import com.zkc.xcplus.learning.model.dto.XcCourseTablesDto;
import com.zkc.xcplus.learning.model.po.XcChooseCourse;
import com.zkc.xcplus.learning.model.po.XcCourseTables;
import com.zkc.xcplus.learning.service.CourseTableService;
import com.zkc.xcplus.learning.service.UserInfoService;
import com.zkc.xcplus.learning.service.dao.XcChooseCourseMapper;
import com.zkc.xcplus.learning.service.dao.XcCourseTablesMapper;
import com.zkc.xcplus.learning.service.feignclient.ContentServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 选课相关接口实现
 */
@Service
@Slf4j
public class CourseTableServiceImpl implements CourseTableService {
	
	@Autowired
	private ContentServiceClient contentServiceClient;
	
	@Autowired
	private XcChooseCourseMapper chooseCourseMapper;
	
	@Autowired
	private XcCourseTablesMapper courseTablesMapper;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Transactional
	@Override
	public XcChooseCourseDto addChooseCourse(Long courseId) {
		XcUser currentUser = userInfoService.getCurrentUser();
		String userId = currentUser.getId();
		//远程调用内容服务查询课程的收费规则
		CoursePublish coursePublish = contentServiceClient.getCoursePublish(courseId);
		if (coursePublish == null) {
			CustomException.cast("课程不存在");
		}
		//收费规则
		String charge = coursePublish.getCharge();
		//选课记录
		XcChooseCourse xcChooseCourse;
		//免费课程  生成选课记录选课记录 生成我的课程表
		if ("201000".equals(charge)) {
			xcChooseCourse = addFreeCourse(userId, coursePublish);
			addCourseTable(xcChooseCourse);
		} else {
			//收费课程 生成选课记录 
			xcChooseCourse = addChargeCourse(userId, coursePublish);
		}
		
		//获取学生的学习资格
		XcCourseTablesDto xcCourseTablesDto = getLearningStatus(courseId);
		String learningStatus = xcCourseTablesDto.getLearningStatus();
		
		//构造返回结果
		XcChooseCourseDto result = new XcChooseCourseDto();
		BeanUtils.copyProperties(xcChooseCourse, result);
		result.setLearningStatus(learningStatus);
		
		return result;
	}
	
	/**
	 * 免费课程生成记录表
	 *
	 * @param userId        用户id
	 * @param coursePublish 课程收费信息
	 * @return 选课记录表
	 */
	private XcChooseCourse addFreeCourse(String userId, CoursePublish coursePublish) {
		Long courseId = coursePublish.getId();
		//如果用户已经存在该免费课程的选课记录并且选课状态为成功 直接返回选课记录
		LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcChooseCourse::getUserId, userId);
		queryWrapper.eq(XcChooseCourse::getCourseId, courseId);
		queryWrapper.eq(XcChooseCourse::getOrderType, "700001");
		queryWrapper.eq(XcChooseCourse::getStatus, "701001");
		List<XcChooseCourse> chooseCourses = chooseCourseMapper.selectList(queryWrapper);
		if (chooseCourses.size() > 0) {
			return chooseCourses.get(0);
		}
		
		//否则生成选课记录
		XcChooseCourse chooseCourse = new XcChooseCourse();
		chooseCourse.setCourseId(courseId);
		chooseCourse.setCourseName(coursePublish.getName());
		chooseCourse.setUserId(userId);
		chooseCourse.setCompanyId(coursePublish.getCompanyId());
		chooseCourse.setOrderType("700001");
		chooseCourse.setCreateDate(LocalDateTime.now());
		chooseCourse.setCoursePrice(coursePublish.getPrice());
		chooseCourse.setValidDays(365);
		chooseCourse.setStatus("701001");
		chooseCourse.setValidtimeStart(LocalDateTime.now());
		chooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));
		int count = chooseCourseMapper.insert(chooseCourse);
		if (count <= 0) {
			CustomException.cast("添加选课记录失败");
		}
		return chooseCourse;
	}
	
	/**
	 * 收费课程生成记录表
	 *
	 * @param userId        用户id
	 * @param coursePublish 课程收费信息
	 * @return 选课记录表
	 */
	private XcChooseCourse addChargeCourse(String userId, CoursePublish coursePublish) {
		Long courseId = coursePublish.getId();
		//如果用户已经存在该收费课程的选课记录并且支付状态为待支付 直接返回选课记录
		LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcChooseCourse::getUserId, userId);
		queryWrapper.eq(XcChooseCourse::getCourseId, courseId);
		queryWrapper.eq(XcChooseCourse::getOrderType, "700002");
		queryWrapper.eq(XcChooseCourse::getStatus, "701002");
		List<XcChooseCourse> chooseCourses = chooseCourseMapper.selectList(queryWrapper);
		if (chooseCourses.size() > 0) {
			return chooseCourses.get(0);
		}
		
		//否则生成选课记录
		XcChooseCourse chooseCourse = new XcChooseCourse();
		chooseCourse.setCourseId(courseId);
		chooseCourse.setCourseName(coursePublish.getName());
		chooseCourse.setUserId(userId);
		chooseCourse.setCompanyId(coursePublish.getCompanyId());
		chooseCourse.setOrderType("700002");
		chooseCourse.setCreateDate(LocalDateTime.now());
		chooseCourse.setCoursePrice(coursePublish.getPrice());
		chooseCourse.setValidDays(365);
		chooseCourse.setStatus("701002");
		chooseCourse.setValidtimeStart(LocalDateTime.now());
		chooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));
		int count = chooseCourseMapper.insert(chooseCourse);
		if (count <= 0) {
			CustomException.cast("添加选课记录失败");
		}
		return chooseCourse;
	}
	
	private XcCourseTables addCourseTable(XcChooseCourse chooseCourse) {
		String staus = chooseCourse.getStatus();
		if (!"701001".equals(staus)) {
			CustomException.cast("选课没有成功无法生成课程表");
		}
		
		//已存在直接返回
		XcCourseTables courseTables = getXcCourseTables(chooseCourse.getUserId(), chooseCourse.getCourseId());
		if (courseTables != null) {
			return courseTables;
		}
		
		courseTables = new XcCourseTables();
		BeanUtils.copyProperties(chooseCourse, courseTables);
		courseTables.setChooseCourseId(chooseCourse.getId());
		courseTables.setCourseType(chooseCourse.getOrderType());
		courseTables.setUpdateDate(LocalDateTime.now());
		int count = courseTablesMapper.insert(courseTables);
		if (count <= 0) {
			CustomException.cast("生成我的课程表失败");
		}
		
		return courseTables;
	}
	
	@Override
	public XcCourseTablesDto getLearningStatus(Long courseId) {
		XcUser currentUser = userInfoService.getCurrentUser();
		String userId = currentUser.getId();
		//返回结果
		XcCourseTablesDto courseTablesDto = new XcCourseTablesDto();
		//查询我的课程表 
		XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
		if (xcCourseTables == null) {
			//没有选课设置相应状态直接返回
			courseTablesDto.setLearningStatus("702002");
			return courseTablesDto;
		}
		
		//否则判断是否过期 过期不能继续学习
		boolean before = xcCourseTables.getValidtimeEnd().isBefore(LocalDateTime.now());
		BeanUtils.copyProperties(xcCourseTables, courseTablesDto);
		if (before) {
			//过期需要续期
			courseTablesDto.setLearningStatus("702003");
			return courseTablesDto;
		}
		//可以正常学习
		courseTablesDto.setLearningStatus("702001");
		return courseTablesDto;
	}
	
	private XcCourseTables getXcCourseTables(String userId, Long courseId) {
		LambdaQueryWrapper<XcCourseTables> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcCourseTables::getUserId, userId);
		queryWrapper.eq(XcCourseTables::getCourseId, courseId);
		return courseTablesMapper.selectOne(queryWrapper);
	}
	
	@Override
	public boolean saveChooseCourseSuccess(String chooseCourseId) {
		XcChooseCourse chooseCourse = chooseCourseMapper.selectById(chooseCourseId);
		if (chooseCourse == null) {
			log.debug("未找到选课记录,id:{}", chooseCourseId);
			return false;
		}
		String status = chooseCourse.getStatus();
		if ("701002".equals(status)) {
			chooseCourse.setStatus("701001");
			int count = chooseCourseMapper.updateById(chooseCourse);
			if (count <= 0) {
				log.debug("更新选课成功状态失败:{}", chooseCourse);
				CustomException.cast("更新选课成功状态失败");
			}
		}
		//生成我的课程表
		addCourseTable(chooseCourse);
		return true;
	}
	
	@Override
	public PageResult<XcCourseTables> myCourseTables(MyCourseTablesQueryParams params) {
		String userId = params.getUserId();
		int pageNo = params.getPageNo();
		int pageSize = params.getPageSize();
		
		Page<XcCourseTables> page = new Page<>(pageNo, pageSize);
		LambdaQueryWrapper<XcCourseTables> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcCourseTables::getUserId, userId);
		Page<XcCourseTables> courseTablesPage = courseTablesMapper.selectPage(page, queryWrapper);
		
		//构造返回结果
		List<XcCourseTables> records = courseTablesPage.getRecords();
		long total = courseTablesPage.getTotal();
		return new PageResult<>(records, total, pageNo, pageSize);
	}
	
}
