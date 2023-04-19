package com.zkc.xcplus.auth.user.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Schema
@Data
@TableName(value = "xc_menu")
public class XcMenu implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="")
    private String id;

    /**
     * 菜单编码
     */
    @TableField(value = "code")
    @Schema(description="菜单编码")
    private String code;

    /**
     * 父菜单ID
     */
    @TableField(value = "p_id")
    @Schema(description="父菜单ID")
    private String pId;

    /**
     * 名称
     */
    @TableField(value = "menu_name")
    @Schema(description="名称")
    private String menuName;

    /**
     * 请求地址
     */
    @TableField(value = "url")
    @Schema(description="请求地址")
    private String url;

    /**
     * 是否是菜单
     */
    @TableField(value = "is_menu")
    @Schema(description="是否是菜单")
    private String isMenu;

    /**
     * 菜单层级
     */
    @TableField(value = "`level`")
    @Schema(description="菜单层级")
    private Integer level;

    /**
     * 菜单排序
     */
    @TableField(value = "sort")
    @Schema(description="菜单排序")
    private Integer sort;

    @TableField(value = "`status`")
    @Schema(description="")
    private String status;

    @TableField(value = "icon")
    @Schema(description="")
    private String icon;

    @TableField(value = "create_time")
    @Schema(description="")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description="")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}