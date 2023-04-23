package com.zkc.xcplus.orders.model.po;

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
@TableName(value = "xc_pay_record")
public class XcPayRecord implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description="主键")
    private Long id;

    /**
     * 本系统支付交易号
     */
    @TableField(value = "pay_no")
    @Schema(description="本系统支付交易号")
    private Long payNo;

    /**
     * 第三方支付交易流水号
     */
    @TableField(value = "out_pay_no")
    @Schema(description="第三方支付交易流水号")
    private String outPayNo;

    /**
     * 第三方支付渠道编号
     */
    @TableField(value = "out_pay_channel")
    @Schema(description="第三方支付渠道编号")
    private String outPayChannel;

    /**
     * 商品订单号
     */
    @TableField(value = "order_id")
    @Schema(description="商品订单号")
    private Long orderId;

    /**
     * 订单名称
     */
    @TableField(value = "order_name")
    @Schema(description="订单名称")
    private String orderName;

    /**
     * 订单总价单位元
     */
    @TableField(value = "total_price")
    @Schema(description="订单总价单位元")
    private Double totalPrice;

    /**
     * 币种CNY
     */
    @TableField(value = "currency")
    @Schema(description="币种CNY")
    private String currency;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    @Schema(description="创建时间")
    private LocalDateTime createDate;

    /**
     * 支付状态
     */
    @TableField(value = "`status`")
    @Schema(description="支付状态")
    private String status;

    /**
     * 支付成功时间
     */
    @TableField(value = "pay_success_time")
    @Schema(description="支付成功时间")
    private LocalDateTime paySuccessTime;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @Schema(description="用户id")
    private String userId;

	@Serial
    private static final long serialVersionUID = 1L;
}