package com.sl.pmpapp.service;

import java.util.Map;


public interface RunDataService {
	

	
	//获取进线列表
	public Object get_coilin_list(Map<String, Object> map);
	
	//获取进线数据（带出线列表）
	public Object getCoilin_detail(Map<String, Object> map);
	
	//获取出线数据
	public Object getCoilout_detail(Map<String, Object> map);
	
	//开关接口
	public Object set_di(Map<String, Object> map);
	
	
	
	
	//分类列表
	public Object get_category_list(Map<String, Object> map);
	
	//开关日志
	public Object di_Log(Map<String, Object> map);
		
		
}
