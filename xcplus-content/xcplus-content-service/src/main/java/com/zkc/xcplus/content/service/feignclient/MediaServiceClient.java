package com.zkc.xcplus.content.service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * value 媒资服务  path 路径前缀
 * api项目中启动类指定FeignClient所在包
 */
@FeignClient(value = "media-api", path = "media", fallbackFactory = MediaServiceClientFallbackFactory.class)
public interface MediaServiceClient {
	
	/**
	 * 上传静态页面文件
	 *
	 * @param file       文件
	 * @param objectName 存放目录
	 * @return 文件信息
	 */
	@PostMapping(value = "/files/upload/html", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	String uploadHtml(@RequestPart("file") MultipartFile file,
					  @RequestParam(value = "objectName", required = false) String objectName) throws IOException;
}
