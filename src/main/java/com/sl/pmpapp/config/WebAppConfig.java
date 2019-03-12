package com.sl.pmpapp.config;

import javax.servlet.MultipartConfigElement;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sl.pmpapp.Interceptor.TokenInterceptor;
 
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
 
	/**
	 * 配置拦截器,拦截token
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TokenInterceptor())
			.addPathPatterns("/*/**")
			.excludePathPatterns("/token/**")
			.excludePathPatterns("/user/get_user_info")
			.excludePathPatterns("/user/add_login_log")
			.excludePathPatterns("/user/get_user_project_info")
			.excludePathPatterns("/alerm/alermPush")
			;
	}

}
