package com.zkc.xcplus.media.api.controller;

import com.zkc.xcplus.base.model.RestResponse;
import com.zkc.xcplus.media.model.po.MediaFiles;
import com.zkc.xcplus.media.service.MediaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MediaOpenController", description = "公开接口 用于获取课程媒资信息")
@RestController
@RequestMapping("/open")
public class MediaOpenController {
	
	@Autowired
	private MediaFileService mediaFileService;
	
	@Operation(summary = "根据媒资id获取媒资的预览地址")
	@GetMapping("/preview/{mediaId}")
	public RestResponse<String> getUrlByMediaId(@PathVariable("mediaId") String mediaId) {
		MediaFiles media = mediaFileService.getMediaById(mediaId);
		if (media == null) {
			return RestResponse.validfail("该视频不存在");
		}
		String url = media.getUrl();
		if (!StringUtils.hasText(url)) {
			return RestResponse.validfail("该视频正在处理");
		}
		return RestResponse.success(url);
	}
	
}
