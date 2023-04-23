package com.zkc.xcplus.orders.service.impl;

import com.google.gson.Gson;
import com.zkc.xcplus.base.exception.CommonError;
import com.zkc.xcplus.base.exception.CustomException;
import com.zkc.xcplus.orders.model.po.XcUser;
import com.zkc.xcplus.orders.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public XcUser getCurrentUser() {
		String userinfo = request.getHeader("userinfo");
		if (!StringUtils.hasText(userinfo)) {
			CustomException.cast(CommonError.UNAUTHORIZED);
		}
		//授权服务内部使用gson序列化日期
		return new Gson().fromJson(userinfo, XcUser.class);
	}
}
