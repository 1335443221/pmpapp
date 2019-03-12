package com.sl.pmpapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sl.pmpapp.service.AlermDataService;
import com.sl.pmpapp.utils.JPushUtil;
import com.sl.pmpapp.utils.Jipush;

@Controller
@RequestMapping("/alerm")
public class AlermController {
	@Autowired
	AlermDataService alermDataImpl;
	
	
	/**
	 * 万达酒店_能耗app版本接口
	 * 报警列表接口
	 * @param user
	 * @return
	 */
	@RequestMapping("/alermList")
	@ResponseBody
	public Object alermList(@RequestParam Map<String, Object> map){
		return alermDataImpl.alermList(map);
	}
	
	
	/**
	 * 万达酒店_能耗app版本接口
	 * 报警详情接口
	 * @param user
	 * @return
	 */
	@RequestMapping("/alermDetail")
	@ResponseBody
	public Object alermDetail(@RequestParam Map<String, Object> map){
		return alermDataImpl.alermDetail(map);
	}
	
	
	/**
	 * 万达酒店_能耗app版本接口
	 * 报警详情接口
	 * @param user
	 * @return
	 */
	@RequestMapping("/alermPush")
	@ResponseBody
	public Object alermPush(@RequestParam Map<String, Object> map){
		return alermDataImpl.alermPush(map);
	}

}
