package com.zkc.xcplus.content.service.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * cause拿到熔断异常
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
	
	@Override
	public MediaServiceClient create(Throwable cause) {
		
		return new MediaServiceClient() {
			//降级方法
			@Override
			public String uploadHtml(MultipartFile file, String objectName) throws IOException {
				log.debug("远程调用媒资服务上传静态页面文件发生熔断", cause);
				return null;
			}
		};
		
	}
}
