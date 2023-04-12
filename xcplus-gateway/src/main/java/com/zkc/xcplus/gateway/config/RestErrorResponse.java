package com.zkc.xcplus.gateway.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装错误响应
 */
@Data
public class RestErrorResponse implements Serializable {
	
	private String errMsg;
	
	public RestErrorResponse(String errMsg) {
		this.errMsg = errMsg;
	}
}
