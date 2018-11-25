package com.kiyoung.cloud.common.dto;

public class BCBResponse {
    private int resultCode = 200;
    private String resultMsg = "";
    private String resultData = "";

    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public String getResultMsg() {
        return resultMsg;
    }
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
    public String getResultData() {
        return resultData;
    }
    public void setResultData(String resultData) {
        this.resultData = resultData;
    }


}
