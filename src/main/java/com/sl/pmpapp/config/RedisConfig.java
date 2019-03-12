package com.sl.pmpapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="redis")
@PropertySource("classpath:/redis.properties")
public class RedisConfig {
	 private String host;
	    private int port;
	    private String password;
	    private int timeout;
	    private int poolMaxTotal;
	    private int poolMaxIdle;
	    private int poolMaxWait;
	    
	    public String getHost() {
	        return host;
	    }
	    public void setHost(String host) {
	        this.host = host;
	    }
	    public int getPort() {
	        return port;
	    }
	    public void setPort(int port) {
	        this.port = port;
	    }
	    public String getPassword() {
	        return password;
	    }
	    public void setPassword(String password) {
	        this.password = password;
	    }
}
