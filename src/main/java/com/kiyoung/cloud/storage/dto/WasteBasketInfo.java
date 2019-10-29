package com.kiyoung.cloud.storage.dto;

import java.io.Serializable;

public class WasteBasketInfo implements Serializable{

	private static final long serialVersionUID = 4769174607352071246L;
	
	
	private String _id;
	private String filePath; //pk 랜덤키(6자리)_파일이름.jpg or 랜덤키(6자리)_폴더이름
	private String oldFilePath; //복원할때 쓰일 path와 네임
	private String fileName; //파일이름
	private String ownerUserAccount;
	private long size;
	private String removeDate;
	private String lastModifyDate;
	private String uploadDate;
	private String uploadUser;
	private String folderType;
	private String fileUniqueKey;
	private boolean directory;
	private String mimeType;
	private String streamUniqueKey;
	private String thumbNailPath;
	private String userId;
	private String bizId;
	private String shareFolderName;
	private String encDate;
	private String encUserId;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
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
	public String getFolderType() {
		return folderType;
	}
	public void setFolderType(String folderType) {
		this.folderType = folderType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getOwnerUserAccount() {
		return ownerUserAccount;
	}
	public void setOwnerUserAccount(String ownerUserAccount) {
		this.ownerUserAccount = ownerUserAccount;
	}
	public String getOldFilePath() {
		return oldFilePath;
	}
	public void setOldFilePath(String oldFilePath) {
		this.oldFilePath = oldFilePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRemoveDate() {
		return removeDate;
	}
	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getFileUniqueKey() {
		return fileUniqueKey;
	}
	public void setFileUniqueKey(String fileUniqueKey) {
		this.fileUniqueKey = fileUniqueKey;
	}
	public boolean isDirectory() {
		return directory;
	}
	public void setDirectory(boolean directory) {
		this.directory = directory;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getStreamUniqueKey() {
		return streamUniqueKey;
	}
	public void setStreamUniqueKey(String streamUniqueKey) {
		this.streamUniqueKey = streamUniqueKey;
	}
	public String getThumbNailPath() {
		return thumbNailPath;
	}
	public void setThumbNailPath(String thumbNailPath) {
		this.thumbNailPath = thumbNailPath;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getShareFolderName() {
		return shareFolderName;
	}
	public void setShareFolderName(String shareFolderName) {
		this.shareFolderName = shareFolderName;
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
	public String toString(){
		return "{filePath: " + filePath + ", oldFilePath: " + oldFilePath  + ", fileName:" + fileName 
				+ ", ownerUserAccount: " + ownerUserAccount + ", removeDate: " + removeDate + ", directory: " + directory
				+ ", size: " + size + ", lastModifyDate: " + lastModifyDate + ", uploadUser: " + uploadUser + ", mimeType: " + mimeType
				+ ", folderType: " + folderType + ", uploadDate: " + uploadDate + ", fileUniqueKey: " + fileUniqueKey + ", streamUniqueKey :  " + streamUniqueKey 
				+ ", thumbNailPath: " + thumbNailPath + ", shareFolderName: " + shareFolderName + ", bizId: " + bizId
				+ ", encDate: " + encDate + ", encUserId: " + encUserId + "}";
	}
}
