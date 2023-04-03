package com.zkc.xcplus.content.service.feignclient;

import com.zkc.xcplus.content.service.po.CourseIndexInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

/**
 * value 搜索服务  path 路径前缀
 * api项目中启动类指定FeignClient所在包
 */
@FeignClient(value = "search-api", path = "search", fallbackFactory = SearchServiceClientFallbackFactory.class)
public interface SearchServiceClient {
	
	/**
	 * 保存课程索引
	 *
	 * @param courseIndexInfo 课程索引
	 * @return 索引保存是否成功
	 */
	@PostMapping(value = "/course/idx/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean addCourseIdx(@RequestBody CourseIndexInfo courseIndexInfo) throws IOException;
}
