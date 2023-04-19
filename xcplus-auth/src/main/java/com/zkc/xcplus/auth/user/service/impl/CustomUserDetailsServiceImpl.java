package com.zkc.xcplus.auth.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkc.xcplus.auth.user.dao.XcMenuMapper;
import com.zkc.xcplus.auth.user.dao.XcUserMapper;
import com.zkc.xcplus.auth.user.model.dto.CustomUser;
import com.zkc.xcplus.auth.user.model.po.XcMenu;
import com.zkc.xcplus.auth.user.model.po.XcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于用户名密码认证时  自定义用户信息获取
 */
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private XcUserMapper xcUserMapper;
	
	@Autowired
	private XcMenuMapper xcMenuMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LambdaQueryWrapper<XcUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(XcUser::getUsername, username);
		XcUser user = xcUserMapper.selectOne(queryWrapper);
		if (user == null) {
			return null;
		}
		//DaoAuthenticationProvider->additionalAuthenticationChecks 使用当前密码与输入密码校验是否一致
		String password = user.getPassword();
		List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(user.getId());
		List<String> ps = new ArrayList<>();
		for (XcMenu menu : xcMenus) {
			ps.add(menu.getCode());
		}
		CustomUser customUser = new CustomUser(username, password, AuthorityUtils.createAuthorityList(ps.toArray(new String[0])));
		user.setPassword(null);
		customUser.setXcUser(user);
		return customUser;
	}
}
