package com.sl.pmpapp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sl.pmpapp.config.ClientUrl;
import com.sl.pmpapp.service.AlermDataService;
import com.sl.pmpapp.utils.CodeMsg;
import com.sl.pmpapp.utils.HttpClientService;
import com.sl.pmpapp.utils.Jipush;
import com.sl.pmpapp.utils.JwtToken;
import com.sl.pmpapp.utils.Result;


@Service("alermDataImpl")
public class AlermDataServiceImpl implements AlermDataService{
	
	@Autowired
	ClientUrl clientUrl;
	
	/**
	 * 报警列表
	 */
	@Override
	public Object alermList(Map<String, Object> map) {  
		Map<String, Object> map3= new HashMap<String, Object>();   //返回数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		
		if(map.get("pageSize")==null||map.get("pageSize")==""||map.get("pageNum")==""||map.get("pageNum")==null){  
			map.put("pageNum","1");
			map.put("pageSize", "20");
		}
		
			Map<String, Object> map4= new HashMap<String, Object>();   //返回数据
			map4.put("pagSize", map.get("pageSize"));
			map4.put("pagNum", map.get("pageNum"));
			map4.put("project_id", JwtToken.getProject_id(map.get("token")+""));   //根据token获取project_id
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   //把时间转换成年月日
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
	        c.add(Calendar.MONTH, -1);
	        Date m = c.getTime();
	        String mon = sdf.format(m);   //获取过去一个月的时间
	        map4.put("btime", mon);  //存到条件查询里
		
			String GET_URL = clientUrl.JAVACENTER+"alerm/AlermData";   //获取接口的数据
			HttpClientService hc=new HttpClientService();
			String data = hc.get(GET_URL,map4); //接口数据String类型
			
			Gson gson=new Gson();
		//	ArrayList<Map<String,Object>> alermData = gson.fromJson(data, ArrayList.class);
			
			JSONArray json = JSONArray.parseArray(data); 
			Map<String, Object> map5= new HashMap<String, Object>();
			if(json.size()<Integer.parseInt(map.get("pageSize").toString())){
				map5.put("is_lastpage", true);
			}else{
				map5.put("is_lastpage", false);
			}
			for (int i = 0; i < json.size(); i++) { 
				Map<String,Object> alermData=json.getJSONObject(i);
			
				map3=new HashMap<String, Object>();    //一条数据
	            map3.put("category_name",alermData.get("category_name"));  //报警类别
	            map3.put("log_time",alermData.get("log_time"));			  //报警时间
	            map3.put("location",alermData.get("location"));   		  //报警位置
	            map3.put("device_name",alermData.get("device_name"));  	  //设备
	            map3.put("is_deal",alermData.get("is_deal"));		//是否解决 1.解决   0.未解决
	            map3.put("id",alermData.get("id"));			  //报警id
	            list.add(map3);
	        } 
			
			map5.put("alermlist", list);
			return Result.success(map5);
	}

	
	/**
	 * 报警详情
	 */
	@Override
	public Object alermDetail(Map<String, Object> map) {
		Map<String, Object> map3= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map4= new HashMap<String, Object>(); 
		
		map4.put("pagNum", 1);  //第一页
		map4.put("pagSize", 100000);  //每页多少个
		if(map.get("time")==null||map.get("time")==""||map.get("id")==""||map.get("id")==null){  
			return CodeMsg.MISSING_PARAMETER;
		}else{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   //把时间转换成年月日
		Date date=null;
		try {
			date = sdf.parse(map.get("time").toString()); //报警时间
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date); 
		int day1=c1.get(Calendar.DATE); 
		c1.set(Calendar.DATE,day1-2); 
		String btime=new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());  //获取之前两天的时间
		map4.put("btime", btime);  //存到条件查询里
		
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date); 
		int day=c2.get(Calendar.DATE); 
		c2.set(Calendar.DATE,day+2); 
		String etime=new SimpleDateFormat("yyyy-MM-dd").format(c2.getTime());  //获取之后两天的时间
		map4.put("etime", etime);  //存到条件查询里
		
		
		
	
		String GET_URL = clientUrl.JAVACENTER+"alerm/AlermData";   //获取接口的数据
		HttpClientService hc=new HttpClientService();
		String data = hc.get(GET_URL,map4); //
		
		Gson gson=new Gson();
		//ArrayList<Map<String,Object>> alermData = gson.fromJson(data, ArrayList.class);
		
		
		JSONArray json = JSONArray.parseArray(data); 
		
		for (int i = 0; i < json.size(); i++) { 
			Map<String,Object> alermData=json.getJSONObject(i);
			
			 if(alermData.get("id").toString().equals(map.get("id")+"")||alermData.get("id")==map.get("id")){
				 
					Map<String, Object> mapid= new HashMap<String, Object>();	
					mapid.put("lid", map.get("id"));
					String url=clientUrl.JAVACENTER+"alerm/queryDealDetail";  //获取维修登记信息
					String data2 = hc.get(url,mapid); //
					HashMap<String,Object> data2Data = gson.fromJson(data2, HashMap.class);
				 
					map3.put("level",alermData.get("level")); 			      //报警等级
				    map3.put("category_name",alermData.get("category_name"));  //报警类别
				    map3.put("factory_name",alermData.get("factory_name"));    //所属区域
				    map3.put("location",alermData.get("location"));   		  //报警位置
				    map3.put("device_name",alermData.get("device_name"));  	  //设备
				    map3.put("config_desc",alermData.get("config_desc"));  	  //报警描述
				    map3.put("log_time",alermData.get("log_time"));			  //报警时间
				    map3.put("value",alermData.get("value"));		     	  //报警值
				    map3.put("th",alermData.get("th"));			              //正常值
				    map3.put("is_deal",alermData.get("is_deal"));			  //处理状态  1.解决   0.未解决
				    map3.put("operater",alermData.get("operater"));			  //处理人
				    map3.put("confirm_time",alermData.get("confirm_time"));	  //处理时间
				    if(data2Data==null||data2Data.get("repa_msg")==null){
				    	map3.put("repair_registration","");//维修登记
				    }else{
				    	map3.put("repair_registration",data2Data.get("repa_msg"));//维修登记
				    }
				    map3.put("id",alermData.get("id"));			              //报警id
			 }
        } 
		}
		return Result.success(map3);
	}


	@Override
	public Object alermPush(Map<String, Object> map) {
		
		String title=map.get("title").toString();
    	List<String> tags=new ArrayList<>();
    	tags.add("env=dev");
    	tags.add("projectId="+map.get("project_id"));
    	
//    	if(map.get("roleid")!=null&&map.get("roleid")!=""){
//    		tags.add("role="+map.get("roleid"));
//    	}
//    	if(map.get("roleName")!=null&&map.get("roleName")!=""){
//    		tags.add("roleName="+map.get("roleName"));
//    	}
    	
    	
    	JsonObject j=new JsonObject();
    	j.addProperty("log_time", map.get("log_time").toString());
    	j.addProperty("id", Integer.parseInt(map.get("id").toString()));
		int a=Jipush.sendToTagList(tags,title, "data",j, "报警推送");
		
		return a;
	}



}
