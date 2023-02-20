package com.zkc.xcplus.content.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 课程-教师关系表
 * @TableName course_teacher
 */
@TableName(value ="course_teacher")
@Data
public class CourseTeacher implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程标识
     */
    @TableField(value = "course_id")
    private Long courseId;

    /**
     * 教师标识
     */
    @TableField(value = "teacher_name")
    private String teacherName;

    /**
     * 教师职位
     */
    @TableField(value = "position")
    private String position;

    /**
     * 教师简介
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 照片
     */
    @TableField(value = "photograph")
    private String photograph;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}