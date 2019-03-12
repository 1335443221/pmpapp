package com.sl.pmpapp.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sl.pmpapp.service.UserDataService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserDataService userDataImpl;
	
	/**
	 * 万达酒店_能耗app版本接口
	 * 验证操作密码
	 * @param user
	 * @return
	 */
	@RequestMapping("/checkOper_pwd")
	@ResponseBody
	public Object checkOper_pwd(@RequestParam Map<String, Object> map){
		Map<String, Object> map2=userDataImpl.checkOper_pwd(map);
		return map2;
	}
	
	
	
	/**
	 * 万达酒店_能耗app版本接口
	 * 退出登录
	 * @param user
	 * @return
	 */
	@RequestMapping("/quit")
	@ResponseBody
	public Object quit(@RequestParam Map<String, Object> map){
		Map<String, Object> map2=userDataImpl.quit(map);
		return map2;
	}
	
	
}
