package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
    * 课程发布
    */
@Schema(description="课程发布")
@Data
@TableName(value = "course_publish_pre")
public class CoursePublishPre implements Serializable {
    /**
     * 主键 课程id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(description="主键")
    private Long id;

    /**
     * 机构ID
     */
    @TableField(value = "company_id")
    @Schema(description="机构ID")
    private Long companyId;

    /**
     * 公司名称
     */
    @TableField(value = "company_name")
    @Schema(description="公司名称")
    private String companyName;

    /**
     * 课程名称
     */
    @TableField(value = "`name`")
    @Schema(description="课程名称")
    private String name;

    /**
     * 适用人群
     */
    @TableField(value = "users")
    @Schema(description="适用人群")
    private String users;

    /**
     * 标签
     */
    @TableField(value = "tags")
    @Schema(description="标签")
    private String tags;

    /**
     * 创建人
     */
    @TableField(value = "username")
    @Schema(description="创建人")
    private String username;

    /**
     * 大分类
     */
    @TableField(value = "mt")
    @Schema(description="大分类")
    private String mt;

    /**
     * 大分类名称
     */
    @TableField(value = "mt_name")
    @Schema(description="大分类名称")
    private String mtName;

    /**
     * 小分类
     */
    @TableField(value = "st")
    @Schema(description="小分类")
    private String st;

    /**
     * 小分类名称
     */
    @TableField(value = "st_name")
    @Schema(description="小分类名称")
    private String stName;

    /**
     * 课程等级
     */
    @TableField(value = "grade")
    @Schema(description="课程等级")
    private String grade;

    /**
     * 教育模式
     */
    @TableField(value = "teachmode")
    @Schema(description="教育模式")
    private String teachmode;

    /**
     * 课程图片
     */
    @TableField(value = "pic")
    @Schema(description="课程图片")
    private String pic;

    /**
     * 课程介绍
     */
    @TableField(value = "description")
    @Schema(description="课程介绍")
    private String description;

    /**
     * 课程营销信息，json格式
     */
    @TableField(value = "market")
    @Schema(description="课程营销信息，json格式")
    private String market;

    /**
     * 所有课程计划，json格式
     */
    @TableField(value = "teachplan")
    @Schema(description="所有课程计划，json格式")
    private String teachplan;

    /**
     * 教师信息，json格式
     */
    @TableField(value = "teachers")
    @Schema(description="教师信息，json格式")
    private String teachers;

    /**
     * 提交时间
     */
    @TableField(value = "create_date")
    @Schema(description="提交时间")
    private LocalDateTime createDate;

    /**
     * 审核时间
     */
    @TableField(value = "audit_date")
    @Schema(description="审核时间")
    private LocalDateTime auditDate;

    /**
     * 状态
     */
    @TableField(value = "`status`")
    @Schema(description="状态")
    private String status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @Schema(description="备注")
    private String remark;

    /**
     * 收费规则，对应数据字典--203
     */
    @TableField(value = "charge")
    @Schema(description="收费规则，对应数据字典--203")
    private String charge;

    /**
     * 现价
     */
    @TableField(value = "price")
    @Schema(description="现价")
    private Double price;

    /**
     * 原价
     */
    @TableField(value = "original_price")
    @Schema(description="原价")
    private Double originalPrice;

    /**
     * 课程有效期天数
     */
    @TableField(value = "valid_days")
    @Schema(description="课程有效期天数")
    private Integer validDays;

    private static final long serialVersionUID = 1L;
}