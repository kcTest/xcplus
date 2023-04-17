package com.zkc.xcplus.auth.user.model.dto;

import com.zkc.xcplus.auth.user.model.po.XcUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 自定义用户信息
 */
public class CustomUser extends User {
	
	private XcUser xcUser;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public XcUser getXcUser() {
		return xcUser;
	}
	
	public void setXcUser(XcUser xcUser) {
		this.xcUser = xcUser;
	}
}
