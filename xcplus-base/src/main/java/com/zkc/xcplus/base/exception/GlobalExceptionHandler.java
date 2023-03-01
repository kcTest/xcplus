package com.zkc.xcplus.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

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
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestErrorResponse handleValidException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		StringBuilder sb = new StringBuilder();
		//遍历错误字段
		for (FieldError fieldError : fieldErrors) {
			sb.append(fieldError.getDefaultMessage()).append(",");
		}
		return new RestErrorResponse(sb.toString());
	}
}
