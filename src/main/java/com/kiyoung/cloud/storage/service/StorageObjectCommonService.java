package com.kiyoung.cloud.storage.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.kiyoung.cloud.service.storage.dto.Session;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;
import com.kiyoung.cloud.storage.dto.UserAndBusinessConnectIdDto;


@Service("StorageObjectCommonSvc")
public class StorageObjectCommonService {

	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	@Value("${ROOT_PATH}")
	private String storageRootPath;
	
	@Autowired
	private PostgreSQLDaoImpl postgreSQLDaoImpl;
	
	public String getSystemPathByPath(String path, Session session) throws Exception 
	{
		return "";
	}
	
	public String getSystemPathByPath(String path) 
	{
		
//		String fileSystemRootPath = getFileSystemRootPath(, );

		return "";
	}
	
	
	/**
	 * duzon/businessconnectId/userConnectId
	 * 
	 * @param businessConnectId
	 * @param userConnectId
	 * @return
	 */
	public String getFileSystemRootPath(String businessConnectId, String userConnectId) {
		StringBuilder sb = new StringBuilder();
		sb.append(storageRootPath);
		sb.append('/');
		sb.append(businessConnectId);
		sb.append('/');
		sb.append(userConnectId);
		sb.append('/');
		return sb.toString();
	}
	
	public UserAndBusinessConnectIdDto getUserAndBizConnectIdByUserIdNotTokenId(String userId, String company_no) 
	{
		UserAndBusinessConnectIdDto connectId = null;
		
		
		
		return connectId;
	}

	/**
	 * 
	 * @param businessConnectID
	 * @param userConnectID
	 * @return
	 */
	public UserAndBusinessConnectIdDto getBizAndUserConnId(String businessConnectID, String userConnectID) {
		UserAndBusinessConnectIdDto userAndBusinessConnectIdDto = new UserAndBusinessConnectIdDto();
		userAndBusinessConnectIdDto.setBusinessConnectId(businessConnectID);
		userAndBusinessConnectIdDto.setUserConnectId(userConnectID);
		return userAndBusinessConnectIdDto;
	} 
	
	
	/**
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public UserAndBusinessConnectIdDto getBizAndUserConnId(String account) throws Exception {
		UserAndBusinessConnectIdDto userAndBusinessConnectIdDto = new UserAndBusinessConnectIdDto();
		HashMap<String,Object> inMap = (HashMap<String, Object>) postgreSQLDaoImpl.getBizAndUserConnId(account);
//		log.debug("inMap>"+inMap.toString());
		String bizId = (String) inMap.get("businessconnectid");
		String userId = (String) inMap.get("userconnectid");
		userAndBusinessConnectIdDto.setBusinessConnectId(bizId);
		userAndBusinessConnectIdDto.setUserConnectId(userId);
		return userAndBusinessConnectIdDto;
	} 
	
}
