package com.zkc.xcplus.base.exception;


/**
 * 自定义全局异常
 */
public class CustomException extends RuntimeException {
	
	private final String msg;
	
	public CustomException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	public static void cast(String msg) {
		throw new CustomException(msg);
	}
	
	public static void cast(CommonError error) {
		throw new CustomException(error.getMsg());
	}
	
}
