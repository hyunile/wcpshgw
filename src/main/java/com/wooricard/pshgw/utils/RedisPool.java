package com.wooricard.pshgw.utils;

import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

@Component
public class RedisPool {
	private static final String MASTER_NAME = "mymaster";
	
	private static final int MAX_TOTAL = 30;
	private static final int MAX_IDLE = 10;
	private static final int MIN_IDLE = 5;
	
	@Value("${redis.server.ip}")
	private String redisIp;
	@Value("${redis.server.port}")
	private int redisPort;
	
	@Value("${redis.sentinel.ip1}")
	private String sentinelIp1;
	@Value("${redis.sentinel.ip2}")
	private String sentinelIp2;
	@Value("${redis.sentinel.ip3}")
	private String sentinelIp3;
	@Value("${redis.sentinel.port}")
	private int sentinelPort;

	@Value("${redis.sentinel.use}")
	private String useSentinel;

	@Value("${redis.database.id}")
	private int databaseId;
	
	@Value("${rest.auth.use}")
	private String useAuth;

	private JedisPool jedisPool;
	private JedisSentinelPool sentinelPool;
	
	@PostConstruct
	private void createPool() {
		if(! "Y".equalsIgnoreCase(useAuth)) {
			return;
		}
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(MAX_TOTAL);
		config.setMaxIdle(MAX_IDLE);
		config.setMinIdle(MIN_IDLE);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setMinEvictableIdleTimeMillis(60000);
		config.setTimeBetweenEvictionRunsMillis(30000);
		config.setNumTestsPerEvictionRun(3);
		config.setBlockWhenExhausted(true);
		
		if("Y".equalsIgnoreCase(useSentinel)) {
			HashSet<String> sentinels = new HashSet<String>();
			sentinels.add(sentinelIp1 + ":" + sentinelPort);
			sentinels.add(sentinelIp2 + ":" + sentinelPort);
			sentinels.add(sentinelIp3 + ":" + sentinelPort);
			
			sentinelPool = new JedisSentinelPool(MASTER_NAME, sentinels, config, 1000);
		}
		else {			
			jedisPool = new JedisPool(config, redisIp, redisPort, 1000);
		}
	}
	
	@PreDestroy
	private void cleanUp() {
		if(sentinelPool != null)
			sentinelPool.destroy();
		
		if(jedisPool != null)
			jedisPool.destroy();
	}
	
	public Jedis getResource() {
		Jedis jedis = null;
		if(sentinelPool != null) {
			if(sentinelPool != null)
				jedis = sentinelPool.getResource();
		}
		else {
			if(jedisPool != null)
				jedis = jedisPool.getResource();
		}
		
		if(jedis != null)
			jedis.select(databaseId);
		
		return jedis;
	}
}