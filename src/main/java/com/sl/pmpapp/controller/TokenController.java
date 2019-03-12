package com.sl.pmpapp.controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sl.pmpapp.service.UserDataService;

@Controller
@RequestMapping("/token")
public class TokenController {
	@Autowired
	UserDataService userDataImpl;
	
	
	/**
	 * 万达酒店_能耗app版本接口
	 * 登录接口
	 * @param user
	 * @return
	 */
	@RequestMapping("/checkLogin")
	@ResponseBody
	public Object checkLogin(@RequestParam Map<String, Object> map){
		Map<String, Object> map2=userDataImpl.checkLogin(map);
		return map2;
	}
	
	@RequestMapping("/test")
	public String test(@RequestParam Map<String, Object> map){
		return "pmpappPag/test";
	}

}
