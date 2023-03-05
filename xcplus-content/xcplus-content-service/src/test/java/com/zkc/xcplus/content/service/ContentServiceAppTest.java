package com.zkc.xcplus.content.service;

import com.zkc.xcplus.content.model.po.CourseBase;
import com.zkc.xcplus.content.service.dao.CourseBaseMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContentServiceAppTest {
	
	@Autowired
	private CourseBaseMapper courseBaseMapper;
	
	@Test
	void testVourseBaseMapper() {
		CourseBase base = courseBaseMapper.selectById(1);
		Assertions.assertNotNull(base);
	}
}
