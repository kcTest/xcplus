package com.zkc.xcplus.media.api.controller;

import com.zkc.xcplus.base.model.RestResponse;
import com.zkc.xcplus.media.model.dto.UploadFileParamsDto;
import com.zkc.xcplus.media.service.MediaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "BigFilesController", description = "大文件分块上传接口")
@RestController
@RequestMapping("/bigfile")
public class BigFilesController {
	
	@Autowired
	private MediaFileService mediaFileService;
	
	@Operation(summary = "文件上传前检查文件")
	@PostMapping("/checkfile")
	public RestResponse<Boolean> checkfile(@RequestParam("fileMd5") String fileMd5) {
		return mediaFileService.checkFile(fileMd5);
		
	}
	
	@Operation(summary = "分块文件上传前的检测")
	@PostMapping("/checkchunk")
	public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunkIdx") int chunkIdx) {
		return mediaFileService.checkChunk(fileMd5, chunkIdx);
	}
	
	@Operation(summary = "上传分块文件")
	@PostMapping("/uploadchunk")
	public RestResponse<Boolean> uploadchunk(@RequestParam("file") MultipartFile file,
											 @RequestParam("fileMd5") String fileMd5,
											 @RequestParam("chunkIdx") int chunkIdx) throws Exception {
		return mediaFileService.uploadChunk(fileMd5, chunkIdx, file.getBytes());
		
	}
	
	@Operation(summary = "合并文件")
	@PostMapping("/mergechunks")
	public RestResponse<Boolean> mergechunks(@RequestParam("fileMd5") String fileMd5,
											 @RequestParam("fileName") String fileName,
											 @RequestParam("chunkTotal") int chunkTotal) {
		//TODO 获取机构id
		Long companyId = 1L;
		UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
		uploadFileParamsDto.setFilename(fileName);
		//视频
		uploadFileParamsDto.setFileType("001002");
		uploadFileParamsDto.setTags("课程视频");
		return mediaFileService.mergeChunks(companyId, fileMd5, chunkTotal, uploadFileParamsDto);
		
	}
	
	
}
