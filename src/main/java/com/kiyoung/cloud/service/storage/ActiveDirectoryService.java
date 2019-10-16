package com.kiyoung.cloud.service.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kiyoung.cloud.common.dto.DCloudResultSet;

@Service("ActiveDirectorySvc")
public class ActiveDirectoryService {

	@Value("${AD_SERVER_DOMAIN}")
	private String serverDomain;
	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	public DCloudResultSet foo(HashMap<String, Object> param) 
	{
		DCloudResultSet resultSet = new DCloudResultSet();
		log.error("hello active ");
//		resultSet.setResultCode("200");
		log.error("serverDomain : "+serverDomain);
		log.error("param:"+param.toString());
		 List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		 HashMap<String, Object> inMap = new HashMap<String, Object>();
		 inMap.put("a", "val");
		 list.add(inMap);
	     resultSet.setList(list);
	     return resultSet;
	}
}
