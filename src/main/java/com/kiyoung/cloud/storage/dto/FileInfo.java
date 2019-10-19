package com.kiyoung.cloud.storage.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kiyoung.cloud.storage.type.AccessTypeNew;
import com.google.gson.annotations.Expose;

public class FileInfo {
	private boolean directory;
	private String fileName;
	private long size;
	private String mimeType;
	private String path;
	private String lastModifyDate;
	private String lastModifyUser;
	private String uploadDate;
	private String uploadUser;
	private boolean shareFolder = false;
	private boolean bizShareFolder = false;
	private boolean memo = false;
	private boolean favorite = false;
	private boolean lock = false;
	private int fileVersion = 0;
	private String folderType;
	private AccessTypeNew accessType;
	private String shareType;
	private String bizId;
	private String contents;
	private String thumbNailPath;
	private String streamUniqueKey;
	private String createDate;
	private int level;
	private String lockDate;
	private String sharedDate;
	private String masterID;
	private int folderCount;
	private int fileCount;
	private String encDate;
	private String encUserId;

	@JsonIgnore
	@Expose
	private String realPath;

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
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

	public int getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(int fileVersion) {
		this.fileVersion = fileVersion;
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

	public AccessTypeNew getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessTypeNew accessType) {
		this.accessType = accessType;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getThumbNailPath() {
		return thumbNailPath;
	}

	public void setThumbNailPath(String thumbNailPath) {
		this.thumbNailPath = thumbNailPath;
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

	public String getSharedDate() {
		return sharedDate;
	}

	public void setSharedDate(String sharedDate) {
		this.sharedDate = sharedDate;
	}

	public String getMasterID() {
		return masterID;
	}

	public void setMasterID(String masterID) {
		this.masterID = masterID;
	}

	public int getFolderCount() {
		return folderCount;
	}

	public void setFolderCount(int folderCount) {
		this.folderCount = folderCount;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public String getEncDate() {
		return encDate;
	}

	public void setEncDate(String encDate) {
		this.encDate = encDate;
	}

	public String getEncUserId() {
		return encUserId;
	}

	public void setEncUserId(String encUserId) {
		this.encUserId = encUserId;
	}

	@Override
	public String toString() {
		return "FileInfo [directory=" + directory + ", fileName=" + fileName + ", size=" + size + ", mimeType="
				+ mimeType + ", path=" + path + ", lastModifyDate=" + lastModifyDate + ", lastModifyUser="
				+ lastModifyUser + ", uploadDate=" + uploadDate + ", uploadUser=" + uploadUser + ", shareFolder="
				+ shareFolder + ", bizShareFolder=" + bizShareFolder + ", memo=" + memo + ", favorite=" + favorite
				+ ", lock=" + lock + ", fileVersion=" + fileVersion + ", folderType=" + folderType + ", accessType="
				+ accessType + ", shareType=" + shareType + ", bizId=" + bizId + ", contents=" + contents
				+ ", thumbNailPath=" + thumbNailPath + ", streamUniqueKey=" + streamUniqueKey + ", createDate="
				+ createDate + ", level=" + level + ", lockDate=" + lockDate + ", sharedDate=" + sharedDate
				+ ", masterID=" + masterID + ", folderCount=" + folderCount + ", fileCount=" + fileCount + ", encDate="
				+ encDate + ", encUserId=" + encUserId + ", realPath=" + realPath + "]";
	}
}
