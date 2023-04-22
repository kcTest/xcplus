package com.zkc.xcplus.learning.model.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class XcUser implements Serializable {
	private String id;
	
	private String username;
	
	private String password;
	
	private String salt;
	
	/**
	 * 微信unionid
	 */
	private String wxUnionid;
	
	/**
	 * 昵称
	 */
	private String nickname;
	
	private String name;
	
	/**
	 * 头像
	 */
	private String userpic;
	
	private String companyId;
	
	private String utype;
	
	private Date birthday;
	
	private String sex;
	
	private String email;
	
	private String cellphone;
	
	private String qq;
	
	/**
	 * 用户状态
	 */
	private String status;
	
	private Date createTime;
	
	private Date updateTime;
	
	@Serial
	private static final long serialVersionUID = 1L;
}
