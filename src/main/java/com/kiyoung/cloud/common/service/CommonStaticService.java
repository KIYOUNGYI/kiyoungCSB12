package com.kiyoung.cloud.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.kiyoung.cloud.common.MongoDBMetaDaoImpl;
import com.kiyoung.cloud.common.StorageConstant;
import com.kiyoung.cloud.common.exception.CSBException;
import com.kiyoung.cloud.service.storage.dto.Session;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;
import com.kiyoung.cloud.storage.dto.DetailSearch;
import com.kiyoung.cloud.storage.dto.FileAccessMeta;
import com.kiyoung.cloud.storage.dto.FileInfo;
import com.kiyoung.cloud.storage.service.SessionService;
import com.kiyoung.cloud.storage.service.StorageObjectCommonService;
import com.kiyoung.cloud.storage.util.DateUtils;

public class CommonStaticService {

	private static Logger log = LoggerFactory.getILoggerFactory().getLogger("BIZ");

	@Value("${rootMetaPath}")
	private String rootMetaPath;

	@Value("${ROOT_PATH}")
	private String storageRootPath;

	@Autowired
	public PostgreSQLDaoImpl postgreSQLDaoImpl;

	@Autowired
	public SessionService sessionService;

	@Autowired
	public MongoDBMetaDaoImpl mongoDaoImpl;

	@Deprecated
	public static List<File> addDetailSearchFileInfo(DetailSearch detailSearch, List<FileInfo> list, String userId, Session session, File directory, boolean recursive, StorageObjectCommonService commonService)
	{
		List<File> subDirectoryList = null;
		
		if (recursive)subDirectoryList = new ArrayList<File>();
		
		File[] files = directory.listFiles();
//		log.debug("files before loop >>> "+Arrays.asList(files));
		for (File child : files) // directory 안의 모든 파일 또는 디렉토리를 child에 넣는다.
		{
			if(!detailSearch.getSearchText().isEmpty()) 
			{}
			addSubDir(recursive, subDirectoryList, child);// 목적 : 서브디렉토리 리스트 추가(디렉토리 검색을 위해)
		}
		log.debug("subDirectoryList files after loop >>> "+subDirectoryList.toString());
		
		
		return subDirectoryList;
	}
	
	
	public static List<File> addSearchFileInfo(String searchText, List<FileInfo> list, String userId, Session session, File directory, boolean recursive, StorageObjectCommonService commonService) throws Exception
	{
//		if(directory.exists()==false)throw new CSBException("",null);
//		if(directory.exists()==false)throw new CSBException("",null);
//		log.debug("userId>>"+userId);
		List<File> subDirectoryList = null;
		if (recursive)subDirectoryList = new ArrayList<File>();
		
		File[] files = directory.listFiles();
//		log.debug("files before loop >>> "+Arrays.asList(files));
		for (File child : files) // directory 안의 모든 파일 또는 디렉토리를 child에 넣는다.
		{
			// 파일명이 맞으면 리스트에 삽입
			// 탐색할 것이 디렉토리면 
			if(child.getName().toLowerCase().contains(searchText)) // 파일,폴더명 검색 (코어) 
			{
				FileAccessMeta meta = commonService.loadFileMetaByFileSystemPathMongoDB(child);
				if(meta==null)continue;
				FileInfo fileInfo = CommonStaticService.convertFileToFileInfo(child, commonService, meta);
				list.add(fileInfo);
			}
			addSubDir(recursive, subDirectoryList, child);// 목적 : 서브디렉토리 리스트 추가(디렉토리 검색을 위해)
		}
		log.debug("subDirectoryList files after loop >>> "+subDirectoryList.toString());
		return subDirectoryList;
	}
	
	// 목적 : 서브디렉토리 리스트 추가(디렉토리 검색을 위해)
	private static void addSubDir(boolean recursive, List<File> subDirectoryList, File child) {
		if (recursive && child.isDirectory() && !child.isFile()) {
			subDirectoryList.add(child);
		}
	}
	
