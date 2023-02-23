package com.zkc.xcplus.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
    * 数据字典
    */
@Schema(description="数据字典")
@Data
@TableName(value = "`dictionary`")
public class Dictionary implements Serializable {
    /**
     * id标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="id标识")
    private Long id;

    /**
     * 数据字典名称
     */
    @TableField(value = "`name`")
    @Schema(description="数据字典名称")
    private String name;

    /**
     * 数据字典代码
     */
    @TableField(value = "code")
    @Schema(description="数据字典代码")
    private String code;

    /**
     * 数据字典项--json格式
  
     */
    @TableField(value = "item_values")
    @Schema(description="数据字典项--json格式")
    private String itemValues;

    private static final long serialVersionUID = 1L;
}