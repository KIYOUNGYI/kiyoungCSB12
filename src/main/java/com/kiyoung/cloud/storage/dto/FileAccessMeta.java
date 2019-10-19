package com.kiyoung.cloud.storage.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

//import com.duzon.dcloud.storage.dto.EncFileAuth;
//import com.duzon.dcloud.storage.dto.FileAccessGuestInfo;
//import com.duzon.dcloud.storage.dto.FileAccessInfo;
//import com.duzon.dcloud.storage.dto.FileAccessMeta;

public class FileAccessMeta implements Serializable {

	private static final long serialVersionUID = -7644753488400458585L;

	private String _id;
	private String fileName;
	private String uploadUserId;
	private String lastModifyUser;
	private String folderType;
	private boolean shareFolder = false;
	private boolean bizShareFolder = false;
	private boolean memo = false;
	private boolean favorite = false;
	private boolean lock = false;
	private long uploadDate;
	private int fileVersion = 0;
//	private List<FileAccessInfo> accessableBusinessConnectIdList;
//	private List<FileAccessInfo> accessableOrgConnectIdList;
//	private List<FileAccessInfo> accessableUserConnectIdList;
	private boolean publicAccess;
	private String fileUniqueKey;
	private String bizId;
	// 2013.08.27 윤혁진 추가
	private long fileSize;
	// 2013.08.28 배지숙 추가 게스트 공유 정보
//	private List<FileAccessGuestInfo> accessableGuestIdList;
	// 2014.03.31 배지숙(썸네일 path) 추가
	private String thumbNailPath;
	private String tags;
	private String streamUniqueKey;
	private String createDate;
	private String parentFileUniqueKey;
	private String systemPath;
	private int level;
	private String lockDate;
	private String ownerUserId;
	private long encFileSize;
	private boolean encYn;
	private String encDate;
	private String decDate;
	private String hashData;
	private List<String> modifyUserList;
	private String orgnFileName;
//	private List<EncFileAuth> encFileAuthList;
	private String encKey;
	private String encUserId;
	private Map<String, FileAccessMeta> zipFileSubMetaMap;

	public FileAccessMeta() {

	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isPublicAccess() {
		return publicAccess;
	}

	public void setPublicAccess(boolean publicAccess) {
		this.publicAccess = publicAccess;
	}

	public String getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(String uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public long getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(long uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(int fileVersion) {
		this.fileVersion = fileVersion;
	}

	public boolean isShareFolder() {
		return shareFolder;
	}

	public void setShareFolder(boolean shareFolder) {
		this.shareFolder = shareFolder;
	}

	public boolean isMemo() {
		return memo;
	}

	public void setMemo(boolean memo) {
		this.memo = memo;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public String getFolderType() {
		return folderType;
	}

	public void setFolderType(String folderType) {
		this.folderType = folderType;
	}

	public String getFileUniqueKey() {
		return fileUniqueKey;
	}

	public void setFileUniqueKey(String fileUniqueKey) {
		this.fileUniqueKey = fileUniqueKey;
	}

	public boolean isBizShareFolder() {
		return bizShareFolder;
	}

	public void setBizShareFolder(boolean bizShareFolder) {
		this.bizShareFolder = bizShareFolder;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

//	public List<FileAccessGuestInfo> getAccessableGuestIdList() {
//		return accessableGuestIdList;
//	}
//	public void setAccessableGuestIdList(List<FileAccessGuestInfo> accessableGuestIdList) {
//		this.accessableGuestIdList = accessableGuestIdList;
//	}
	public String getThumbNailPath() {
		return thumbNailPath;
	}

	public void setThumbNailPath(String thumbNailPath) {
		this.thumbNailPath = thumbNailPath;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStreamUniqueKey() {
		return streamUniqueKey;
	}

	public void setStreamUniqueKey(String streamUniqueKey) {
		this.streamUniqueKey = streamUniqueKey;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getParentFileUniqueKey() {
		return parentFileUniqueKey;
	}

	public void setParentFileUniqueKey(String parentFileUniqueKey) {
		this.parentFileUniqueKey = parentFileUniqueKey;
	}

	public String getSystemPath() {
		return systemPath;
	}

	public void setSystemPath(String systemPath) {
		this.systemPath = systemPath;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLockDate() {
		return lockDate;
	}

	public void setLockDate(String lockDate) {
		this.lockDate = lockDate;
	}

	public String getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public long getEncFileSize() {
		return encFileSize;
	}

	public void setEncFileSize(long encFileSize) {
		this.encFileSize = encFileSize;
	}

	public boolean isEncYn() {
		return encYn;
	}

	public void setEncYn(boolean encYn) {
		this.encYn = encYn;
	}

	public String getEncDate() {
		return encDate;
	}

	public void setEncDate(String encDate) {
		this.encDate = encDate;
	}

	public String getDecDate() {
		return decDate;
	}

	public void setDecDate(String decDate) {
		this.decDate = decDate;
	}

	public String getHashData() {
		return hashData;
	}

	public void setHashData(String hashData) {
		this.hashData = hashData;
	}

	public List<String> getModifyUserList() {
		return modifyUserList;
	}

	public void setModifyUserList(List<String> modifyUserList) {
		this.modifyUserList = modifyUserList;
	}

	public String getOrgnFileName() {
		return orgnFileName;
	}

	public void setOrgnFileName(String orgnFileName) {
		this.orgnFileName = orgnFileName;
	}

	public String getEncKey() {
		return encKey;
	}

	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}

	public String getEncUserId() {
		return encUserId;
	}

	public void setEncUserId(String encUserId) {
		this.encUserId = encUserId;
	}

	public Map<String, FileAccessMeta> getZipFileSubMetaMap() {
		return zipFileSubMetaMap;
	}

	public void setZipFileSubMetaMap(Map<String, FileAccessMeta> zipFileSubMetaMap) {
		this.zipFileSubMetaMap = zipFileSubMetaMap;
	}

	@Override
	public String toString() {
		return "FileAccessMeta [_id=" + _id + ", fileName=" + fileName + ", uploadUserId=" + uploadUserId
				+ ", lastModifyUser=" + lastModifyUser + ", folderType=" + folderType + ", shareFolder=" + shareFolder
				+ ", bizShareFolder=" + bizShareFolder + ", memo=" + memo + ", favorite=" + favorite + ", lock=" + lock
				+ ", uploadDate=" + uploadDate + ", fileVersion=" + fileVersion + ", publicAccess=" + publicAccess
				+ ", fileUniqueKey=" + fileUniqueKey + ", bizId=" + bizId + ", fileSize=" + fileSize
				+ ", thumbNailPath=" + thumbNailPath + ", tags=" + tags + ", streamUniqueKey=" + streamUniqueKey
				+ ", createDate=" + createDate + ", parentFileUniqueKey=" + parentFileUniqueKey + ", systemPath="
				+ systemPath + ", level=" + level + ", lockDate=" + lockDate + ", ownerUserId=" + ownerUserId
				+ ", encFileSize=" + encFileSize + ", encYn=" + encYn + ", encDate=" + encDate + ", decDate=" + decDate
				+ ", hashData=" + hashData + ", modifyUserList=" + modifyUserList + ", orgnFileName=" + orgnFileName
				+ ", encKey=" + encKey + ", encUserId=" + encUserId + ", zipFileSubMetaMap=" + zipFileSubMetaMap + "]";
	}

}
