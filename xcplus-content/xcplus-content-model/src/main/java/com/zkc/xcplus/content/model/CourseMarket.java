package com.zkc.xcplus.content.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 课程营销信息
 * @TableName course_market
 */
@TableName(value ="course_market")
@Data
public class CourseMarket implements Serializable {
    /**
     * 主键，课程id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 收费规则，对应数据字典
     */
    @TableField(value = "charge")
    private String charge;

    /**
     * 现价
     */
    @TableField(value = "price")
    private Double price;

    /**
     * 原价
     */
    @TableField(value = "original_price")
    private Double originalPrice;

    /**
     * 咨询qq
     */
    @TableField(value = "qq")
    private String qq;

    /**
     * 微信
     */
    @TableField(value = "wechat")
    private String wechat;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 有效期天数
     */
    @TableField(value = "valid_days")
    private Integer validDays;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}