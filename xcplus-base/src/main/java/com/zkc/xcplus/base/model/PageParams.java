package com.zkc.xcplus.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageParams {
	
	/**
	 * 默认页页码
	 */
	public static final long DEFAULT_PAGE_CURRENT = 1L;
	
	/**
	 * 每页默认条数
	 */
	public static final long DEFAULT_PAGE_SIZE = 10L;
	
	/**
	 * 当前页页码
	 */
	@Schema(description = "当前页页码")
	private Long pageNo = DEFAULT_PAGE_CURRENT;
	
	/**
	 * 每页记录数
	 */
	@Schema(description = "每页记录数")
	private Long pageSize = DEFAULT_PAGE_SIZE;
}
