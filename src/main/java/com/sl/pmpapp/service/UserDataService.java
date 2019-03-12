package com.sl.pmpapp.service;

import java.util.Map;


public interface UserDataService {
	//登录验证
	public Map<String, Object> checkLogin(Map<String, Object> map);
		
		
	//验证操作密码
	public Map<String, Object> checkOper_pwd(Map<String, Object> map);
	
	
	//退出登录
	public Map<String, Object> quit(Map<String, Object> map);
}
