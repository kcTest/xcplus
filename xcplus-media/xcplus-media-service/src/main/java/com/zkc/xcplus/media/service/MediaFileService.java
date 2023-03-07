package com.zkc.xcplus.media.service;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.media.model.dto.QueryMediaFilesDto;
import com.zkc.xcplus.media.model.dto.UploadFileParamsDto;
import com.zkc.xcplus.media.model.dto.UploadFileResultDto;
import com.zkc.xcplus.media.model.po.MediaFiles;

public interface MediaFileService {
	
	/**
	 * 获取媒资列表
	 *
	 * @param companyId  机构id
	 * @param pageParams 分页参数
	 * @param dto        请求参数
	 * @return 媒资文件列表
	 */
	PageResult<MediaFiles> queryMediaFilesDto(Long companyId, PageParams pageParams, QueryMediaFilesDto dto);
	
	/**
	 * @param companyId  机构id
	 * @param dto        文件信息
	 * @param fileData   文件内容
	 * @param saveFolder 存储根目录
	 * @param savePath   存储路径
	 * @return 媒资信息
	 */
	UploadFileResultDto upload(Long companyId, UploadFileParamsDto dto, byte[] fileData, String saveFolder, String savePath);
	
	void delete(String objectName);
}
