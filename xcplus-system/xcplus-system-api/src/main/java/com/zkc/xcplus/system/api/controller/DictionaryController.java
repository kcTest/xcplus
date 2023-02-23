package com.zkc.xcplus.system.api.controller;

import com.zkc.xcplus.system.model.po.Dictionary;
import com.zkc.xcplus.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@GetMapping("/all")
	public List<Dictionary> queryAll() {
		return dictionaryService.queryAll();
	}
	
	@GetMapping("/code/{code}")
	public Dictionary getByCode(@PathVariable String code) {
		return dictionaryService.getByCode(code);
	}
}
