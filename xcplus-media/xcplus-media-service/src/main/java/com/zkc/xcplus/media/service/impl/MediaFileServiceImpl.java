package com.zkc.xcplus.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.media.model.dto.QueryMediaFilesDto;
import com.zkc.xcplus.media.model.dto.UploadFileParamsDto;
import com.zkc.xcplus.media.model.dto.UploadFileResultDto;
import com.zkc.xcplus.media.model.po.MediaFiles;
import com.zkc.xcplus.media.service.MediaFileService;
import com.zkc.xcplus.media.service.dao.MediaFilesMapper;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {
	
	@Value("${minio.bucket}")
	private String bucket;
	
	@Autowired
	private MinioClient client;
	
	@Autowired
	private MediaFilesMapper mediaFilesMapper;
	
	@Override
	public PageResult<MediaFiles> queryMediaFilesDto(Long companyId, PageParams pageParams, QueryMediaFilesDto dto) {
		LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(StringUtils.hasText(dto.getFilename()), MediaFiles::getFilename, dto.getFilename());
		queryWrapper.eq(StringUtils.hasText(dto.getFileType()), MediaFiles::getFileType, dto.getFileType());
		queryWrapper.eq(StringUtils.hasText(dto.getAuditStatus()), MediaFiles::getAuditStatus, dto.getAuditStatus());
		queryWrapper.eq(MediaFiles::getCompanyId, companyId);
		Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
		Page<MediaFiles> selectPage = mediaFilesMapper.selectPage(page, queryWrapper);
		//记录列表 总条数 当前页 每页条数
		return new PageResult<>(selectPage.getRecords(), selectPage.getTotal(), selectPage.getCurrent(), selectPage.getSize());
	}
	
	//1.	@Transactional
	@Override
	public UploadFileResultDto upload(Long companyId, UploadFileParamsDto dto, byte[] fileData, String saveFolder, String savePath) {
		try {
			boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
			if (!exists) {
				throw new Exception("存储桶不存在");
			}
			//设置默认路径
			if (!StringUtils.hasText(saveFolder)) {
				saveFolder = getFileFolder(new Date(), true, true, true);
			} else if (!saveFolder.contains("/")) {
				saveFolder += "/";
			}
			String oriFileName = dto.getFilename();
			String md5Id = DigestUtils.md5DigestAsHex(fileData);
			if (!StringUtils.hasText(savePath)) {
				//md5作为保存后的文件名 
				savePath = md5Id + oriFileName.substring(oriFileName.lastIndexOf("."));
			}
			//共同作为服务器上的存储路径objectname 
			savePath = saveFolder + savePath;
			
			addFileToMinio(dto, fileData, savePath);
			//1.
			//			MediaFiles mediaFiles = addFileDbInfo(companyId, dto, savePath, oriFileName, md5Id);
			//2.
			MediaFiles mediaFiles = currentProxy.addFileDbInfo(companyId, dto, savePath, oriFileName, md5Id);
			
			//不为空的情况 只更新了文件， 其余信息直接查询返回  
			UploadFileResultDto resultDto = new UploadFileResultDto();
			BeanUtils.copyProperties(mediaFiles, resultDto);
			return resultDto;
		} catch (Exception e) {
			CustomException.cast("上传文件失败: " + e.getMessage());
			log.error(e.toString());
		}
		return null;
	}
	
	/**
	 * 2.
	 */
	@Autowired
	@Lazy
	private MediaFileService currentProxy;
	
	/**
	 * 事务控制生效
	 * 【方法抛出异常+方法被代理对象调用+方法加@Transactional】
	 * 1.在upload方法加@Transactional。
	 * 2.本方法+@Transactional ，使用@Autowired声明当前serviced对象作为字段，该字段成为代理对象。upload方法中使用currentProxy调用addFileDbInfo
	 * upload方法包含上传文件操作 事务占用时间长， 使用1
	 */
	//2.
	@Transactional(rollbackFor = Exception.class)
	@Override
	public MediaFiles addFileDbInfo(Long companyId, UploadFileParamsDto dto, String savePath, String oriFileName, String md5Id) {
		//数据库信息保存
		MediaFiles mediaFiles = mediaFilesMapper.selectById(md5Id);
		//新增媒资信息
		if (mediaFiles == null) {
			mediaFiles = new MediaFiles();
			BeanUtils.copyProperties(dto, mediaFiles);
			//MD5作为主键和文件id
			mediaFiles.setId(md5Id);
			mediaFiles.setFileId(md5Id);
			mediaFiles.setCompanyId(companyId);
			mediaFiles.setFilename(oriFileName);
			mediaFiles.setBucket(bucket);
			mediaFiles.setFilePath(savePath);
			mediaFiles.setUrl("/" + bucket + "/" + savePath);
			mediaFiles.setCreateDate(LocalDateTime.now());
			mediaFiles.setStatus("1");
			//默认未审核
			mediaFiles.setAuditStatus("002002");
			mediaFilesMapper.insert(mediaFiles);
		} else {
			mediaFiles.setFilePath(savePath);
			mediaFiles.setUrl("/" + bucket + "/" + savePath);
			mediaFiles.setChangeDate(LocalDateTime.now());
			mediaFilesMapper.updateById(mediaFiles);
		}
//		int a = 1 / 0;
		return mediaFiles;
	}
	
	@Override
	public void addFileToMinio(UploadFileParamsDto dto, byte[] fileData, String savePath) {
		try {
			//上传minio
			ByteArrayInputStream in = new ByteArrayInputStream(fileData);
			PutObjectArgs objectArgs = PutObjectArgs.builder()
					.bucket(bucket)
					.object(savePath)
					.contentType(dto.getContentType())
					.stream(in, in.available(), -1)
					.build();
			client.putObject(objectArgs);
		} catch (Exception e) {
			e.printStackTrace();
			CustomException.cast("上传文件到文件系统失败: " + e.getMessage());
		}
	}
	
	private String getFileFolder(Date date, boolean year, boolean month, boolean day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		String[] dateStrArr = dateStr.split("-");
		StringBuffer folderStr = new StringBuffer();
		if (year) {
			folderStr.append(dateStrArr[0]).append("/");
		}
		if (month) {
			folderStr.append(dateStrArr[1]).append("/");
		}
		if (day) {
			folderStr.append(dateStrArr[2]).append("/");
		}
		return folderStr.toString();
	}
	
}
