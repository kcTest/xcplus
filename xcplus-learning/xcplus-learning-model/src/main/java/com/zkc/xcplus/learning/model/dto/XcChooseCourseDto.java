package com.zkc.xcplus.learning.model.dto;

import com.zkc.xcplus.learning.model.po.XcChooseCourse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class XcChooseCourseDto extends XcChooseCourse {
	
	/**
	 * 学习资格
	 * 702001：正常学习
	 * 702002：没有选课或选课后没有支付
	 * 702003：已过期需要申请续期或重新支付
	 */
	private String learnStatus;
}
