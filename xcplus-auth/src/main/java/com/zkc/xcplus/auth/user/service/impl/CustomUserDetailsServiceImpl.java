package com.zkc.xcplus.auth.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkc.xcplus.auth.user.dao.XcUserMapper;
import com.zkc.xcplus.auth.user.model.dto.AuthParamsDto;
import com.zkc.xcplus.auth.user.model.dto.CustomUser;
import com.zkc.xcplus.auth.user.model.po.XcUser;
import com.zkc.xcplus.base.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 基于用户名密码认证时  自定义用户信息获取
 */
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private XcUserMapper xcUserMapper;
	
	/**
	 * 传入的认证数据AuthParamsDto
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//将传入的json串转成AuthParamsDto
//		AuthParamsDto dto = null;
//		try {
//			dto = JSON.parseObject(data, AuthParamsDto.class);
//		} catch (Exception e) {
//			CustomException.cast("请求认证参数不符合要求");
//		}
//		
//		String username = dto.getUsername();
		
		LambdaQueryWrapper<XcUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcUser::getUsername, username);
		XcUser user = xcUserMapper.selectOne(queryWrapper);
		if (user == null) {
			return null;
		}
		String password = user.getPassword();
		
		CustomUser customUser = new CustomUser(username, password, AuthorityUtils.createAuthorityList("test"));
		user.setPassword(null);
		customUser.setXcUser(user);
		return customUser;
	}
}
