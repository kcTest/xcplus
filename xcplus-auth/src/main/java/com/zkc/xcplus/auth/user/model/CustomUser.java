package com.zkc.xcplus.auth.user.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义用户信息
 */
public class CustomUser extends User {
	
	private String nickname;
	
	private String name;
	
	private String userpic;
	
	private String companyId;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserpic() {
		return userpic;
	}
	
	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public Map<String, String> getAdditionalInfo() {
		Map<String, String> map = new HashMap<>();
		map.put("name", this.name);
		map.put("userpic", this.userpic);
		map.put("companyId", this.companyId);
		map.put("nickname", this.nickname);
		return map;
	}
	
}
