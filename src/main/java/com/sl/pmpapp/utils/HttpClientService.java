package com.sl.pmpapp.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import org.springframework.stereotype.Service;

/**
 * @author cloud
 * @description
 * @date 2017年06月15日
 */
@Service
public class HttpClientService {

  private final Logger logger = LoggerFactory.getLogger(HttpClientService.class);

  private CloseableHttpClient httpClient;
  private RequestConfig requestConfig;

  /**
   * 处理get请求.
   *
   * @param url 请求路径
   * @return json
   */
public  String get(String GET_URL,Map<String,Object> map){
	GetParam getParam=new GetParam();
	try {
		String params = getParam.getParams(map);
        URL url = new URL(GET_URL+"?"+params);    // 把字符串转换为URL请求地址
        System.out.println(url);
       HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
       connection.connect();// 连接会话
       // 获取输入流
       BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {// 循环读取流
           sb.append(line);
        }
        br.close();// 关闭流
       connection.disconnect();// 断开连接
      return  sb.toString();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("失败!");
        return null;
    }

}
}