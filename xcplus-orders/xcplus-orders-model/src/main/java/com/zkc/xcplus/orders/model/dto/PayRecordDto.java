package com.zkc.xcplus.orders.model.dto;

import com.zkc.xcplus.orders.model.po.XcPayRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付记录Dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayRecordDto extends XcPayRecord {
	
	/**
	 * 二维码
	 */
	private String qrcode;
	
}
