package com.zkc.xcplus.auth.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkc.xcplus.auth.user.model.po.XcMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 自定义  指定mapper location?
 */
public interface XcMenuMapper extends BaseMapper<XcMenu> {
	
	@Select("select m.*\n" +
			"from xc_user_role  ur\n" +
			"join xc_permission p on p.role_id = ur.role_id\n" +
			"join xc_menu       m on m.id = p.menu_id\n" +
			"where ur.user_id = #{userId}\n")
	List<XcMenu> selectPermissionByUserId(@Param("userId") String userId);
}