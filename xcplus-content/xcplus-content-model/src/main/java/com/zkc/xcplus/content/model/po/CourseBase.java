package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 课程基本信息
 * @TableName course_base
 */
@TableName(value ="course_base")
@Data
public class CourseBase implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 机构ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 机构名称
     */
    @TableField(value = "company_name")
    private String companyName;

    /**
     * 课程名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 适用人群
     */
    @TableField(value = "users")
    private String users;

    /**
     * 课程标签
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 大分类
     */
    @TableField(value = "mt")
    private String mt;

    /**
     * 小分类
     */
    @TableField(value = "st")
    private String st;

    /**
     * 课程等级
     */
    @TableField(value = "grade")
    private String grade;

    /**
     * 教育模式(common普通，record 录播，live直播等）
     */
    @TableField(value = "teachmode")
    private String teachmode;

    /**
     * 课程介绍
     */
    @TableField(value = "description")
    private String description;

    /**
     * 课程图片
     */
    @TableField(value = "pic")
    private String pic;

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

    /**
     * 创建人
     */
    @TableField(value = "create_people")
    private String createPeople;

    /**
     * 更新人
     */
    @TableField(value = "change_people")
    private String changePeople;

    /**
     * 审核状态
     */
    @TableField(value = "audit_status")
    private String auditStatus;

    /**
     * 课程发布状态 未发布  已发布 下线
     */
    @TableField(value = "status")
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}