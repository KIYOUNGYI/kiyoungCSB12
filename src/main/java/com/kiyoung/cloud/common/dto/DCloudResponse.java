package com.kiyoung.cloud.common.dto;

import java.util.List;

import com.kiyoung.cloud.common.constants.CloudConstants;

public class DCloudResponse<T> {
	private String serverMsg; // success, error
	private String resultCode;
	private String errorMessage;
	private String totalCount;
	private List<T> resultList;
	
	public String getServerMsg() {
		return serverMsg;
	}

	public void setServerMsg(String serverMsg) {
		this.serverMsg = serverMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	public boolean isSuccess() {
		if (CloudConstants.SERVICE_SUCCESS.equals(serverMsg))
			return true;
		return false;
	}
	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	
}