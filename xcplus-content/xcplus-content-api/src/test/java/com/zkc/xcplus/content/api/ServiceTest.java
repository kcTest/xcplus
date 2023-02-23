package com.zkc.xcplus.content.api;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.content.model.dto.CourseQueryParamsDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import com.zkc.xcplus.content.service.dao.CourseBaseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
	
	@Autowired
	private CourseBaseMapper courseBaseMapper;
	
	@Autowired
	private CourseBaseInfoService courseBaseInfoService;
	
	@Test
	public void courseBaseMapperTest() {
		
		CourseBase courseBase = courseBaseMapper.selectById(2);
		System.out.println(courseBase);
	}
	
	@Test
	public void courseBaseServiceTest() {
		PageParams pageParams = new PageParams();
		pageParams.setPageNo(1L);
		pageParams.setPageSize(10L);
		PageResult<CourseBase> pageResult = courseBaseInfoService.courseBaseList(pageParams, new CourseQueryParamsDto());
		for (CourseBase item : pageResult.getItems()) {
			System.out.println(item);
		}
	}
}
