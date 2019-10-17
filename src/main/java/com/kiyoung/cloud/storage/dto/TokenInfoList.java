package com.kiyoung.cloud.storage.dto;

import java.util.HashMap;
import java.util.List;

public class TokenInfoList {

	private String _id;
	private String portal_ID;
	private List<String> company_noList;
	private HashMap<String, HashMap<String, String>> tokenIDInfo;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getPortal_ID() {
		return portal_ID;
	}

	public void setPortal_ID(String portal_ID) {
		this.portal_ID = portal_ID;
	}

	public List<String> getCompany_noList() {
		return company_noList;
	}

	public void setCompany_noList(List<String> company_noList) {
		this.company_noList = company_noList;
	}

	public HashMap<String, HashMap<String, String>> getTokenIDInfo() {
		return tokenIDInfo;
	}

	public void setTokenIDInfo(HashMap<String, HashMap<String, String>> tokenIDInfo) {
		this.tokenIDInfo = tokenIDInfo;
	}

	@Override
	public String toString() {
		return "TokenInfoList [_id=" + _id + ", portal_ID=" + portal_ID + ", company_noList=" + company_noList
				+ ", tokenIDInfo=" + tokenIDInfo + "]";
	}

}
