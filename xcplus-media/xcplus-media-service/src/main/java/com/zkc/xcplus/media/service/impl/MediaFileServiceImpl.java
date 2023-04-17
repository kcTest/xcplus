package com.zkc.xcplus.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.base.model.PageParams;
import com.zkc.xcplus.base.model.PageResult;
import com.zkc.xcplus.base.model.RestResponse;
import com.zkc.xcplus.media.model.dto.QueryMediaFilesDto;
import com.zkc.xcplus.media.model.dto.UploadFileParamsDto;
import com.zkc.xcplus.media.model.dto.UploadFileResultDto;
import com.zkc.xcplus.media.model.po.MediaFiles;
import com.zkc.xcplus.media.model.po.MediaProcess;
import com.zkc.xcplus.media.service.MediaFileService;
import com.zkc.xcplus.media.service.dao.MediaFilesMapper;
import com.zkc.xcplus.media.service.dao.MediaProcessHistoryMapper;
import com.zkc.xcplus.media.service.dao.MediaProcessMapper;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {
	
	@Value("${minio.bucket.default}")
	private String defaultBucket;
	
	@Value("${minio.bucket.bigfile}")
	private String bigFileBucket;
	
	@Value("${minio.bucket.html}")
	private String htmlBucket;
	
	@Autowired
	private MinioClient client;
	
	@Autowired
	private MediaFilesMapper mediaFilesMapper;
	
	@Autowired
	private MediaProcessMapper mediaProcessMapper;
	
	@Autowired
	private MediaProcessHistoryMapper mediaProcessHistoryMapper;
	
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
			boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(defaultBucket).build());
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
			//扩展名得到mimeType
			String extension = oriFileName.substring(oriFileName.lastIndexOf("."));
			String mimeType = getMimeTypeByExtension(extension);
			
			String md5Id = DigestUtils.md5DigestAsHex(fileData);
			if (!StringUtils.hasText(savePath)) {
				//md5作为保存后的文件名 
				savePath = md5Id + oriFileName.substring(oriFileName.lastIndexOf("."));
			}
			//共同作为服务器上的存储路径objectname 
			savePath = saveFolder + savePath;
			
			addFileToMinio(defaultBucket, mimeType, fileData, savePath);
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
//			log.error(e.toString());
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
	 * 2.本方法+@Transactional ，使用@Autowired声明当前serviced对象作为字段，该字段成为代理对象。upload方法中使用currentProxy调用addFileDbInfo（非事务方法内调用事务方法需使用代理对象）
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
			mediaFiles.setBucket(defaultBucket);
			mediaFiles.setFilePath(savePath);
			mediaFiles.setUrl("/" + defaultBucket + "/" + savePath);
			mediaFiles.setCreateDate(LocalDateTime.now());
			mediaFiles.setStatus("1");
			//默认未审核
			mediaFiles.setAuditStatus("002002");
			int row = mediaFilesMapper.insert(mediaFiles);
			//暂时相同视频只能插入一次
			if (row <= 0) {
				log.error("保存文件信息失败, bucket:{},objectName:{}", bigFileBucket, savePath);
				CustomException.cast("保存文件信息失败");
			}
		}

