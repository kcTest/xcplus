package com.zkc.xcplus.media.api.controller;

import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.media.model.dto.QueryMediaFilesDto;
import com.zkc.xcplus.media.model.dto.UploadFileParamsDto;
import com.zkc.xcplus.media.model.dto.UploadFileResultDto;
import com.zkc.xcplus.media.model.po.MediaFiles;
import com.zkc.xcplus.media.service.MediaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Tag(name = "MediaFileController", description = "媒资管理")
@RestController
@RequestMapping("/files")
public class MediaFileController {
	
	@Autowired
	private MediaFileService mediaFileService;
	
	@Operation(summary = "获取媒资列表")
	@PostMapping("/list")
	public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaFilesDto dto) {
		//TODO 获取机构名称
		Long companyId = 1L;
		return mediaFileService.queryMediaFilesDto(companyId, pageParams, dto);
	}
	
	@Operation(summary = "上传文件")
	@Parameters(
			{@Parameter(name = "file", description = "文件内容", required = true),
					@Parameter(name = "saveFolder", description = "服务器存储根目录"),
					@Parameter(name = "savePath", description = "服务器存储路径")
			}
	)
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadFileResultDto upload(@RequestPart("file") MultipartFile file,
									  @RequestParam(value = "saveFolder", required = false) String saveFolder,
									  @RequestParam(value = "savePath", required = false) String savePath) {
		//TODO 获取机构名称
		Long companyId = 1L;
		UploadFileParamsDto paramsDto = new UploadFileParamsDto();
		paramsDto.setFilename(file.getOriginalFilename());
		String contentType = file.getContentType();
		paramsDto.setContentType(contentType);
		paramsDto.setFileSize(file.getSize());
		if (contentType != null && contentType.contains("image")) {
			paramsDto.setFileType("001001");
		} else {
			paramsDto.setFileType("001003");
		}
		byte[] fileBytes = null;
		try {
			fileBytes = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			CustomException.cast("获取文件内容失败");
		}
		return mediaFileService.upload(companyId, paramsDto, fileBytes, saveFolder, savePath);
	}
	
	@PostMapping(value = "/upload/html", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadFileResultDto uploadHtmlFile(@RequestPart("file") MultipartFile file,
											  @RequestParam(value = "objectName", required = false) String objectName) throws IOException {
		//TODO 获取机构名称
		Long companyId = 1L;
		UploadFileParamsDto paramsDto = new UploadFileParamsDto();
		paramsDto.setFilename(file.getOriginalFilename());
		paramsDto.setContentType(file.getContentType());
		paramsDto.setFileSize(file.getSize());
		paramsDto.setFileType("001001");
		File temp = File.createTempFile("minio", ".temp");
		file.transferTo(temp);
		String localFilePath = temp.getAbsolutePath();
		return mediaFileService.uploadHtmlFile(companyId, paramsDto, localFilePath, objectName);
	}
	
}
