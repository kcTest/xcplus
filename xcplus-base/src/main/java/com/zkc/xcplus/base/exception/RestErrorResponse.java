package com.zkc.xcplus.base.exception;

import java.io.Serializable;

public class RestErrorResponse implements Serializable {
	
	private String msg;
	
	public RestErrorResponse(String msg) {
		this.msg = msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
