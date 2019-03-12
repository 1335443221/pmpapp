package com.sl.pmpapp.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sl.pmpapp.service.ElecReportDataService;

@Controller
@RequestMapping("/elec")
public class ElecReportController {
	@Autowired
	ElecReportDataService elecReportDataImpl;
	
	
	
	
	/**
	 * 一个项目的所有厂区+配电
	 * @param map
	 * @return
	 */
	@RequestMapping("/get_project_factorys")
	@ResponseBody
	public Object get_project_factorys(@RequestParam Map<String, Object> map){
		
		return elecReportDataImpl.get_project_factorys(map);
	}	
	
	
 /**
    * 单日报表列表
    */
	@RequestMapping("/singleDay_list")
	@ResponseBody
	public Object singleDay_list(@RequestParam Map<String, Object> map){
		return elecReportDataImpl.singleDay_list(map);
	}
	
 /**
    * 区间报表列表
    */
	@RequestMapping("/section_list")
	@ResponseBody
	public Object section_list(@RequestParam Map<String, Object> map){
		return elecReportDataImpl.section_list(map);
	}
			
 /**
    * 单日报表详情
    */
	@RequestMapping("/singleDay_detail")
	@ResponseBody
	public Object singleDay_detail(@RequestParam Map<String, Object> map){
		return elecReportDataImpl.singleDay_detail(map);
	}

 /**
    * 区间报表详情
    */
	@RequestMapping("/section_detail")
	@ResponseBody
	public Object section_detail(@RequestParam Map<String, Object> map){
		return elecReportDataImpl.section_detail(map);
	}
	
	/**
     * 出线列表单日
     */
	@RequestMapping("/singleDay_Coilin_sort")
	@ResponseBody
    public Object singleDay_Coilin_sort(@RequestParam Map<String, Object> map){
	
		return elecReportDataImpl.singleDay_Coilin_sort(map);
	}
	
	
	/**
     * 出线列表区间
     */
	@RequestMapping("/section_Coilin_sort")
	@ResponseBody
    public Object section_Coilin_sort(@RequestParam Map<String, Object> map){
	
		return elecReportDataImpl.section_Coilin_sort(map);
	}
	
	
	
	
	
	
	
	
	
	
	
 /**
    * 一个厂区电的区域分类列表（配电室）
    */
	@RequestMapping("/get_distribution_room_list")
	@ResponseBody
	public Object get_factory_elec_list(@RequestParam Map<String, Object> map){
		return elecReportDataImpl.get_distribution_room_list(map);
	}
	
	
	
	
	
/**
    * 万达酒店_能耗app推广版本接口
    * 所有厂区电的区域分类列表
    */
	@RequestMapping("/get_category_list")
	@ResponseBody
	public Object get_category_list(@RequestParam Map<String, Object> map){
		return elecReportDataImpl.get_category_list(map);
	}	
	
	
	
	
	
	
	
	
	
	

}
