package com.zkc.xcplus.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
	
	/**
	 * 数据列表
	 */
	@Schema(description = "数据列表")
	private List<T> items;
	
	/**
	 * 总记录数
	 */
	@Schema(description = "总记录数")
	private long counts;
	
	/**
	 * 当前页码
	 */
	@Schema(description = "当前页码")
	private long page;
	
	/**
	 * 每页记录数
	 */
	@Schema(description = "每页记录数")
	private long pageSize;
}
