package com.zkc.xcplus.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName teachplan_media
 */
@TableName(value ="teachplan_media")
@Data
public class TeachplanMedia implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 媒资文件id
     */
    @TableField(value = "media_id")
    private String mediaId;

    /**
     * 课程计划标识
     */
    @TableField(value = "teachplan_id")
    private Long teachplanId;

    /**
     * 课程标识
     */
    @TableField(value = "course_id")
    private Long courseId;

    /**
     * 媒资文件原始名称
     */
    @TableField(value = "media_fileName")
    private String mediaFilename;

    /**
     * 
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 创建人
     */
    @TableField(value = "create_people")
    private String createPeople;

    /**
     * 修改人
     */
    @TableField(value = "change_people")
    private String changePeople;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}