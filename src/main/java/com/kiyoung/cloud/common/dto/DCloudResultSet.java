package com.kiyoung.cloud.common.dto;

import java.util.List;

public class DCloudResultSet {

	private String resultCode;
	private String serverMsg;
	private String totalCount;
	private List list;

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getServerMsg() {
		return serverMsg;
	}

	public void setServerMsg(String serverMsg) {
		this.serverMsg = serverMsg;
	}

	public String toString() {
		return "{resultCode: " + resultCode + ", serverMsg: " + serverMsg + ", list: " + list;
	}
}
