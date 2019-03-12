package com.sl.pmpapp.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.sl.pmpapp.service.UserDataService;
import com.sl.pmpapp.utils.CEUtils;
import com.sl.pmpapp.utils.HttpClientService;
import com.sl.pmpapp.utils.JwtToken;
import com.sl.pmpapp.utils.MD5;


@Service("userDataImpl")
public class UserDataServiceImpl implements UserDataService {
	
	/**
	 * 登录验证
	 */
	@Override
	public Map<String, Object> checkLogin(Map<String, Object> map){
		Map<String, Object> map2= new HashMap<String, Object>();  //最终返回数据
		Map<String, Object> map3= new HashMap<String, Object>();  //返给接口的数据
		Map<String, Object> map4= new HashMap<String, Object>();  //data数据
		if(map.get("account")==null||map.get("account")==""||map.get("password")==""||map.get("password")==null){  
			map2.put("code","1003");
			map2.put("msg", "参数缺失");
		}else{
			
			String password=map.get("password").toString(); //MD5加密登录密码
			map3.put("uname", map.get("account"));
			map3=CEUtils.pmpEncrypt(map3); //加密传输数据
			String GET_URL = "http://192.168.0.60:8001/user/get_user_info";   //获取接口的数据
			HttpClientService hc=new HttpClientService();
			String data = hc.get(GET_URL,map3); //接口数据String类型
			Gson gson=new Gson();
			HashMap<String,Object> userData = gson.fromJson(data, HashMap.class);
			if(userData.get("pwd").equals(password)){  //密码相等，登录成功
				if(userData.get("operate_system").toString().contains("2")){
					HashMap<String,Object> userData2=new HashMap();
					userData2.put("uid", userData.get("uid"));
					userData2.put("oper_pwd", userData.get("oper_pwd"));
					map2.put("code","1000");
					map2.put("msg", "OK");
					map4.put("id", Integer.parseInt(userData.get("uid").toString())); //用户id
					String str="1,2";
					if(str.indexOf(userData.get("level")+"")!=-1){   //判断权限并返回是否拥有权限
						map4.put("auth",true);                             
						}else{
						map4.put("auth",false);
						}
					try {
						map4.put("token", JwtToken.createToken(userData2));
						String peojectid=JwtToken.getProject_id(map4.get("token").toString());
						map4.put("project_id", Integer.parseInt(peojectid));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					map2.put("data", map4);
				}else{
					map2.put("code","1006");
					map2.put("msg","没有登录权限"); 
				}
				
			}else{                  //登录失败
				map2.put("code","1005");
				map2.put("msg","用户名/密码错误"); 
			}
		}
		return map2;
	}

	
	/**
	 * 验证操作密码
	 */
	@Override 
	public Map<String, Object> checkOper_pwd(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> map2= new HashMap<String, Object>();
		if(map.get("opassword")==""||map.get("opassword")==null){
			map2.put("code","1003");
			map2.put("msg", "参数缺失");
		}else{
			String opassword =MD5.md5(map.get("opassword")+"",""); //MD5加密操作密码
			Gson gson=new Gson();
			HashMap<String,Object> userData = gson.fromJson(JwtToken.getAppUID(map.get("token")+""), HashMap.class);
			if(userData.get("oper_pwd").equals(opassword)){
				map2.put("code","1000");                                     
				map2.put("msg","OK"); 
			}else{
				map2.put("code","1005");
				map2.put("msg","操作密码错误"); 
			}
		}
		// TODO Auto-generated method stub
		return map2;
	}

	

	/**
	 * 退出登录
	 */
	@Override
	public Map<String, Object> quit(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

	

}
