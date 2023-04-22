package com.zkc.xcplus.learning.service.feignclient;

import com.zkc.xcplus.content.model.po.CoursePublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContentServiceClientFallbackFactory implements FallbackFactory<ContentServiceClient> {
	
	@Override
	public ContentServiceClient create(Throwable cause) {
		return new ContentServiceClient() {
			@Override
			public CoursePublish getCoursePublish(Long courseId) {
				log.error("调用内容管理服务发生熔断：{}", cause.toString(), cause);
				return null;
			}
		};
	}
}
