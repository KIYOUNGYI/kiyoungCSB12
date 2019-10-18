package com.kiyoung.cloud.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiyoung.cloud.common.dto.DCloudResultSet;
import com.kiyoung.cloud.service.storage.dto.Session;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;
import com.kiyoung.cloud.storage.service.SessionService;

@Service("LoginSvc")
public class LoginService {

	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	
	@Autowired
	public PostgreSQLDaoImpl postgreSQLDaoImpl;
	
	@Autowired
	public SessionService sessionService;
	
	/**
	 * 아이디와 토큰(레디스)가 인풋
	 * @param inMap
	 * @return
	 * @throws Exception
	 */
	public DCloudResultSet login(Map<String,Object> inMap) throws Exception {
		DCloudResultSet resultSet = new DCloudResultSet();
		String tokenId = inMap.get("TokenID").toString();//포탈 세션
		String portalID = inMap.get("portalID").toString();
		
		
		// 토큰아이디로 레디스에 있는 데이터를 뽑아와서 포탈아이디와 파라미터로 넘어오는 포털아이디 값이 일치하는지 체크하는 로직(고도화 예정) 
//		int portalIDLastIndex = portalID.lastIndexOf("@");
//		if((portalID.length()-1)==portalID.lastIndexOf("@")) portalID = portalID.substring(0,portalIDLastIndex);
		
		//[1] 세션 가져오기 db로  (리스트를 써야 할지는 의문) <- 일단 내비두자 // 어파치 지금 구상은 한개 가지고 오는것이다.
		List<Session> sessionList = postgreSQLDaoImpl.getUserSessionInfoByPortalId(portalID);
		log.debug("sessionList >"+sessionList.toString());
	
		String stTokenId = UUID.randomUUID().toString();
		//[2] 세션 생성
		sessionService.setSession(stTokenId, sessionList.get(0));
		
		Map<String, String> resultMap = new HashMap<String, String>();
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		resultMap.put("token", stTokenId);
		resultMap.put("info", sessionList.get(0).toString());
		resultList.add(resultMap);
		
		resultSet.setList(resultList);
//		log.debug("list>"+list.toString());
		
		return resultSet;
	}
	
	/**
	 * 로그아웃 
	 * @TODO 포탈세션에서 뽑은 데이터와 스토리지세션에서 뽑은 데이터 비교해서 예외처리하는 로직
	 * @param inMap
	 * @return
	 * @throws Exception
	 */
	public DCloudResultSet logout(Map<String,Object> inMap) throws Exception 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
		String tokenIDArr[] = inMap.get("TokenID").toString().split("@@");
		String tokenId = tokenIDArr[0]; //이건 스토리지 세션
		String oauthTokenId = tokenIDArr[1];// 이건 포탈 세션  (아직은...)
		sessionService.removeSession(tokenId);
		return resultSet;
	}
	
}
