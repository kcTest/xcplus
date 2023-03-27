package com.zkc.xcplus.content.service.config;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Configuration

public class MultipartSupportConfig {
	
	/**
	 * file转MultipartFile
	 *
	 * @param file File
	 * @return MultipartFile
	 */
	public static MultipartFile getMultipartFile(File file) {
		FileItem item = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
		try (FileInputStream inputStream = new FileInputStream(file);
			 OutputStream outputStream = item.getOutputStream();) {
			IOUtils.copy(inputStream, outputStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new CommonsMultipartFile(item);
	}
}
