package com.kiyoung.cloud.storage.service;

import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiyoung.cloud.common.StorageConstant;
import com.google.gson.Gson;
import com.kiyoung.cloud.service.storage.dto.Session;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class SessionService {

	@Autowired
	private JedisPool jedisPool;
	
	@Autowired
	private PostgreSQLDaoImpl postgreSQLDao;
	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	
	/**
	 * 세션 생성 or 연장
	 * @param stTokenId
	 * @param session
	 * @throws Exception
	 */
	public void setSession(String stTokenId, Session session) throws Exception 
	{
		Jedis jedis = jedisPool.getResource();
		try 
		{
			jedis.select(StorageConstant.SESSION_DB); // login
			jedis.set("CS_SESSION_" + stTokenId, new Gson().toJson(session));
			jedis.expire("CS_SESSION_" + stTokenId, StorageConstant.SESSION_TIMEOUT);
		}
		catch(Exception e) 
		{
			log.error("error:",e);
		}
		finally 
		{
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 세션 가져오기 
	 * @param tokenId
	 * @return
	 */
	public Session getSession(String tokenId) 
	{
		Jedis jedis = jedisPool.getResource();
		String body = "";
		boolean result = true;
		int idx=0;
		
		// step1 => body 값 세팅
		try 
		{
			jedis.select(StorageConstant.SESSION_DB); // login
			body = jedis.get("CS_SESSION_" + tokenId);
			
			// 예외처리 (3번정도 더 찾아보고)
			while(result) 
			{
				body = jedis.get("CS_SESSION_" + tokenId);
				idx++;
				if(!ObjectHelper.isEmpty(body) && !body.equals("OK")) result = false; 
				if(idx == 3) result = false;
			}
		}
		catch(Exception e) 
		{
			log.error("error:",e);
		}
		finally 
		{
			jedisPool.returnResource(jedis);
		}
		
		// step2 => 세션 연장
		Session dSession = null;
		try {
			dSession = new Gson().fromJson(body, Session.class);// json string 값을 Session 객체로 예쁘게~~~
			// 세션 연장
			expireSession(tokenId);
		} catch (Exception e) {	
			log.error("Redis getSession: {}", body);
			log.error("GSON Error : {}", e);
		}
		finally 
		{
			jedisPool.returnResource(jedis);
		}
		return dSession;
		
	}
	
	/**
	 * 
	 * 세션 연장한다.
	 * @author <a href="mailto:yky2798@gmail.com">이기영</a>
	 */
	public void expireSession(String tokenId) throws Exception {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.select(StorageConstant.SESSION_DB); // login
			jedis.expire("CS_SESSION_" + tokenId, StorageConstant.SESSION_TIMEOUT);
		} catch (Exception e) {
			log.error("error:",e);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/*
	 * 세션을 없앤다.
	 * @author <a href="mailto:yky2798@gmail.com">이기영</a>
	 */
	public void removeSession(String tokenId) throws Exception {
		log.debug("remove TokenID: {}", tokenId);
		
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.select(StorageConstant.SESSION_DB);
			Long i = jedis.del("CS_SESSION_"+tokenId);
			log.debug("result>"+Long.toString(i));
		} catch (Exception e) {
			log.error("error:",e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		
	}
}
