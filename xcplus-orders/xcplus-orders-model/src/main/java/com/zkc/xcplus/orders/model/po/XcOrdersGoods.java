package com.zkc.xcplus.orders.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Schema
@Data
@TableName(value = "xc_orders_goods")
public class XcOrdersGoods implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField(value = "order_id")
    @Schema(description="订单号")
    private Long orderId;

    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    @Schema(description="商品id")
    private String goodsId;

    /**
     * 商品类型
     */
    @TableField(value = "goods_type")
    @Schema(description="商品类型")
    private String goodsType;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    @Schema(description="商品名称")
    private String goodsName;

    /**
     * 商品交易价，单位分
     */
    @TableField(value = "goods_price")
    @Schema(description="商品交易价，单位分")
    private Double goodsPrice;

    /**
     * 商品详情json
     */
    @TableField(value = "goods_detail")
    @Schema(description="商品详情json")
    private String goodsDetail;
	
	@Serial
    private static final long serialVersionUID = 1L;
}