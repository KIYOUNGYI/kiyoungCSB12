package com.kiyoung.cloud.storage.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.component.http.helper.CamelFileDataSource;
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
	
	public DCloudResultSet fileDownloadDummy(Exchange exchange) throws InvalidPayloadException, IOException 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
		Map inMap = exchange.getIn().getMandatoryBody(Map.class);
		File file = new File("/duzon/company1/user1/hello.txt");
		
		Map<String, DataHandler> attachments = new HashMap<String, DataHandler>();
		
		log.debug(">> " + new CamelFileDataSource(file, file.getName()).getName().toString());;
		DataHandler value = new DataHandler(new CamelFileDataSource(file, file.getName()));
		attachments.put(file.getName(), value);
		log.debug("value.getName > " +value.getName());
		log.debug("value.content > " +value.getContent().toString());
		log.debug("attachments > "+attachments.toString());
		exchange.getOut().setAttachments(attachments);
		exchange.setProperty("rPath", "/duzon/company1/user1/hello.txt");
		log.debug("getIn > "+exchange.getIn().toString());
		log.debug("getout > "+exchange.getOut().toString());// body is null 
		log.debug("getout headers >"+exchange.getOut().getHeaders().toString());
//		log.debug("getout body > "+exchange.getOut().getBody().toString());
//		log.debug("getout mandatory body > "+exchange.getOut().getMandatoryBody().toString());
		log.debug("context >"+ exchange.getContext().toString());
		log.debug("properties >"+exchange.getProperties().toString());
		log.debug("exchange > "+exchange.toString());

		return resultSet;
	}


}
