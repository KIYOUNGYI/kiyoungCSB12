package com.kiyoung.cloud.storage.dto;

public class SizeMeta {

	private String _id;
	private String userId;
	private long used = 0L;
	private long wasteUsed = 0L;
	private long versionUsed = 0L;
	private String bizId;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public long getWasteUsed() {
		return wasteUsed;
	}

	public void setWasteUsed(long wasteUsed) {
		this.wasteUsed = wasteUsed;
	}

	public long getVersionUsed() {
		return versionUsed;
	}

	public void setVersionUsed(long versionUsed) {
		this.versionUsed = versionUsed;
	}

	@Override
	public String toString() {
		return "SizeMeta [_id=" + _id + ", userId=" + userId + ", used=" + used + ", bizId=" + bizId + ", wasteUsed="
				+ wasteUsed + ", versionUsed=" + versionUsed + "]";
	}
}
