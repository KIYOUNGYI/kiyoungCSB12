package com.kiyoung.cloud.storage.dto;

import java.io.Serializable;
import java.util.List;

import com.kiyoung.cloud.common.StorageConstant;


public class FavoriteFolder implements Serializable {

	private static final long serialVersionUID = 7746232772059506558L;
	
	/**
	 * 시스템 파일 패스형식의 링크된 파일 패스
	 */
	
	private List<String> filePath;
	
	private boolean memo = false;
	private boolean shareFolder = false;
	private boolean lock = false;
	
	private String lastModifyDate;
	private String uploadUser;
	private String favoriteFolderName = StorageConstant.FAVORITEFOLDER;
	private String folderType = StorageConstant.FOLDERTYPE_FAVORITE;
	private String uploadDate;
	private String userId;
	private String companyCode;
	private String _id;
	private String lockDate;
	
	public List<String> getFilePath() {
		return filePath;
	}

	public void setFilePath(List<String> filePath) {
		this.filePath = filePath;
	}

	public boolean isMemo() {
		return memo;
	}

	public void setMemo(boolean memo) {
		this.memo = memo;
	}

	public boolean isShareFolder() {
		return shareFolder;
	}

	public void setShareFolder(boolean shareFolder) {
		this.shareFolder = shareFolder;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}
	
	public String getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getFavoriteFolderName() {
		return favoriteFolderName;
	}

	public void setFavoriteFolderName(String favoriteFolderName) {
		this.favoriteFolderName = favoriteFolderName;
	}
	public String getFolderType() {
		return folderType;
	}

	public void setFolderType(String folderType) {
		this.folderType = folderType;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getLockDate() {
		return lockDate;
	}

	public void setLockDate(String lockDate) {
		this.lockDate = lockDate;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@Override
	public String toString(){
		return "filePath: " + filePath + ", " 
				+ "lastModifyDate: " + lastModifyDate + ", "
				+ "uploadDate: " + uploadDate + ", "
				+ "uploadUser: " + uploadUser + ", "
				+ "favoriteFolderName: " + favoriteFolderName + ", "
				+ "memo: " + memo + ", "
				+ "shareFolder: " + shareFolder + ", "
				+ "folderType: " + folderType + ", "
				+ "lock: " + lock + ", "
				+ "companyCode: " + companyCode + ", "
				+ "userId: " + userId;
	}

}
