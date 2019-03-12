package com.sl.pmpapp.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class CEUtils {

	/**
	 * 加密
	 * @param map  传进来的参数
	 */
	public static Map<String,Object> pmpEncrypt(Map<String,Object> map){
		//获取当前时间的时间戳
		//long ts = Math.round(new Date().getTime()/1000);
		//map.put("timestamp",ts);
		
//		1.对除签名外的所有请求参数按key做升序排列,value无需编码
//		 List<Map.Entry<String,Object>> list = new ArrayList<Map.Entry<String,Object>>(map.entrySet());
//	        //然后通过比较器来实现排序
//	        Collections.sort(list,new Comparator<Map.Entry<String,Object>>() {
//	            //升序排序
//	            public int compare(Entry<String,Object> o1,
//	                    Entry<String,Object> o2) {
//	                return (o1.getValue()+"").compareTo(o2.getValue()+"");
//	            }
//	        });
	       
	        
	        List<Map.Entry<String, Object>> itmes = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
			
			//对所有传入的参数按照字段名从小到大排序
			//Collections.sort(items); 默认正序
			//可通过实现Comparator接口的compare方法来完成自定义排序
			Collections.sort(itmes, new Comparator<Map.Entry<String, Object>>() {
				@Override
				public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
					// TODO Auto-generated method stub
					return (o1.getKey().toString().compareTo(o2.getKey()));
				}
			});
	        
	        
	    //2.把参数名和参数值连接成字符串
	        String m="";
	        for(Map.Entry<String,Object> mapping:itmes){ 
	        	m+=mapping.getKey()+mapping.getValue();
	          } 
	        
		 //  String m=formatUrlParam(map, "utf-8", false);
	        
	    //3.用申请到的appkey（假如是666）连接到上面字符串的头部，然后进行32位MD5加密，最后将到得MD5加密摘要转化成大写，得到签名。
	        m="test_key"+m;
	        //MD5加密
	        m= MD5.md5(m,"");
	    //4.拼接获得完整请求参数
	        map.put("sign", m);
	        GetParam g=new GetParam();
	        m=g.getParams(map);
	    //5.对传输数据进行非对称加密
	        Rsa r=new Rsa();
	        m=r.encryptByPublicKey(m);
	        
	    //6.返回Map    
	        Map<String,Object> map2=new HashMap<String,Object>();
	        map2.put("app_id", "pmp");
	        map2.put("params", m);
	        return map2;
	}
	
	
	
	
	
	public static String formatUrlParam(Map<String, Object> param, String encode, boolean isLower) {
		String params = "";
		Map<String, Object> map = param;
		
		try {
			List<Map.Entry<String, Object>> itmes = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
			
			//对所有传入的参数按照字段名从小到大排序
			//Collections.sort(items); 默认正序
			//可通过实现Comparator接口的compare方法来完成自定义排序
			Collections.sort(itmes, new Comparator<Map.Entry<String, Object>>() {
				@Override
				public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
					// TODO Auto-generated method stub
					return (o1.getKey().toString().compareTo(o2.getKey()));
				}
			});
			
			//构造URL 键值对的形式
			StringBuffer sb = new StringBuffer();
			for (Map.Entry<String, Object> item : itmes) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
					String val = item.getValue().toString();
					val = URLEncoder.encode(val, encode);
					if (isLower) {
						sb.append(key.toLowerCase() + val);
					} else {
						sb.append(key + val);
					}
				}
			}
			
			params = sb.toString();
			if (!params.isEmpty()) {
				params = params.substring(0, params.length() - 1);
			}
		} catch (Exception e) {
			return "";
		}
		return params;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		  Map<String,Object> map=new HashMap<String,Object>();
		  map.put("project_id","2");
		 // map.put("meter_ids", "8");
		  //map.put("date_from", "2018-11-04");
		  map.put("factory_id","1");
		//  map.put("project_id", "2");
		  map.put("column_key", true);
		  
		// map.put("category_type","2");
		//获取当前时间的时间戳
		//long ts = Math.round(new Date().getTime()/1000);
		//map.put("timestamp",ts);
		
//		1.对除签名外的所有请求参数按key做升序排列,value无需编码
//		 List<Map.Entry<String,Object>> list = new ArrayList<Map.Entry<String,Object>>(map.entrySet());
//	        //然后通过比较器来实现排序
//	        Collections.sort(list,new Comparator<Map.Entry<String,Object>>() {
//	            //升序排序
//	            public int compare(Entry<String,Object> o1,
//	                    Entry<String,Object> o2) {
//	                return (o1.getValue()+"").compareTo(o2.getValue()+"");
//	            }
//	        });
	       
	        
	        List<Map.Entry<String, Object>> itmes = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
			
			//对所有传入的参数按照字段名从小到大排序
			//Collections.sort(items); 默认正序
			//可通过实现Comparator接口的compare方法来完成自定义排序
			Collections.sort(itmes, new Comparator<Map.Entry<String, Object>>() {
				@Override
				public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
					// TODO Auto-generated method stub
					return (o1.getKey().toString().compareTo(o2.getKey()));
				}
			});
	        
	        
	    //2.把参数名和参数值连接成字符串
	        String m="";
	        for(Map.Entry<String,Object> mapping:itmes){ 
	        	m+=mapping.getKey()+mapping.getValue();
	          } 
	        
		 //  String m=formatUrlParam(map, "utf-8", false);
	        
	    //3.用申请到的appkey（假如是666）连接到上面字符串的头部，然后进行32位MD5加密，最后将到得MD5加密摘要转化成大写，得到签名。
	        m="test_key"+m;
	        //MD5加密
	        m= MD5.md5(m,"");
	    //4.拼接获得完整请求参数
	        map.put("sign", m);
	        GetParam g=new GetParam();
	        m=g.getParams(map);
	    //5.对传输数据进行非对称加密
	        Rsa r=new Rsa();
	        m=r.encryptByPublicKey(m);
	        
	    //6.返回Map    
	        Map<String,Object> map2=new HashMap<String,Object>();
	        map2.put("app_id", "pmp");
	        map2.put("params", m);
	        System.out.println(map2);
	}
}
