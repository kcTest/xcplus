package com.zkc.xcplus.content.service.feignclient;

import com.zkc.xcplus.content.service.po.CourseIndexInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * cause拿到熔断异常
 */
@Slf4j
@Component
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {
	
	@Override
	public SearchServiceClient create(Throwable cause) {
		
		return new SearchServiceClient() {
			//降级方法
			@Override
			public boolean addCourseIdx(CourseIndexInfo courseIndexInfo) throws IOException {
				log.debug("远程调用搜索服务上传课程索引发生熔断", cause);
				return false;
			}
		};
		
	}
}
