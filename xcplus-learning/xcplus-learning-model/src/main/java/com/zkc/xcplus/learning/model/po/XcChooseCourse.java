package com.zkc.xcplus.learning.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Schema
@Data
@TableName(value = "xc_choose_course")
public class XcChooseCourse implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="主键")
    private Long id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    @Schema(description="课程id")
    private Long courseId;

    /**
     * 课程名称
     */
    @TableField(value = "course_name")
    @Schema(description="课程名称")
    private String courseName;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @Schema(description="用户id")
    private String userId;

    /**
     * 机构id
     */
    @TableField(value = "company_id")
    @Schema(description="机构id")
    private Long companyId;

    /**
     * 选课类型
     */
    @TableField(value = "order_type")
    @Schema(description="选课类型")
    private String orderType;

    /**
     * 添加时间
     */
    @TableField(value = "create_date")
    @Schema(description="添加时间")
    private LocalDateTime createDate;

    /**
     * 课程价格
     */
    @TableField(value = "course_price")
    @Schema(description="课程价格")
    private Double coursePrice;

    /**
     * 课程有效期(天)
     */
    @TableField(value = "valid_days")
    @Schema(description="课程有效期(天)")
    private Integer validDays;

    /**
     * 选课状态
     */
    @TableField(value = "`status`")
    @Schema(description="选课状态")
    private String status;

    /**
     * 开始服务时间
     */
    @TableField(value = "validtime_start")
    @Schema(description="开始服务时间")
    private LocalDateTime validtimeStart;

    /**
     * 结束服务时间
     */
    @TableField(value = "validtime_end")
    @Schema(description="结束服务时间")
    private LocalDateTime validtimeEnd;

    /**
     * 备注
     */
    @TableField(value = "remarks")
    @Schema(description="备注")
    private String remarks;

	@Serial
    private static final long serialVersionUID = 1L;
}