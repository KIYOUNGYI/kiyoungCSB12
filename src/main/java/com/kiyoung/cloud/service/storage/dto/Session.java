package com.kiyoung.cloud.service.storage.dto;

public class Session {
	private String company_no; // 회사번
	private String employee_no; // 사원번호 
	private String businessconnectid; // 회사 uuid
	private String userconnectid; // 사원 uuid
	private String id; // id 
	private String domain; // domain
	private String name; //  이름 
	private String email; // 메일
//	private boolean isEmployee; // BCB 회사 소속사용자
	private boolean isNasStorage; // BCB 스마트스퀘어 사용자
	private boolean isObjectStorage; // BCB 스마트스퀘어 사용자
	private String user_no; // 사용자 정보 시퀀스
	private String oauthTokenId; // 사용자 정보 시퀀스
	
	
	public String getCompany_no() {
		return company_no;
	}
	public void setCompany_no(String company_no) {
		this.company_no = company_no;
	}
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public String getBusinessConnectID() {
		return businessconnectid;
	}
	public void setBusinessConnectID(String businessconnectid) {
		this.businessconnectid = businessconnectid;
	}
	public String getUserConnectID() {
		return userconnectid;
	}
	public void setUserConnectID(String userconnectid) {
		this.userconnectid = userconnectid;
	}
	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getName() {
			return name;
		}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUser_no() {
		return user_no;
	}
	public boolean isNasStorage() {
		return isNasStorage;
	}
	public void setNasStorage(boolean isNasStorage) {
		this.isNasStorage = isNasStorage;
	}
	public void setUser_no(String user_no) {
		this.user_no = user_no;
	}
	public boolean isObjectStorage() {
		return isObjectStorage;
	}
	public void setObjectStorage(boolean isObjectStorage) {
		this.isObjectStorage = isObjectStorage;
	}
	public String getOauthTokenId() {
		return oauthTokenId;
	}
	public void setOauthTokenId(String oauthTokenId) {
		this.oauthTokenId = oauthTokenId;
	}
	@Override
	public String toString() {
		return "BCB Storage Session [company_no=" + company_no + ", employee_no="
				+ employee_no + ", businessconnectid=" + businessconnectid
				+ ", userconnectid=" + userconnectid + ", id=" + id
				+ ", domain=" + domain + ", isNasStorage=" + isNasStorage + ", isObjectStorage=" + isObjectStorage + ", oauthTokenId=" + oauthTokenId + "]";
	}
}
