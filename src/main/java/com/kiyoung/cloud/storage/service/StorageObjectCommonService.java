package com.kiyoung.cloud.storage.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kiyoung.cloud.common.MongoDBMetaDaoImpl;
import com.kiyoung.cloud.common.StorageConstant;
import com.kiyoung.cloud.service.storage.dto.Session;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;
import com.kiyoung.cloud.storage.dto.FileAccessMeta;
import com.kiyoung.cloud.storage.dto.UserAndBusinessConnectIdDto;


@Service("StorageObjectCommonSvc")
public class StorageObjectCommonService {

	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	@Value("${rootMetaPath}")
	private String rootMetaPath;
	
	@Value("${ROOT_PATH}")
	private String storageRootPath;
	
	@Value("${INDEX_STORAGE_COUNT}")
	private int index_storage_count;
	
	@Value("${indexRootPath}")
	private String indexRootPath;
	
	@Autowired
	private PostgreSQLDaoImpl postgreSQLDaoImpl;
	
	@Autowired
	private MongoDBMetaDaoImpl metaDao;
	
	@Produce
    ProducerTemplate producer;
	

	public String getIndexFilePath(String account) throws Exception 
	{
		UserAndBusinessConnectIdDto ubDto = getBizAndUserConnId(account);
		StringBuilder sb = new StringBuilder();
		sb.append(indexRootPath);
		sb.append(File.separator);
		sb.append(ubDto.getBusinessConnectId());
		sb.append(File.separator);
		sb.append(ubDto.getUserConnectId());
		sb.append(File.separator);
		return sb.toString();
	}
	
	public String getSystemPathByPath(String path, Session session) throws Exception 
	{
		return "";
	}
	
	public String getSystemPathByPath(String path) 
	{
		
//		String fileSystemRootPath = getFileSystemRootPath(, );

		return "";
	}
	
	
	public void insertAccessMeta(FileAccessMeta meta) throws Exception 
	{
		metaDao.insertAccessMeta(meta);
	}
	
