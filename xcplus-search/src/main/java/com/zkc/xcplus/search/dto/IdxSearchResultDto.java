package com.zkc.xcplus.search.dto;

import com.zkc.xcplus.base.model.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 分页查询结果
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class IdxSearchResultDto<T> extends PageResult<T> {
	
	/**
	 * 小分类列表
	 */
	private List<String> stList;
	
	/**
	 * 大分类列表
	 */
	private List<String> mtList;
	
	public IdxSearchResultDto(List<T> items, long counts, long page, long pageSize) {
		super(items, counts, page, pageSize);
	}
}
