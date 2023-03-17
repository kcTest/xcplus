package com.zkc.xcplus.media.service;

import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.base.model.RestResponse;
import com.zkc.xcplus.media.model.dto.QueryMediaFilesDto;
import com.zkc.xcplus.media.model.dto.UploadFileParamsDto;
import com.zkc.xcplus.media.model.dto.UploadFileResultDto;
import com.zkc.xcplus.media.model.po.MediaFiles;

import java.io.File;

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
	
	/**
	 * 上传文件到分布式文件系统
	 *
	 * @param bucketName  桶名称
	 * @param contentType mime类型
	 * @param fileData    文件字节数组
	 * @param savePath    存储路径
	 */
	void addFileToMinio(String bucketName, String contentType, byte[] fileData, String savePath);
	
	/**
	 * 上传本地文件到分布式文件系统
	 */
	boolean addLocalFileToMinio(String bucketName, String contentType, String sourcePath, String savePath);
	
	/**
	 * 添加文件信息到数据库
	 *
	 * @param companyId   机构id
	 * @param dto         请求参数
	 * @param savePath    保存路径
	 * @param oriFileName 文件名称
	 * @param md5Id       文件md5值
	 * @return 文件信息
	 */
	MediaFiles addFileDbInfo(Long companyId, UploadFileParamsDto dto, String savePath, String oriFileName, String md5Id);
	
	/**
	 * 检查文件是否存在 数据库及minio
	 *
	 * @param fileMd5 文件的md5
	 * @return 是否存在
	 */
	RestResponse<Boolean> checkFile(String fileMd5);
	
	/**
	 * 检查分块文件
	 *
	 * @param fileMd5  文件的md5
	 * @param chunkIdx 分块文件序号
	 * @return 是否存在
	 */
	RestResponse<Boolean> checkChunk(String fileMd5, int chunkIdx);
	
	/**
	 * 上传分块文件
	 *
	 * @param fileMd5  文件的md5
	 * @param chunkIdx 分块文件序号
	 * @param bytes    文件字节数组
	 * @return 上传是否成功
	 */
	RestResponse<Boolean> uploadChunk(String fileMd5, int chunkIdx, byte[] bytes);
	
	/**
	 * 调用minio sdk 合并已存储的分块文件
	 *
	 * @param companyId           机构id
	 * @param fileMd5             文件MD5值
	 * @param chunkTotal          分块总数
	 * @param uploadFileParamsDto 请求参数 包含文件信息
	 * @return 合并是否成功
	 */
	RestResponse<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto);
	
	
	/**
	 * 从MINIO下载视频
	 *
	 * @param bucket     桶
	 * @param objectName 路径
	 * @return 文件
	 */
	File downloadFileFromMinio(String bucket, String objectName);
	
	/**
	 * 根据媒资ID获取媒资URL
	 * @param mediaId 媒资ID
	 * @return 媒资信息
	 */
	MediaFiles getMediaById(String mediaId);
}