	//메타 저장 & 파일 저장
	public void createUserDirectoryFileAccessMeta(String userConnectId, String businessConnectId) throws Exception {
		FileAccessMeta meta = null;
		meta = new FileAccessMeta();
		meta.setFileName(userConnectId);
		meta.setUploadUserId("");
		meta.setLastModifyUser("");
		meta.setUploadDate(new Date().getTime());
		meta.setFolderType(StorageConstant.FOLDERTYPE_NORMAL);
		meta.setFileUniqueKey(UUID.randomUUID().toString());
		String fullPath = storageRootPath+ File.separator+ businessConnectId+File.separator+userConnectId;
		meta.setSystemPath(fullPath);
		meta.set_id(meta.getFileUniqueKey());
		metaDao.insertAccessMeta(meta);
		
		String metaPath = fullPath.replace(storageRootPath, rootMetaPath)+File.separator + StorageConstant.UUID_META_NAME;;
		log.debug("metaPath > "+metaPath);
		File file = new File(metaPath);
		if(file.getParentFile().exists() == false) {
			FileUtils.forceMkdir(file.getParentFile());
		}
		FileUtils.writeStringToFile(file, meta.getFileUniqueKey());
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
	
	
	public String getFileSystemPath(String objecPath, Session session) 
	{
		String[] objectInfo = getUserAccountAndObjectPath(objecPath);
		
		return "";
	}
	
	public String[] getUserAccountAndObjectPath(String objectPath) {
		File tmp = new File(objectPath);
		int index = org.apache.commons.lang.StringUtils.indexOf(tmp.getPath(), File.separator);
		if (index < 0)
			return new String[] {objectPath, org.apache.commons.lang.StringUtils.EMPTY};
		if(log.isDebugEnabled()) log.debug("getUserAccountAndObjectPath objectPath : ["+objectPath+"]     index : ["+index+"]");		
		String userId = org.apache.commons.lang.StringUtils.substring(objectPath, 0, index);
		String path = removeUnusedSeperate(org.apache.commons.lang.StringUtils.substring(objectPath, index));
		return new String[]{userId, path};
	}
	
	
	private String removeUnusedSeperate(String path) {
		if (path == null)return null;
		
		boolean isSeperator = false;
		if (File.separator.equals("\\")) {
			path = org.apache.commons.lang.StringUtils.replace(path, "\\", "/");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < path.length(); i++) {
			char ch = path.charAt(i);
			if (ch == '/') {
				if (isSeperator == false) {
					isSeperator = true;
					sb.append(ch);
				} 
			} else {
				isSeperator = false;
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public FileAccessMeta loadFileMetaByFileSystemPathMongoDB(File file) throws Exception 
	{
		FileAccessMeta m = null;
		FileAccessMeta result = null;
		if(file.isDirectory()) 
		{
			String fk = getMetaUidKey(file);
			if(fk!=null) 
			{
				log.debug("fk->"+fk);
				m = new FileAccessMeta();
				m.setFileUniqueKey(fk);
				
				result = metaDao.getFileAccessMetaByFileUniqueKey(m);
			}
		}
		// 2019-10-28 구현
		else 
		{
			String pfk = getMetaUidKey(file.getParentFile());
			if(pfk!=null) 
			{
				result = metaDao.getAccessMetaWithParentUKey(pfk, file.getName().trim());
			}
		}
		
		log.debug("result>"+result);
		
		return result;
	}
	
	
	public List<FileAccessMeta> loadFileMetaByFileSystemPathMongoDBChildList(File file) throws IOException, Exception 
	{
		String fk = getMetaUidKey(file);
		FileAccessMeta mc = new FileAccessMeta();
		mc.setParentFileUniqueKey(fk);
		log.debug(">>>>"+fk);
		List<FileAccessMeta> list = metaDao.getAccessMetaList(mc);
		return list;
	}
	
	
	
	public String getMetaUidKey(File file) throws FileNotFoundException, IOException 
	{
		String filePath = file.getPath().replaceAll("\\\\", "/").replaceAll(storageRootPath, rootMetaPath);
		String path = filePath + File.separator + StorageConstant.UUID_META_NAME;
		File f = new File(path);
		String fk = null;
		if(f.exists()) 
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			fk =  br.readLine();
			br.close();
		}
		return fk;
	}
	
	public boolean checkFileExist(File file, File[] listFiles) {
		if (null != listFiles && listFiles.length > 0) {
			for (File f : listFiles) {
				if (file.getName().toLowerCase().equals(f.getName().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void saveFileAccessMeta(File uploadFile, String account, String fullPath, File file, FileAccessMeta meta,String fileName) throws Exception {
		String uuid = UUID.randomUUID().toString();
		meta.setSystemPath(file.getAbsolutePath() + "/" + fileName);
		meta.setFileName(uploadFile.getName());
		meta.setUploadUserId(account);
		meta.setLastModifyUser(account);
		meta.setFileUniqueKey(uuid);
		meta.setUploadDate(new Date().getTime());
		meta.set_id(uuid);
		//이게 고민
		File t = new File(fullPath);
		FileAccessMeta pm = loadFileMetaByFileSystemPathMongoDB(t);
		meta.setParentFileUniqueKey(pm.getFileUniqueKey());
//		log.debug("saveFileAccessMeta meta >>> "+meta.toString());
		insertAccessMeta(meta);
	}
	
	public void setIndexData(String indexRootPath, String userData) throws Exception {
		try 
		{
			File indexRoot = new File(indexRootPath);
			String rootName = indexRoot.getName();

			int indexQueueNum = Math.abs(rootName.hashCode()) % index_storage_count;
			if (log.isDebugEnabled())log.debug("setIndexData rabbitMQ index_storage_count : " + index_storage_count + "     rootName : "+ rootName + "     indexQueueNum : " + indexQueueNum);
			try {
				if (indexQueueNum == 0) {
					producer.sendBody("direct:rabbitMQ1", userData);
				} else if (indexQueueNum == 1) {
					producer.sendBody("direct:rabbitMQ1", userData);
//					producer.sendBody("direct:rabbitMQ2", userData);
				}
			} catch (Exception e) {
				log.error("setIndexData Error : " + e.getMessage() + " class : " + e.getClass() + "     indexRootPath: "+ indexRootPath + "     userData : " + userData);
				log.error("error:",e);
			}
		} catch (Exception e) {
			log.error("setIndexData Error : " + e.getMessage() + " class : " + e.getClass() + "     indexRootPath: "+ indexRootPath);
			log.error("error:",e);
		}
		// rabbitMQ end
	}

}
