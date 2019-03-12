package com.sl.pmpapp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.pmpapp.service.RunDataService;


@Controller
@RequestMapping("/run")
public class RunController {
	
	@Autowired
	RunDataService runDataImpl;
	
	
	


	/**
	 * 进线列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getCoilin_list")
	@ResponseBody
	public Object get_coilin_list(@RequestParam Map<String, Object> map){
		return runDataImpl.get_coilin_list(map);
	}	
	
	
	
	/**
	 * 进线详情（带出线数据）
	 * @param map
	 * @return
	 */
	@RequestMapping("/getCoilin_detail")
	@ResponseBody
	public Object getCoilin_detail(@RequestParam Map<String, Object> map){
		return runDataImpl.getCoilin_detail(map);
	}	
	
	
	/**
	 * 出线详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/getCoilout_detail")
	@ResponseBody
	public Object getCoilout_detail(@RequestParam Map<String, Object> map){
		return runDataImpl.getCoilout_detail(map);
	}	
	
	
	
	/**
	 * 开关按钮
	 * @param map
	 * @return
	 */
	@RequestMapping("/set_di")
	@ResponseBody
	public Object set_di(@RequestParam Map<String, Object> map){
		return runDataImpl.set_di(map);
	}	
	
	

}
