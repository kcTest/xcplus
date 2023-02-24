package com.zkc.xcplus.content.model.dto;

import com.zkc.xcplus.content.model.po.CourseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCategoryTreeDto extends CourseCategory {
	
	@Schema(description = "子节点")
	List<CourseCategoryTreeDto> childrenTreeNodes;
}
