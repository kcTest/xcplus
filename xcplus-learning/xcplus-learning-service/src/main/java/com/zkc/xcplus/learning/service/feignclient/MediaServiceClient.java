package com.zkc.xcplus.learning.service.feignclient;

import com.zkc.xcplus.base.model.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理服务远程接口
 */
@FeignClient(value = "media-api", fallbackFactory = ContentServiceClientFallbackFactory.class)
public interface MediaServiceClient {
	
	@ResponseBody
	@GetMapping("/open/preview/{mediaId}")
	RestResponse<String> getPlayUrlByMediaId(@PathVariable("mediaId") String mediaId);
}
