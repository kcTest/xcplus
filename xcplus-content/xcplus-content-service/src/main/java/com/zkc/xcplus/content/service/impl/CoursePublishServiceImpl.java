package com.zkc.xcplus.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.zkc.xcplus.base.exception.CommonError;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.content.model.dto.CourseBaseInfoDto;
import com.zkc.xcplus.content.model.dto.CoursePreviewDto;
import com.zkc.xcplus.content.model.dto.TeachPlanDto;
import com.zkc.xcplus.content.model.po.CourseBase;
import com.zkc.xcplus.content.model.po.CourseMarket;
import com.zkc.xcplus.content.model.po.CoursePublish;
import com.zkc.xcplus.content.model.po.CoursePublishPre;
import com.zkc.xcplus.content.service.CourseBaseInfoService;
import com.zkc.xcplus.content.service.CoursePublishService;
import com.zkc.xcplus.content.service.TeachPlanService;
import com.zkc.xcplus.content.service.config.MultipartSupportConfig;
import com.zkc.xcplus.content.service.dao.CourseBaseMapper;
import com.zkc.xcplus.content.service.dao.CourseMarketMapper;
import com.zkc.xcplus.content.service.dao.CoursePublishMapper;
import com.zkc.xcplus.content.service.dao.CoursePublishPreMapper;
import com.zkc.xcplus.content.service.feignclient.MediaServiceClient;
import com.zkc.xcplus.content.service.feignclient.SearchServiceClient;
import com.zkc.xcplus.content.service.po.CourseIndexInfo;
import com.zkc.xcplus.message.model.MqMessage;
import com.zkc.xcplus.message.service.MqMessageService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class CoursePublishServiceImpl implements CoursePublishService {
	
	@Autowired
	private CourseBaseInfoService courseBaseInfoService;
	
	@Autowired
	private TeachPlanService teachPlanService;
	
	@Autowired
	private CourseMarketMapper courseMarketMapper;
	
	@Autowired
	private CoursePublishPreMapper coursePublishPreMapper;
	
	@Autowired
	private CourseBaseMapper courseBaseMapper;
	
	@Autowired
	private CoursePublishMapper coursePublishMapper;
	
	@Autowired
	private MqMessageService mqMessageService;
	
	@Autowired
	private MediaServiceClient mediaServiceClient;
	
	@Autowired
	private SearchServiceClient searchServiceClient;
	
	@Override
	public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
		CoursePreviewDto ret = new CoursePreviewDto();
		
		//基本信息
		CourseBaseInfoDto baseInfo = courseBaseInfoService.get(courseId);
		//课程计划
		List<TeachPlanDto> plan = teachPlanService.getTreeNodes(courseId);
		
		ret.setCourseBaseInfo(baseInfo);
		ret.setTeachPlans(plan);
		return ret;
	}
	
	@Transactional
	@Override
	public void submitForReview(Long companyId, Long courseId) {
		//约束
		CourseBaseInfoDto baseInfo = courseBaseInfoService.get(courseId);
		if (baseInfo == null) {
			CustomException.cast("课程未找到");
		}
		if (baseInfo.getCompanyId().longValue() != companyId.longValue()) {
			CustomException.cast("只能提交本机构课程");
		}
		if ("202003".equals(baseInfo.getAuditStatus())) {
			CustomException.cast("课程已提交");
		}
		if (!StringUtils.hasText(baseInfo.getPic())) {
			CustomException.cast("请上传课程图片");
		}
		List<TeachPlanDto> planDtoList = teachPlanService.getTreeNodes(courseId);
		if (planDtoList == null || planDtoList.size() == 0) {
			CustomException.cast("请添加课程计划");
		}
		//插入课程预发布表
		CoursePublishPre coursePublishPre = new CoursePublishPre();
		//基本
		BeanUtils.copyProperties(baseInfo, coursePublishPre);
		//营销信息
		CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
		String courseMarketJson = JSON.toJSONString(courseMarket);
		coursePublishPre.setMarket(courseMarketJson);
		//计划
		String planJson = JSON.toJSONString(planDtoList);
		coursePublishPre.setTeachplan(planJson);
		//已提交
		coursePublishPre.setStatus("202003");
		coursePublishPre.setCreateDate(LocalDateTime.now());
		
		//已存在更新
		CoursePublishPre coursePublishPreOld = coursePublishPreMapper.selectById(courseId);
		if (coursePublishPreOld == null) {
			coursePublishPreMapper.insert(coursePublishPre);
		} else {
			coursePublishPreMapper.updateById(coursePublishPre);
		}
		
		//修改课程基本信息表状态
		CourseBase courseBase = courseBaseMapper.selectById(courseId);
		//已提交
		courseBase.setAuditStatus("202003");
		courseBaseMapper.updateById(courseBase);
	}
	
	@Transactional
	@Override
	public void coursePublish(Long companyId, Long courseId) {
		
		CoursePublishPre publishPre = coursePublishPreMapper.selectById(courseId);
		if (publishPre == null) {
			CustomException.cast("课程预发布信息未找到");
		}
		if (publishPre.getCompanyId().longValue() != companyId.longValue()) {
			CustomException.cast("只能发布本机构课程");
		}
		if (!"202004".equals(publishPre.getStatus())) {
			CustomException.cast("课程未审核通过");
		}
		
		// 课程发布表
		CoursePublish publish = new CoursePublish();
		BeanUtils.copyProperties(publishPre, publish);
		CoursePublish publishOld = coursePublishMapper.selectById(courseId);
		if (publishOld == null) {
			coursePublishMapper.insert(publish);
		} else {
			coursePublishMapper.updateById(publish);
		}
		
		//消息写入数据
		saveCoursePublishMessage(courseId);
		
		//预发布表记录删除
		coursePublishPreMapper.deleteById(publishPre);
	}
	
	/**
	 * 消息表 写入数据,课程发布后写入发布表 消息表，后续任务调度上传缓存到redis 上传索引到es 生成页面缓存到nginx； AP
	 *
	 * @param courseId 课程id
	 */
	private void saveCoursePublishMessage(Long courseId) {
		MqMessage message = mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
		if (message == null) {
			CustomException.cast(CommonError.UNKNOWN_ERROR);
		}
	}
	
	@Override
	public File generateCourseHtml(Long courseId) {
		//获取当前使用的freemarker版本
		Configuration configuration = new Configuration(Configuration.getVersion());
		//静态页面文件
		File htmlFile = null;
		try {
			//指定页面模板目录
			configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass().getClassLoader(), "/templates"));
			//指定页面编码
			configuration.setDefaultEncoding("utf-8");
			
			//获取页面模板
			Template template = configuration.getTemplate("course_template.ftl");
			
			//准备课程页面数据
			CoursePreviewDto previewInfo = this.getCoursePreviewInfo(courseId);
			HashMap<String, Object> map = new HashMap<>();
			map.put("model", previewInfo);
			
			//填充数据后的页面字符串
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
			
			//字符输出文件
			InputStream inputStream = IOUtils.toInputStream(html, "utf-8");
			htmlFile = File.createTempFile("coursepublish", ".html");
			FileOutputStream outputStream = new FileOutputStream(htmlFile);
			IOUtils.copy(inputStream, outputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("页面静态化出现问题，课程id:{}", courseId, ex);
		}
		return htmlFile;
	}
	
	@Override
	public void uploadCourseHtml(long courseId, File file) {
		try {
			MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
			String ret = mediaServiceClient.uploadHtml(multipartFile, courseId + ".html");
			if (ret == null) {
				log.debug("远程调用媒资服务上传页面文件异常,课程id:{}", courseId);
				CustomException.cast("远程调用媒资服务上传页面文件异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			CustomException.cast("上传静态文件异常");
		}
	}
	
	@Override
	public boolean addCourseIdx(CourseIndexInfo courseIndexInfo) {
		try {
			boolean ret = searchServiceClient.addCourseIdx(courseIndexInfo);
			if (!ret) {
				log.debug("远程调用媒资服务上传页面文件异常,课程索引:{}", courseIndexInfo);
				CustomException.cast("远程调用媒资服务上传页面文件异常");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			CustomException.cast("保存课程索引异常");
		}
		return false;
	}
	
	@Override
	public CoursePublish getCoursePublish(Long courseId) {
		return coursePublishMapper.selectById(courseId);
	}
	
	
}
