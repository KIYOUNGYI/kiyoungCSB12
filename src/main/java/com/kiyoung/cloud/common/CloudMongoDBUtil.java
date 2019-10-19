package com.kiyoung.cloud.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.kiyoung.cloud.storage.dto.FileAccessMeta;


@Component
public class CloudMongoDBUtil {
	@Autowired
	MongoTemplate mongoOptions;
	
	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());
	
	public CloudMongoDBUtil() {}
	
	private static CloudMongoDBUtil dbUtil = new CloudMongoDBUtil();
	
	public CloudMongoDBUtil getInstance() 
	{
		return dbUtil;
	}
	

	public void insertAccessMeta(FileAccessMeta m) {
//		log.debug("FileAccessMeta m >> "+m);
		mongoOptions.save(m, "FileAccessMeta");
	}


	public List<FileAccessMeta> selectAccessMetaList(FileAccessMeta mc) {
		List<FileAccessMeta> resultList = new ArrayList<FileAccessMeta>();
		Criteria cri = new Criteria();
		
		if(null != mc.getParentFileUniqueKey()) {
			cri = Criteria.where("parentFileUniqueKey").is(mc.getParentFileUniqueKey());
		}else if(null != mc.getFileUniqueKey()) {
			cri = Criteria.where("fileUniqueKey").is(mc.getFileUniqueKey());
		}
		else{
			return resultList;
		}
		resultList = mongoOptions.find(new Query(cri), FileAccessMeta.class, "FileAccessMeta");
		
		log.debug("reulstList > "+resultList.toString());
		return null;
	}
}