//		int a = 1 / 0;
		
		addTask(mediaFiles);
		
		return mediaFiles;
	}
	
	/**
	 * 检查格式添加待处理任务
	 *
	 * @param mediaFiles 文件信息
	 */
	private void addTask(MediaFiles mediaFiles) {
		String fileName = mediaFiles.getFilename();
		String extension = fileName.substring(fileName.lastIndexOf("."));
		String mimeType = getMimeTypeByExtension(extension);
		if ("video/x-msvideo".equals(mimeType)) {
			MediaProcess mediaProcess = new MediaProcess();
			BeanUtils.copyProperties(mediaFiles, mediaProcess);
			mediaProcess.setStatus("1");
			mediaProcess.setCreateDate(LocalDateTime.now());
			mediaProcess.setFailCount(0);
			mediaProcess.setUrl(null);
			mediaProcessMapper.insert(mediaProcess);
		}
	}
	
	/**
	 * 根据扩展名拿匹配的媒体类型
	 *
	 * @param extension 文件拓展名
	 * @return mimeType
	 */
	private String getMimeTypeByExtension(String extension) {
		//资源的媒体类型 默认未知二进制流
		String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		if (StringUtils.hasText(extension)) {
			ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
			if (extensionMatch != null) {
				contentType = extensionMatch.getMimeType();
			}
		}
		return contentType;
	}
	
	@Override
	public RestResponse<Boolean> checkFile(String fileMd5) {
		//在文件表中记录，并且在文件系统存在，此文件才存在
		MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
		if (mediaFiles == null) {
			return RestResponse.success(false);
		}
		//查看是否在文件系统存在
		GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(mediaFiles.getBucket()).object(mediaFiles.getFilePath()).build();
		try {
			InputStream inputStream = client.getObject(getObjectArgs);
			if (inputStream == null) {
				//文件不存在
				return RestResponse.success(false);
			}
		} catch (Exception e) {
			//文件不存在
			return RestResponse.success(false);
		}
		//文件已存在
		return RestResponse.success(true);
	}
	
	@Override
	public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIdx) {
		//得到分块文件所在目录
		String chunkFileFolder = getChunkFileFolder(fileMd5);
		//分块文件的完整路径
		String chunkFilePath = chunkFileFolder + chunkIdx;
		//查询文件系统分块文件是否存在
		//查看是否在文件系统存在
		GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bigFileBucket).object(chunkFilePath).build();
		try {
			InputStream inputStream = client.getObject(getObjectArgs);
			if (inputStream == null) {
				//文件不存在
				return RestResponse.success(false);
			}
		} catch (Exception e) {
			//文件不存在
			return RestResponse.success(false);
		}
		return RestResponse.success(true);
	}
	
	@Override
	public RestResponse<Boolean> uploadChunk(String fileMd5, int chunkIdx, byte[] bytes) {
		//得到分块文件所在目录
		String chunkFileFolderPath = getChunkFileFolder(fileMd5);
		//分块文件的路径
		String chunkFilePath = chunkFileFolderPath + chunkIdx;
		//获取mimeType
		String mimeType = getMimeTypeByExtension(null);
		try {
			//将分块上传到文件系统
			addFileToMinio(bigFileBucket, mimeType, bytes, chunkFilePath);
			//上传成功
			return RestResponse.success(true);
		} catch (Exception e) {
			log.error("上传分块失败: " + e);
			return RestResponse.validfail(false, "上传分块失败");
		}
	}
	
	@Override
	public RestResponse<Boolean> mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
		//分块文件所在目录
		String chunkFileFolderPath = getChunkFileFolder(fileMd5);
		//找到所有的分块文件
		List<ComposeSource> sources = Stream.iterate(0, i -> ++i).limit(chunkTotal).map(i -> ComposeSource.builder().bucket(bigFileBucket).object(chunkFileFolderPath + i).build()).collect(Collectors.toList());
		String oriFilename = uploadFileParamsDto.getFilename();
		String extension = oriFilename.substring(oriFilename.lastIndexOf("."));
		//合并后文件的objectname
		String savePath = getMergeFileFolder(fileMd5, extension);
		//指定合并后的objectName等信息
		ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
				.bucket(bigFileBucket)
				//合并后的文件的objectname
				.object(savePath)
				//指定源文件
				.sources(sources)
				.build();
		
		//===========合并文件============
		try {
			client.composeObject(composeObjectArgs);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("合并文件出错,bucket:{},objectName:{},错误信息:{}", bigFileBucket, savePath, e.getMessage());
			return RestResponse.validfail(false, "合并文件异常");
		}
		
		//===========校验合并后的和源文件是否一致，视频上传才成功===========
		//先下载合并后的文件
		File file = downloadFileFromMinio(bigFileBucket, savePath);
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			//计算合并后文件的md5
			String mergeFileMd5 = DigestUtils.md5DigestAsHex(fileInputStream);
			//比较原始md5和合并后文件的md5
			if (!fileMd5.equals(mergeFileMd5)) {
				log.error("校验合并文件md5值不一致,原始文件:{},合并文件:{}", fileMd5, mergeFileMd5);
				return RestResponse.validfail(false, "文件校验失败");
			}
			//文件大小
			uploadFileParamsDto.setFileSize(file.length());
		} catch (Exception e) {
			return RestResponse.validfail(false, "文件校验失败");
		}
		
		//==============将文件信息入库============
		MediaFiles mediaFiles = currentProxy.addFileDbInfo(companyId, uploadFileParamsDto, savePath, oriFilename, fileMd5);
		if (mediaFiles == null) {
			return RestResponse.validfail(false, "文件入库失败");
		}
		
		//==========清理分块文件=========
		clearChunkFiles(chunkFileFolderPath, chunkTotal);
		return RestResponse.success(true);
	}
	
	/**
	 * 合并后将分块文件清除
	 *
	 * @param chunkFileFolderPath 分块文件路径
	 * @param chunkTotal          分块文件总数
	 */
	private void clearChunkFiles(String chunkFileFolderPath, int chunkTotal) {
		Iterable<DeleteObject> objects = Stream.iterate(0, i -> ++i)
				.limit(chunkTotal)
				.map(i -> new DeleteObject(chunkFileFolderPath + i))
				.collect(Collectors.toList());
		RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket(bigFileBucket).objects(objects).build();
		Iterable<Result<DeleteError>> results = client.removeObjects(removeObjectsArgs);
		//执行删除
		results.forEach(errorResult -> {
			try {
				DeleteError error = errorResult.get();
				log.error("minio删除对象错误： " + error.objectName() + "; " + error.message());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * 从minio下载文件
	 *
	 * @param bucket     存储桶
	 * @param objectName 路径
	 * @return 文件
	 */
	@Override
	public File downloadFileFromMinio(String bucket, String objectName) {
		File tempMergedFile;
		//临时文件
		FileOutputStream outputStream = null;
		try {
			InputStream stream = client.getObject(GetObjectArgs.builder()
					.bucket(bucket)
					.object(objectName)
					.build());
			//临时测试 DigestUtils.md5DigestAsHex修改了inputstream不能连续使用 
			//System.out.println(DigestUtils.md5DigestAsHex(stream));
			//创建临时文件
			tempMergedFile = File.createTempFile("minio", ".merge");
			outputStream = new FileOutputStream(tempMergedFile);
			IOUtils.copy(stream, outputStream);
			return tempMergedFile;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@Override
	public MediaFiles getMediaById(String mediaId) {
		return mediaFilesMapper.selectById(mediaId);
	}
	
	@Override
	public UploadFileResultDto uploadHtmlFile(Long companyId, UploadFileParamsDto dto, String localFilePath, String objectName) {
		try {
			boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(htmlBucket).build());
			if (!exists) {
				throw new Exception("存储桶不存在");
			}
			
			String oriFileName = dto.getFilename();
			//扩展名得到mimeType
			String extension = oriFileName.substring(oriFileName.lastIndexOf("."));
			String mimeType = getMimeTypeByExtension(extension);
			//md5作为保存后的文件名 
			String md5Id = DigestUtils.md5DigestAsHex(new FileInputStream(localFilePath));
			//设置默认路径
			if (!StringUtils.hasText(objectName)) {
				objectName = md5Id + extension;
			}
			addLocalFileToMinio(htmlBucket, mimeType, localFilePath, objectName);
			MediaFiles mediaFiles = currentProxy.addFileDbInfo(companyId, dto, objectName, oriFileName, md5Id);
			
			UploadFileResultDto resultDto = new UploadFileResultDto();
			BeanUtils.copyProperties(mediaFiles, resultDto);
			return resultDto;
		} catch (Exception e) {
			CustomException.cast("上传文件失败: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据MD5值得到分块文件存储的目录
	 * 位作为分块文件完整路径 MD5第一位/MD5第二位/MD5/chunk/i
	 */
	private String getChunkFileFolder(String fileMd5) {
		return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + "chunk" + "/";
	}
	
	private String getMergeFileFolder(String fileMd5, String ext) {
		return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + ext;
	}
	
	@Override
	public void addFileToMinio(String bucketName, String contentType, byte[] fileData, String savePath) {
		try {
			//上传minio
			ByteArrayInputStream in = new ByteArrayInputStream(fileData);
			PutObjectArgs objectArgs = PutObjectArgs.builder()
					.bucket(bucketName)
					.object(savePath)
					.contentType(contentType)
					.stream(in, in.available(), -1)
					.build();
			client.putObject(objectArgs);
		} catch (Exception e) {
			e.printStackTrace();
			CustomException.cast("上传文件到文件系统失败: " + e.getMessage());
		}
	}
	
	@Override
	public boolean addLocalFileToMinio(String bucketName, String contentType, String sourcePath, String savePath) {
		try {
			UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
					//桶
					.bucket(bucketName)
					//指定本地文件路径
					.filename(sourcePath)
					//对象名 放在子目录下
					.object(savePath)
					//设置媒体文件类型
					.contentType(contentType)
					.build();
			//上传文件
			client.uploadObject(uploadObjectArgs);
			log.debug("上传文件到minio成功,bucket:{},objectName:{}", bucketName, savePath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传文件出错,bucket:{},objectName:{},错误信息:{}", bucketName, savePath, e.getMessage());
		}
		return false;
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
