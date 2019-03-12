package com.sl.pmpapp.service;

import java.util.Map;


public interface ElecReportDataService {
	
	
	
	//一个项目的所有厂区+配电
	public Object get_project_factorys(Map<String, Object> map);
	
	//单日报表列表
	public Object singleDay_list(Map<String, Object> map);
	
	//区间报表列表
	public Object section_list(Map<String, Object> map);
	
	//单日报表详情
	public Object singleDay_detail(Map<String, Object> map);
		
		
	//单日报表详情
	public Object section_detail(Map<String, Object> map);
	
	//获取进线分类区间
	public Object section_Coilin_sort(Map<String, Object> map);
	
	//获取进线分类单日
	public Object singleDay_Coilin_sort(Map<String, Object> map);
	
	
	
	
	
	
	//所选厂区电的区域分类列表
	public Object get_distribution_room_list(Map<String, Object> map);
	
	
	//分类列表
	public Object get_category_list(Map<String, Object> map);
	
	
		
}
