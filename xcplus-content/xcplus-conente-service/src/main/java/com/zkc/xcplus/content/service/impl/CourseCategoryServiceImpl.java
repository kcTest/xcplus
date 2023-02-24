package com.zkc.xcplus.content.service.impl;

import com.zkc.xcplus.content.model.dto.CourseCategoryTreeDto;
import com.zkc.xcplus.content.service.CourseCategoryService;
import com.zkc.xcplus.content.service.dao.CourseCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {
	
	@Autowired
	private CourseCategoryMapper courseCategoryMapper;
	
	@Override
	public List<CourseCategoryTreeDto> getTreeNodes(String parentId) {
		
		List<CourseCategoryTreeDto> nodes = courseCategoryMapper.getTreeNodes();
		List<CourseCategoryTreeDto> resultNodes = new ArrayList<>();
		for (CourseCategoryTreeDto curNode : nodes) {
			if (curNode.getParentid().equals(parentId)) {
				resultNodes.add(curNode);
				fillChildNode(curNode, nodes);
			}
		}
		return resultNodes;
	}
	
	/**
	 * 填充子节点
	 *
	 * @param parentNode 父节点
	 * @param nodes      所有节点
	 */
	private static void fillChildNode(CourseCategoryTreeDto parentNode, List<CourseCategoryTreeDto> nodes) {
		for (CourseCategoryTreeDto node : nodes) {
			if (node.getParentid().equals(parentNode.getId())) {
				List<CourseCategoryTreeDto> childrenTreeNodes = parentNode.getChildrenTreeNodes();
				if (childrenTreeNodes == null) {
					childrenTreeNodes = new ArrayList<>();
					parentNode.setChildrenTreeNodes(childrenTreeNodes);
				}
				childrenTreeNodes.add(node);
				fillChildNode(node, nodes);
			}
		}
	}
}
