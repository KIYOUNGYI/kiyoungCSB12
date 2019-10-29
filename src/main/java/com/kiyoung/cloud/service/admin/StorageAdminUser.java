package com.kiyoung.cloud.service.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kiyoung.cloud.common.MongoDBMetaDaoImpl;
import com.kiyoung.cloud.common.StorageConstant;
import com.kiyoung.cloud.common.dto.DCloudResultSet;
import com.kiyoung.cloud.storage.dao.PostgreSQLDaoImpl;
import com.kiyoung.cloud.storage.dto.FavoriteFolder;
import com.kiyoung.cloud.storage.dto.FileAccessMeta;
import com.kiyoung.cloud.storage.dto.WasteBasketInfo;
import com.kiyoung.cloud.storage.service.StorageObjectCommonService;
import com.kiyoung.cloud.storage.util.DateUtils;

@Service("StorageAdminUser")
public class StorageAdminUser {

	private Logger log = LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName());

	@Value("${ROOT_PATH}")
	private String storageRootPath;

//	@Value("${thumb_img_url}")
//	private String thumb_img_url;

	@Autowired
	private StorageObjectCommonService commonService;

	@Autowired
	private MongoDBMetaDaoImpl metaDao;

	@Autowired
	private PostgreSQLDaoImpl postgresqlDao;

	/**
	 * 신규 가입자 스토리지 기본폴더 밑 meta 생성
	 * 
	 * @param inMap
	 * @return json type 데이터
	 * @throws Exception
	 */
	public DCloudResultSet setStorageUser(HashMap<String, Object> inMap) throws Exception {
		DCloudResultSet resultSet = new DCloudResultSet();

		String userConnectId = inMap.get("userConnectID").toString();
		String businessConnectId = inMap.get("businessConnectID").toString();
		// [1] 사용자 디렉토리 생성
		createDirectory(userConnectId, businessConnectId);
		// [2] 사용자 디렉토리 FileAccessMeta 생성
		commonService.createUserDirectoryFileAccessMeta(userConnectId, businessConnectId);
		// [3] 휴지통 디렉토리 생성
		createWasteBasketDirectory(userConnectId, businessConnectId);
		// [4] 휴지통 디렉토리 메타 생성
		createWasteBasketFolderMeta(userConnectId, businessConnectId);
		// [5] 즐겨찾기 메타 생성
		createFavoriteMeta(userConnectId, businessConnectId);
		// [6] 권한주입
		// wireTabService.addAuth();

		return resultSet;
	}

	private void createWasteBasketFolderMeta(String userConnectId, String businessConnectId) throws Exception {
		WasteBasketInfo meta = new WasteBasketInfo();
		Map<String, Object> AccountMap = postgresqlDao.getAccountByUserConnectID(userConnectId);
		log.debug("AccountMap" + AccountMap.toString());
		String account = (String) AccountMap.get("user_email");
		meta.setOwnerUserAccount(account);
		metaDao.insertWasteMeta(meta);
	}

	private void createFavoriteMeta(String userConnectId, String businessConnectId) throws Exception {
		FavoriteFolder favorite = new FavoriteFolder();
		favorite.setFavoriteFolderName(StorageConstant.FAVORITEFOLDER);
		favorite.setFilePath(new ArrayList<String>());
		favorite.setFolderType(StorageConstant.FOLDERTYPE_FAVORITE);
		favorite.setLastModifyDate(DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss"));
		favorite.setLock(false);
		favorite.setMemo(false);
		favorite.setShareFolder(false);
		favorite.setUploadDate(DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss"));
		Map<String, Object> AccountMap = postgresqlDao.getAccountByUserConnectID(userConnectId);
		log.debug("AccountMap" + AccountMap.toString());
		String account = (String) AccountMap.get("user_email");
		log.debug("> " + account);
		favorite.setUploadUser(account);
		metaDao.insertFavoriteMeta(favorite);
	}

	private void createWasteBasketDirectory(String userConnectId, String businessConnectId) throws IOException {
		String wasteBasketPath = storageRootPath + File.separator + businessConnectId + File.separator + userConnectId
				+ StorageConstant.WASTE_BASKET_FOLDER;
		File file = new File(wasteBasketPath);

		if (file.getParentFile().exists() == false)
			return;
		else {
			FileUtils.forceMkdir(file);
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);
		}

	}

	private void createDirectory(String userConnectId, String businessConnectId) {
		String fullPath = storageRootPath + File.separator + businessConnectId + File.separator + userConnectId;
		log.debug("fullPath > " + fullPath);
		File userDirectory = new File(fullPath);
		if (!userDirectory.exists()) {
			try {
				FileUtils.forceMkdir(userDirectory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (userDirectory.exists()) {
			log.debug("> Directory exist.");
			userDirectory.setExecutable(true, false);
			userDirectory.setReadable(true, false);
			userDirectory.setWritable(true, false);
		}
	}

}
