package com.zkc.xcplus.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkc.xcplus.system.model.po.Dictionary;

import java.util.List;

/**
 * 数据字典服务
 */
public interface DictionaryService extends IService<Dictionary> {
	/**
	 * 查询所有数据字典
	 */
	List<Dictionary> queryAll();
	
	/**
	 * 根据code查询数据字典
	 */
	Dictionary getByCode(String code);
}
