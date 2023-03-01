package com.zkc.xcplus.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(CustomException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse handleCustomException(CustomException e) {
		e.printStackTrace();
		String msg = e.getMsg();
		log.error(msg);
		return new RestErrorResponse(msg);
	}
	
	/**
	 * 处理其余不可预知异常
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse handleCustomException(Exception e) {
		e.printStackTrace();
		String msg = e.getMessage();
		log.error(msg);
		return new RestErrorResponse(msg);
	}
}
