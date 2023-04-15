package com.zkc.xcplus.base.exception;

public enum CommonError {
	
	UNKNOWN_ERROR("执行过程异常，请重试。"),
	PARAMS_ERROR("非法参数"),
	OBJECT_NULL("对象为空"),
	QUERY_NULL("查询结果为空"),
	REQUEST_NULL("请求参数为空"),
	UNAUTHORIZED("未授权");
	
	CommonError(String msg) {
		this.msg = msg;
	}
	
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	

}
