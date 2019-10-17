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

@Service("LoginSvc")
public class LoginService {

	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	
	@Autowired
	public PostgreSQLDaoImpl postgreSQLDaoImpl;
	
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
		int portalIDLastIndex = portalID.lastIndexOf("@");
		if((portalID.length()-1)==portalID.lastIndexOf("@")) portalID = portalID.substring(0,portalIDLastIndex);
		
		//[1] 세션 가져오기 db로
		List<Session> sessionList = postgreSQLDaoImpl.getUserSessionInfoByPortalId(portalID);
		log.debug("sessionList >"+sessionList.toString());
	
		
		// 토큰아이디로 레디스에 있는 데이터를 뽑아와서 포탈아이디와 파라미터로 넘어오는 포털아이디 값이 일치하는지 체크하는 로직(고도화 예정) 
		String stTokenId = UUID.randomUUID().toString();
		
		//[2] 세션 생성
		
//		Map<String, String> map = new HashMap<String, String>();
//		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
//		list.add(map);
//		resultSet.setList(list);
//		log.debug("list>"+list.toString());
		
		return resultSet;
	}
}
