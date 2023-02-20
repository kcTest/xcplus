package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 课程计划
 * @TableName teachplan
 */
@TableName(value ="teachplan")
@Data
public class Teachplan implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程计划名称
     */
    @TableField(value = "pname")
    private String pname;

    /**
     * 课程计划父级Id
     */
    @TableField(value = "parentid")
    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    @TableField(value = "grade")
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    @TableField(value = "media_type")
    private String mediaType;

    /**
     * 开始直播时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 直播结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 章节及课程时介绍
     */
    @TableField(value = "description")
    private String description;

    /**
     * 时长，单位时:分:秒
     */
    @TableField(value = "timelength")
    private String timelength;

    /**
     * 排序字段
     */
    @TableField(value = "orderby")
    private Integer orderby;

    /**
     * 课程标识
     */
    @TableField(value = "course_id")
    private Long courseId;

    /**
     * 课程发布标识
     */
    @TableField(value = "course_pub_id")
    private Long coursePubId;

    /**
     * 状态（1正常  0删除）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否支持试学或预览（试看）
     */
    @TableField(value = "is_preview")
    private String isPreview;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 修改时间
     */
    @TableField(value = "change_date")
    private Date changeDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}