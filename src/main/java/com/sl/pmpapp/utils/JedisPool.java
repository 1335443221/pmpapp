package com.sl.pmpapp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.pmpapp.config.RedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

@Service
public class JedisPool {
	@Autowired
    JedisPool jedisPool;
    
    @Autowired
    RedisConfig redisConfig;
    
    public Jedis getInstance(){
    	redisConfig.setPort(6380);
    	String s= redisConfig.getHost();
    	 JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
         jedisShardInfo1.setPassword(redisConfig.getPassword());
        Jedis jedis = new Jedis(jedisShardInfo1);
        return jedis;
    }
    
    
    public Jedis getInstance2(){
    	redisConfig.setPort(6379);
    	String s= redisConfig.getHost();
    	 JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
         jedisShardInfo1.setPassword(redisConfig.getPassword());
        Jedis jedis = new Jedis(jedisShardInfo1);
        return jedis;
    }
}