	public static void addFileInfoForSharedAnalyze(List<FileInfo> list, HashMap<String, Object> paramMap,StorageObjectCommonService commonService) throws Exception {

		String fullPath = (String) paramMap.get("filePath");
		File directory = new File(fullPath);
		File[] files = directory.listFiles();
		log.debug(">>>>" + Arrays.asList(files).toString());
		List<File> subDirectoryList = null;
		List<FileAccessMeta> metaList = new ArrayList<FileAccessMeta>();

		if (files == null || files.length == 0)return;

		FileAccessMeta accessInfoMap = commonService.loadFileMetaByFileSystemPathMongoDB(directory);
		log.debug("accessInfoMap >" + accessInfoMap.toString());
		if (accessInfoMap != null) {
			metaList = commonService.loadFileMetaByFileSystemPathMongoDBChildList(directory);
			log.debug("metaList > "+metaList.toString());
		}
		
		//디렉토리내 파일 가지고 오기
		for(File child:files) 
		{
			if(!child.getName().equals(StorageConstant.WASTE_BASKET_FOLDER)) 
			{
				FileAccessMeta meta = null;
				
				// [1] child 에 해당하는 메타 고르기
				for(FileAccessMeta m: metaList) 
				{
					if(m.getFileName().equals(child.getName())) 
					{
						meta = m;
						break;
					}
				}
				
				// 메타없을시 생성하는 예외처리 (차후 개발)
				
				FileInfo fileInfo = null;
				fileInfo = CommonStaticService.convertFileToFileInfo(child,commonService,meta);
				list.add(fileInfo);
			}
		}
	}

	public static FileInfo convertFileToFileInfo(File file, StorageObjectCommonService commonService,FileAccessMeta meta) {
		
		FileInfo fileInfo = new FileInfo();
		String fileName = file.getName();
		
		fileInfo.setDirectory(file.isDirectory());
		fileInfo.setFileName(fileName);
		fileInfo.setLastModifyDate(DateUtils.getDateStr(new Date(file.lastModified()), "yyyyMMddHHmmss"));
		if(null != meta.getCreateDate() && !meta.getCreateDate().isEmpty()) {
			fileInfo.setCreateDate(DateUtils.getDateStr(new Date(Long.valueOf(meta.getCreateDate())), "yyyyMMddHHmmss"));
		}
		fileInfo.setMimeType(getMimeType(file));
		
		fileInfo.setRealPath(file.getAbsolutePath());
		if(meta.isBizShareFolder()) {
			//추후 개발
		} else {
			//원본 코드 (첫라인 주석)
			fileInfo.setPath(file.getAbsolutePath());
		}
		fileInfo.setSize(file.length());
		fileInfo.setLastModifyUser(meta.getLastModifyUser());
		/*이기영 2017-09-12 코드수정*/
		//(원본)fileInfo.setUploadUser(meta.getUploadUserId());
		String uploadUser = meta.getUploadUserId();
		log.debug("CommonstaticService convertFileToFileMethod uploadUser : "+uploadUser);
		uploadUser = meta.getUploadUserId();
		fileInfo.setUploadUser(uploadUser);
		fileInfo.setUploadDate(DateUtils.getDateStr(new Date(meta.getUploadDate()), "yyyyMMddHHmmss"));
		fileInfo.setShareFolder(meta.isShareFolder());		
		fileInfo.setFileVersion(meta.getFileVersion());
		fileInfo.setFavorite(meta.isFavorite());
		fileInfo.setMemo(meta.isMemo());
		fileInfo.setLock(meta.isLock());
		fileInfo.setFolderType(meta.getFolderType());
		fileInfo.setThumbNailPath(meta.getThumbNailPath());
		fileInfo.setStreamUniqueKey(meta.getStreamUniqueKey());
		fileInfo.setLevel(meta.getLevel());
		if(null != meta.getCreateDate() && !meta.getCreateDate().isEmpty()) {
			fileInfo.setCreateDate(DateUtils.getDateStr(new Date(Long.valueOf(meta.getCreateDate())), "yyyyMMddHHmmss"));
		}
		fileInfo.setEncDate(meta.getEncDate());
		fileInfo.setEncUserId(meta.getEncUserId());
		
		log.debug("fileInfo Meta :{} ", meta.toString());
		return fileInfo;
	}
	
	public static String getMimeType(File file) {
		String mimeType = "ETC";
		if(file.isDirectory()) {
			mimeType = "FOLDER";
		} else {
			String extension = FilenameUtils.getExtension(file.getName());
			HashMap<String,List<String>> extMap = StorageConstant.FileInfoMap;
			Set<String> keySet = extMap.keySet();
			for(String extKey : keySet) {
				List<String> list = extMap.get(extKey);
				if(list.contains(extension.toLowerCase())) {
					mimeType = extKey;
					break;
				}
			}
		}
		return mimeType;
	}
}
