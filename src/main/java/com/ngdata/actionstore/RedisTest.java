package com.ngdata.actionstore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import redis.clients.jedis.Jedis;

public class RedisTest {

	public static void main(String[] args) {
		
    	ApplicationProperties.initialize();
		RedisConnectionHelper redisConnectionHelper = new RedisConnectionHelper();
		redisConnectionHelper.getJedisConnection();
				
		Jedis jedis = null;
		jedis = new RedisConnectionHelper().getJedisConnection();
		String strVal = jedis.get("foo");
		System.out.println("strVal = " + strVal);
		
	}
	
}
