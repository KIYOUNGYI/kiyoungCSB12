package com.kiyoung.cloud.storage.type;

import java.io.Serializable;

public class AccessTypeNew implements Serializable {

	private static final long serialVersionUID = -6330799442554979233L;

	private boolean read;
	private boolean write;
	private boolean upload;
	private boolean download;
	private boolean ignore;

	public AccessTypeNew() {

	}

	public AccessTypeNew(boolean read, boolean write, boolean upload, boolean download, boolean ignore) {
		this.read = read;
		this.write = write;
		this.upload = upload;
		this.download = download;
		this.ignore = ignore;
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

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	@Override
	public String toString() {
		return "{read: " + read + ", write: " + write + ", upload: " + upload + ", download: " + download + ", ignore: "
				+ ignore + "}";
	}
}
