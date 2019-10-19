package com.kiyoung.cloud.storage.dto;

public class UserAndBusinessConnectIdDto {

	private String userConnectId;
	private String businessConnectId;

	public String getUserConnectId() {
		return userConnectId;
	}

	public void setUserConnectId(String userConnectId) {
		this.userConnectId = userConnectId;
	}

	public String getBusinessConnectId() {
		return businessConnectId;
	}

	public void setBusinessConnectId(String businessConnectId) {
		this.businessConnectId = businessConnectId;
	}

	@Override
	public String toString() {
		return "UserAndBusinessConnectIdDto [userConnectId=" + userConnectId + ", businessConnectId="
				+ businessConnectId + "]";
	}
	
	
}
