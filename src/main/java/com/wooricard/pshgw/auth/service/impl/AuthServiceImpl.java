package com.wooricard.pshgw.auth.service.impl;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooricard.pshgw.auth.service.AuthService;
import com.wooricard.pshgw.common.Result;
import com.wooricard.pshgw.mobile.dto.MobileDTO;
import com.wooricard.pshgw.utils.RedisPool;
import com.wooricard.pshgw.utils.Utils;

import redis.clients.jedis.Jedis;

@Service
public class AuthServiceImpl implements AuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Autowired
	private RedisPool redisPool;
	
	private ObjectMapper objMapper;
	
	@Override
	public int issueAuthKey(MobileDTO dto) {
		try {
			if(! findUserInfo(dto)) {
				return Result.ERR_AUTH_FAILED;
			}
				
			String authKey = makeAuthKey(dto);
			dto.setAuth(authKey);
		} 
		catch (Exception e) {
			logger.error(String.format("authenticate 1  : %s : %s", e.getClass().getName(), e.getMessage()));
			return Result.ERR_SYSTEM;
		}
		
		return Result.SUCCESS;
	}

	@PostConstruct
	private void init() {
		objMapper = new ObjectMapper();
	}
	
	@Override
	public int authenticate(MobileDTO dto) {
		String authKey = dto.getAuth();
		String cuid = dto.getCuid();
		String appId = dto.getAppid();
		
		if(Utils.isEmpty(authKey))
			return Result.ERR_AUTHKEY_MISSING;
		
		try {
			if(! isAvailableAuthKey(appId, cuid, authKey))
				return Result.ERR_AUTH_FAILED;
		}
		catch(Exception e) {
			logger.error(String.format("authenticate 2 : %s : %s", e.getClass().getName(), e.getMessage()));
			return Result.ERR_SYSTEM;
		}

		return Result.SUCCESS;
	}	
	
	private boolean findUserInfo(MobileDTO dto) throws Exception {
		Jedis jedis = null;
		try {
			jedis = redisPool.getResource();

			String appId = dto.getAppid();
			String cuid = dto.getCuid();
			String deviceId = dto.getDevice();

			String pushKey = jedis.hget(appId + ":CUID", cuid);
			if(Utils.isEmpty(pushKey)) {
				return false;
			}

			JsonNode node = objMapper.readTree(pushKey);
			if(node == null || ! node.isArray()) {
				throw new Exception("Json string (pushkey) error.");
			}
			
			node = node.get(0);
			pushKey = node.asText();
			if(Utils.isEmpty(pushKey)) {
				return false;
			}

			String data = jedis.hget(appId + ":PUSHUSERINFO", pushKey);
			if(Utils.isEmpty(data)) {
				return false;
			}
			
			TypeReference<HashMap<String, String>> ref = new TypeReference<HashMap<String, String>>() {};
			HashMap<String, String> map = objMapper.readValue(data, ref);
			if(! deviceId.equals(map.get("deviceid"))) {
				return false;
			}
		}
		finally {
			if(jedis != null)
				jedis.close();
		}
		
		return true;
	}
	
	private String makeAuthKey(MobileDTO dto) throws Exception {
		String key = UUID.randomUUID().toString().replace("-", "");
		String appId = dto.getAppid();
		String cuid = dto.getCuid();
		
		Jedis jedis = null;
		try {
			jedis = redisPool.getResource();
			jedis.hset(appId + ":APIAUTH", cuid, key);
		}
		finally {
			if(jedis != null)
				jedis.close();
		}
		
		return key;
	}
	
	private boolean isAvailableAuthKey(String appId, String cuid, String authKey) throws Exception {
		Jedis jedis = null;
		try {
			jedis = redisPool.getResource();
			String data = jedis.hget(appId + ":APIAUTH", cuid);
			if(Utils.isEmpty(data)) {
				return false;
			}
			
			if(! data.equals(authKey)) {
				return false;
			}
		}
		finally {
			if(jedis != null)
				jedis.close();
		}
		
		return true;
	}
}
