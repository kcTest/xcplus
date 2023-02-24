package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
    * 课程分类
    */
@Schema(description="课程分类")
@Data
@TableName(value = "course_category")
public class CourseCategory implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="主键")
    private String id;

    /**
     * 分类名称
     */
    @TableField(value = "`name`")
    @Schema(description="分类名称")
    private String name;

    /**
     * 分类标签默认和名称一样
     */
    @TableField(value = "`label`")
    @Schema(description="分类标签默认和名称一样")
    private String label;

    /**
     * 父结点id（第一级的父节点是0，自关联字段id）
     */
    @TableField(value = "parentid")
    @Schema(description="父结点id（第一级的父节点是0，自关联字段id）")
    private String parentid;

    /**
     * 是否显示
     */
    @TableField(value = "is_show")
    @Schema(description="是否显示")
    private Byte isShow;

    /**
     * 排序字段
     */
    @TableField(value = "orderby")
    @Schema(description="排序字段")
    private Integer orderby;

    /**
     * 是否叶子
     */
    @TableField(value = "is_leaf")
    @Schema(description="是否叶子")
    private Byte isLeaf;

    private static final long serialVersionUID = 1L;
}