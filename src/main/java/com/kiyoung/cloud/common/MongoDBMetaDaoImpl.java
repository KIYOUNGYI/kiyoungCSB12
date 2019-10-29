package com.kiyoung.cloud.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kiyoung.cloud.storage.dto.FavoriteFolder;
import com.kiyoung.cloud.storage.dto.FileAccessMeta;
import com.kiyoung.cloud.storage.dto.WasteBasketInfo;

@Repository
public class MongoDBMetaDaoImpl {
	
	@Autowired
	private CloudMongoDBUtil mongoDBUtil;
	
	public void insertAccessMeta(FileAccessMeta m) throws Exception {
		mongoDBUtil.insertAccessMeta(m);
	}
	
	public void insertFavoriteMeta(FavoriteFolder m) 
	{
		mongoDBUtil.insertFavoriteMeta(m);
	}
	
	public List<FileAccessMeta> getAccessMetaList(FileAccessMeta mc) throws Exception {
		List<FileAccessMeta> resultList = mongoDBUtil.selectAccessMetaList(mc);
		return resultList;
	}

	public void insertWasteMeta(WasteBasketInfo meta) {
		mongoDBUtil.insertWasteMeta(meta);
		
	}
	
	public FileAccessMeta getFileAccessMetaByFilePath(FileAccessMeta mc) throws Exception
	{
		FileAccessMeta m = null;
		
		m = mongoDBUtil.getFileAccessMetaByFilePath(mc);
		
		return m;
	}
	
	public FileAccessMeta getFileAccessMetaByFileUniqueKey(FileAccessMeta meta) 
	{
		return mongoDBUtil.getFileAccessMetaByFileUniqueKey(meta);
	}
	
	public FileAccessMeta getAccessMetaWithParentUKey(String parentFileUniquekey, String fileName) 
	{
		return mongoDBUtil.getAccessMetaWithParentUKey(parentFileUniquekey,fileName);
	}
	
}
