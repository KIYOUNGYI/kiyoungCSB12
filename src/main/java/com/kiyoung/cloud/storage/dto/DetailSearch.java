package com.kiyoung.cloud.storage.dto;

public class DetailSearch {
	private String searchText;
	private String searchLocahtionPath;
	private String mimeType;
	private String startDate;
	private String endDate;
	private String startSize;
	private String endSize;
	
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getSearchLocahtionPath() {
		return searchLocahtionPath;
	}
	public void setSearchLocahtionPath(String searchLocahtionPath) {
		this.searchLocahtionPath = searchLocahtionPath;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartSize() {
		return startSize;
	}
	public void setStartSize(String startSize) {
		this.startSize = startSize;
	}
	public String getEndSize() {
		return endSize;
	}
	public void setEndSize(String endSize) {
		this.endSize = endSize;
	}
	@Override
	public String toString() {
		return "DetailSearch [searchText=" + searchText + ", searchLocahtionPath=" + searchLocahtionPath + ", mimeType="
				+ mimeType + ", startDate=" + startDate + ", endDate=" + endDate + ", startSize=" + startSize
				+ ", endSize=" + endSize + "]";
	}
	
	
}
