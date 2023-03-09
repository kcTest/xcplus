package com.zkc.xcplus.media.service;

import com.zkc.xcplus.base.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.*;

public class BigFileTest {
	
	@Test
	public void testChunk() throws IOException {
		//源文件
		File sourceFile = new File("C:\\Users\\pczkc\\Desktop\\wallpaper\\TEST.mp4");
		//MD5值
		String md5 = DigestUtils.md5DigestAsHex(new FileInputStream(sourceFile));
		System.out.println(md5);
		//分块文件存储路径
		File chunkFolderPath = new File("C:\\Users\\pczkc\\Desktop\\wallpaper\\chunks");
		if (!chunkFolderPath.exists() && !chunkFolderPath.mkdirs()) {
			CustomException.cast("分块文件目录创建失败");
		}
		//分块的大小 minio最小
		int chunkSize = 1024 * 1024 * 5;
		//分块数量向上取整
		long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
		//使用流对象读取源文件，向分块文件写数据，达到分块大小不再写
		RandomAccessFile rafSource = new RandomAccessFile(sourceFile, "r");
		//缓冲区
		byte[] b = new byte[1024];
		for (long i = 0; i < chunkNum; i++) {
			File file = new File(chunkFolderPath + "\\" + i);
			//如果分块文件存在，则删除
			if (file.exists() && !file.delete()) {
				CustomException.cast("分块文件已存在 删除失败");
			}
			boolean newFile = file.createNewFile();
			if (newFile) {
				//向分块文件写数据流对象
				RandomAccessFile rafTargetChunk = new RandomAccessFile(file, "rw");
				int len;
				while ((len = rafSource.read(b)) != -1) {
					//向文件中写数据
					rafTargetChunk.write(b, 0, len);
					//达到分块大小不再写了
					if (file.length() >= chunkSize) {
						break;
					}
				}
				rafTargetChunk.close();
			}
		}
		rafSource.close();
	}
}
