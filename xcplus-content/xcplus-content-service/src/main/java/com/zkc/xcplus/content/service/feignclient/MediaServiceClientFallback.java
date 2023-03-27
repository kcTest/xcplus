package com.zkc.xcplus.content.service.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * fallback = 无法拿到熔断异常
 */
@Component
@Slf4j
public class MediaServiceClientFallback implements MediaServiceClient {
	
	@Override
	public String uploadHtml(MultipartFile file, String objectName) throws IOException {
		log.debug("远程调用媒资服务上传静态页面文件发生熔断");
		return null;
	}
}
