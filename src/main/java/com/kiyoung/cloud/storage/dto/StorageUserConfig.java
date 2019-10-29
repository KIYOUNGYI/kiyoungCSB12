package com.kiyoung.cloud.storage.dto;

import java.io.Serializable;

public class StorageUserConfig {

//	private static final long serialVersionUID = 8134299358125716069L;
	private long size = 0;
	private boolean useYn = false;
	private boolean upload = true;
	private boolean download = true;
	private boolean read = true;
	private boolean write = true;
	private int wasteAutoDeleteDay = 60;
	private boolean versionUse = true;
	private String date = "";

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isUseYn() {
		return useYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public int getWasteAutoDeleteDay() {
		return wasteAutoDeleteDay;
	}

	public void setWasteAutoDeleteDay(int wasteAutoDeleteDay) {
		this.wasteAutoDeleteDay = wasteAutoDeleteDay;
	}

	public boolean isVersionUse() {
		return versionUse;
	}

	public void setVersionUse(boolean versionUse) {
		this.versionUse = versionUse;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "{size:" + size + ", " + "useYn:" + useYn + ", " + "upload:" + upload + ", " + "download:" + download
				+ "read:" + read + "write:" + write + "date:" + date + "wasteAutoDeleteDay:" + wasteAutoDeleteDay + "}";
	}
}
