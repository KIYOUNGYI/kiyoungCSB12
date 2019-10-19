package com.kiyoung.cloud.storage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kiyoung.cloud.storage.dto.FileInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;

@Service("FileDownloadSvc")
public class FileDownloadService {

	
	@Autowired
	private PostgreSQLDaoImpl postgreSQLDaoImpl;
	
	@Autowired
	private SessionService sessionService;
	
	@Value("${ROOT_PATH}")
	private String storageRootPath;
	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());


	public DCloudResultSet fileDownload(HashMap<String,Object> inMap) 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
		String tokenIDArr[] = inMap.get("TokenID").toString().split("@@");
		String tokenId = tokenIDArr[0];
		String oauthTokenId = tokenIDArr[1];
		String filePathJson = inMap.get("FilePath").toString();
		String fileName = "";
		
		String[] filePaths = null;
		try {
			Gson gson = new Gson();
			filePaths = gson.fromJson(filePathJson, String[].class);
		} catch (JsonSyntaxException e) {
			resultSet.setResultCode("E4001");//Filepath 파라미터는 반드시 String[] type 이어야 합니다.
			return resultSet;
		}
		List<FileInfo> list = new ArrayList<FileInfo>();
		String path = filePaths[0];
		
		for (String filePath : filePaths) 
		{
			
		}
		return resultSet;
	}


}
