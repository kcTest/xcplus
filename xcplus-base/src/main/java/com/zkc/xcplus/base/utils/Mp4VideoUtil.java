package com.zkc.xcplus.base.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Mp4VideoUtil extends VideoUtil {
	
	/**
	 * ffmpeg的安装位置
	 */
	private String ffmpeg_path = "E:\\浏览器下载\\xcplus\\ffmpeg\\ffmpeg.exe";
	private String video_path = "C:\\Users\\pczkc\\Desktop\\wallpaper\\TEST.mp4";
	private String mp4_path = "C:\\Users\\pczkc\\Desktop\\wallpaper\\";
	
	public Mp4VideoUtil(String ffmpeg_path, String video_path, String mp4_path) {
		super(ffmpeg_path);
		this.ffmpeg_path = ffmpeg_path;
		this.video_path = video_path;
		this.mp4_path = mp4_path;
	}
	
	/**
	 * 清除已生成的mp4
	 */
	private void clear_mp4(String mp4_path) {
		//删除原来已经生成的m3u8及ts文件
		File mp4File = new File(mp4_path);
		if (mp4File.exists() && mp4File.isFile()) {
			mp4File.delete();
		}
	}
	
	/**
	 * 视频编码，生成mp4文件
	 *
	 * @return 成功返回success，失败返回控制台日志
	 */
	public String generateMp4() {
		//清除已生成的mp4
		//        clear_mp4(mp4folder_path+mp4_name);
		clear_mp4(mp4_path);
        /*
        ffmpeg.exe -i  lucene.avi -c:v libx264 -s 1280x720 -pix_fmt yuv420p -b:a 63k -b:v 753k -r 18 .\lucene.mp4
         */
		List<String> command = new ArrayList<>();
		command.add(ffmpeg_path);
		command.add("-i");
		command.add(video_path);
		command.add("-c:v");
		command.add("libx264");
		//覆盖输出文件
		command.add("-y");
		command.add("-s");
		command.add("1280x720");
		command.add("-pix_fmt");
		command.add("yuv420p");
		command.add("-b:a");
		command.add("63k");
		command.add("-b:v");
		command.add("753k");
		command.add("-r");
		command.add("18");
		command.add(mp4_path);
		String outstring = null;
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(command);
			//将标准输入流和错误输入流合并，通过标准输入流程读取信息
			builder.redirectErrorStream(true);
			Process p = builder.start();
			outstring = waitFor(p);
			
		} catch (Exception ex) {
			
			ex.printStackTrace();
			
		}
		Boolean check_video_time = this.check_video_time(video_path, mp4_path);
		if (!check_video_time) {
			return outstring;
		} else {
			return "success";
		}
	}
	
	public static void main(String[] args) {
		//ffmpeg的路径
		String ffmpeg_path = "E:\\浏览器下载\\xcplus\\ffmpeg\\ffmpeg.exe";
		//源avi视频的路径
		String video_path = "C:\\Users\\pczkc\\Desktop\\wallpaper\\11.avi";
		//转换后mp4文件的名称
		//转换后mp4文件的路径
		String mp4_path = "C:\\Users\\pczkc\\Desktop\\wallpaper\\11.mp4";
		//创建工具类对象
		Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_path);
		//开始视频转换，成功将返回success
		String s = videoUtil.generateMp4();
		System.out.println(s);
	}
}