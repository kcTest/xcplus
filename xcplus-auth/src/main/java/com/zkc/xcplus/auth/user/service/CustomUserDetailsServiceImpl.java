package com.zkc.xcplus.auth.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkc.xcplus.auth.user.dao.XcUserMapper;
import com.zkc.xcplus.auth.user.model.CustomUser;
import com.zkc.xcplus.auth.user.model.XcUser;
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
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
