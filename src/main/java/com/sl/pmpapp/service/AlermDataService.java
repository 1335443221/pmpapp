package com.sl.pmpapp.service;

import java.util.Map;


public interface AlermDataService {
	//报警列表
	public Object alermList(Map<String, Object> map);
		
		
	//报警详情
	public Object alermDetail(Map<String, Object> map);
	
	
	//报警推送
	public Object alermPush(Map<String, Object> map);
}
