package com.zkc.xcplus.learning.service.feignclient;

import com.zkc.xcplus.base.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
	
	@Override
	public MediaServiceClient create(Throwable cause) {
		return new MediaServiceClient() {
			@Override
			public RestResponse<String> getPlayUrlByMediaId(String courseId) {
				log.error("调用媒资服务发生熔断：{}", cause.toString(), cause);
				return null;
			}
		};
	}
}
