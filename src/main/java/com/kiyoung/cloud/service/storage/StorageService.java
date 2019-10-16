package com.kiyoung.cloud.service.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kiyoung.cloud.common.StorageConstant;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import com.kiyoung.cloud.common.exception.CSBException;
import com.kiyoung.cloud.service.storage.dto.Session;

@Service("StorageSvc")
public class StorageService {

	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	@Value("${ROOT_PATH}")
	private String storageRootPath;
	
	
	@Transactional
	public DCloudResultSet create(Exchange exchange) throws Exception 
	{
		
		DCloudResultSet resultSet = new DCloudResultSet();
		HashMap inMap = exchange.getIn().getMandatoryBody(HashMap.class);
		
		String directoryPath = inMap.get("DirectoryPath").toString();
		log.debug("create exchange > "+exchange.toString());
		
		inMap.put("FilePath", directoryPath);
		exchange.getIn().setBody(inMap);
		
		Map<String,Object> map = exchange.getProperties();
		log.debug("> map:"+map.toString());
		
		
		String service = (String) exchange.getIn().getHeader("service");
		String method = (String) exchange.getIn().getHeader("method");
		log.debug("service>"+ service+" method>"+method);
		
		// 일반 케이스
		if(service.equals("StorageSvc") && method.equals("create"))
		{
			return createFolder(exchange);
		}
		// 예외 케이스 // 로그인 api 호출했는데 해당 경로가 없는 경우
		else 
		{
			
		}
		
		return resultSet;
	}


	private void createUserRootFolder(String rootSystemPath) throws CSBException {
		//[1] session 뽑고 (생략)
		// 루트시스템패스로 파일 만들고 
//			String rootSystemPath = commonService.getFileSystemPath(account, session);
		log.debug("createUserRootFolder >");
		String path = rootSystemPath;
		File file = new File(path);
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e2) {
			log.error("createFail", e2);
			throw new CSBException("createFail: " + rootSystemPath);
		}
		try {file.setExecutable(true, false);file.setReadable(true, false);file.setWritable(true, false);} catch (Exception e) {log.error("폴더 전체 권한주기 에러", e);}
	}


	
	private DCloudResultSet createFolder(Exchange exchange) throws InvalidPayloadException, CSBException {
		DCloudResultSet resultSet = new DCloudResultSet();
		// 토큰뽑고, 경로 뽑고
		HashMap<String, Object> inMap = exchange.getIn().getMandatoryBody(HashMap.class);
		String directoryPath = inMap.get("FilePath").toString();
		
		Session session = new Session();
		
		// 치환된 경로
		String path = "/duzon/company1/user1/dummy";

		if(log.isDebugEnabled()) log.debug("createFolder FileSystemPath : " + path);
		File file = new File(path);
		createFolderAndMeta(file,session);
		return resultSet;
	}


	private void createFolderAndMeta(File file, Session session) throws CSBException {
//		String rootPath = storageRootPath + File.separator + session.getBusinessConnectID() + File.separator + session.getUserConnectID();
//		String rootPath = storageRootPath + File.separator + "company1"+File.separator+"user1";
//		String directoryPath = file.getPath().substring(rootPath.length());
//		String absolutePath2 = file.getParentFile().getParentFile().getAbsolutePath();
//		log.debug("absolutePath not root : " + absolutePath2);
		
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e2) {
			log.error("createFail", e2);
		}
		try {
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);
		} catch (Exception e) {
			log.error("폴더 전체 권한주기 에러", e);
		}
	}
}
