package com.sl.pmpapp.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public class GetParam {
	public String getParams(Map<String,Object> map){
		String params="";
		int i=0;
		for (Object in : map.keySet()) {
			String object = map.get(in).toString();
			try {
				object=URLEncoder.encode(object, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
             params+=in+"="+object;
             i++;
             if(i<map.size()){
            	 params+="&";
             }
             
	        }
		return params;
	}
}
