package com.kiyoung.cloud.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kiyoung.cloud.storage.dto.FileAccessMeta;

@Repository
public class MongoDBMetaDaoImpl {
	
	@Autowired
	private CloudMongoDBUtil mongoDBUtil;

	public void insertAccessMeta(FileAccessMeta m) throws Exception {
		mongoDBUtil.insertAccessMeta(m);
	}
	
	public List<FileAccessMeta> getAccessMetaList(FileAccessMeta mc) throws Exception {
		List<FileAccessMeta> resultList = mongoDBUtil.selectAccessMetaList(mc);
		return resultList;
	}
}
