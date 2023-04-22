package com.zkc.xcplus.learning.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Schema
@Data
@TableName(value = "xc_course_tables")
public class XcCourseTables implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 选课记录id
     */
    @TableField(value = "choose_course_id")
    @Schema(description="选课记录id")
    private Long chooseCourseId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @Schema(description="用户id")
    private String userId;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    @Schema(description="课程id")
    private Long courseId;

    /**
     * 机构id
     */
    @TableField(value = "company_id")
    @Schema(description="机构id")
    private Long companyId;

    /**
     * 课程名称
     */
    @TableField(value = "course_name")
    @Schema(description="课程名称")
    private String courseName;

    /**
     * 课程类型
     */
    @TableField(value = "course_type")
    @Schema(description="课程类型")
    private String courseType;

    /**
     * 添加时间
     */
    @TableField(value = "create_date")
    @Schema(description="添加时间")
    private LocalDateTime createDate;

    /**
     * 开始服务时间
     */
    @TableField(value = "validtime_start")
    @Schema(description="开始服务时间")
    private LocalDateTime validtimeStart;

    /**
     * 到期时间
     */
    @TableField(value = "validtime_end")
    @Schema(description="到期时间")
    private LocalDateTime validtimeEnd;

    /**
     * 更新时间
     */
    @TableField(value = "update_date")
    @Schema(description="更新时间")
    private LocalDateTime updateDate;

    /**
     * 备注
     */
    @TableField(value = "remarks")
    @Schema(description="备注")
    private String remarks;

	@Serial
    private static final long serialVersionUID = 1L;
}