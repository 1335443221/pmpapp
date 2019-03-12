package com.sl.pmpapp.utils;

import java.text.DecimalFormat;
import java.util.Map;

import com.google.gson.Gson;

public class RunFuns {
	static DecimalFormat df = new DecimalFormat("#.0");  //保留一位小数
	static Gson gson=new Gson();  //转换格式
	/**
	 * 格式化object  保留一位小数
	 * @param a
	 * @return
	 */
	public static String dfformat(String a){
		return df.format(Double.parseDouble(fromJsonMap(a).toString()));
	}
	
	/**
	 * redis  map类型返回val
	 * @param a
	 * @return
	 */
	public static Object fromJsonMap(String a){
		return gson.fromJson(a, Map.class).get("val");
	}
			
}
