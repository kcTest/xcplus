package com.zkc.xcplus.learning.service.feignclient;

import com.zkc.xcplus.content.model.po.CoursePublish;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理服务远程接口
 */
@FeignClient(value = "content-api", path = "/content", fallbackFactory = ContentServiceClientFallbackFactory.class)
public interface ContentServiceClient {
	
	@ResponseBody
	@GetMapping("/coursepublish/{courseId}")
	CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId);
}
