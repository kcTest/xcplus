package com.zkc.xcplus.auth.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema
@Data
@TableName(value = "xc_user")
@ToString
public class XcUser implements Serializable {
	@TableId(value = "id", type = IdType.INPUT)
	private String id;
	
	@TableField(value = "username")
	private String username;
	
	@TableField(value = "`password`")
	private String password;
	
	@TableField(value = "salt")
	private String salt;
	
	/**
	 * 微信unionid
	 */
	@TableField(value = "wx_unionid")
	@Schema(description = "微信unionid")
	private String wxUnionid;
	
	/**
	 * 昵称
	 */
	@TableField(value = "nickname")
	@Schema(description = "昵称")
	private String nickname;
	
	@TableField(value = "`name`")
	private String name;
	
	/**
	 * 头像
	 */
	@TableField(value = "userpic")
	@Schema(description = "头像")
	private String userpic;
	
	@TableField(value = "company_id")
	private String companyId;
	
	@TableField(value = "utype")
	private String utype;
	
	@TableField(value = "birthday")
	private Date birthday;
	
	@TableField(value = "sex")
	private String sex;
	
	@TableField(value = "email")
	private String email;
	
	@TableField(value = "cellphone")
	private String cellphone;
	
	@TableField(value = "qq")
	private String qq;
	
	/**
	 * 用户状态
	 */
	@TableField(value = "`status`")
	@Schema(description = "用户状态")
	private String status;
	
	@TableField(value = "create_time")
	//LocalDateTime
	//com.nimbusds.jose.shaded.gson.JsonIOException: Failed making field 'java.time.LocalDateTime#date' accessible; either change its visibility or write a custom TypeAdapter for its declaring type
	private Date createTime;
	
	@TableField(value = "update_time")
	private Date updateTime;
	
	@Serial
	private static final long serialVersionUID = 1L;
}

