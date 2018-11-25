package com.kiyoung.cloud.common.exception;

/**
 *
 */
public class BaseException extends Exception {


//    private static final long serialVersionUID = -4947153609981184353L;

    final static String DEFAULT_ERROR_CODE = "9999";

    private String errorCode;

    private String[] errorMessage;

    private String debuggingMessage;

    /**
     * BaseException 생성자
     * @param errorMessage
     */
    public BaseException(String errorMessage) {
        super("ERROR CODE >> " + DEFAULT_ERROR_CODE + " ERROR MESSAGE >> " + errorMessage);
        this.errorCode = DEFAULT_ERROR_CODE;
        this.errorMessage = new String[] { errorMessage };
    }

    /**
     * BaseException 생성자
     * @param errorCode
     * @param errorMessage
     */
    public BaseException(String errorCode, String[] errorMessage) {
        super("ERROR CODE >> " + errorCode + " ERROR MESSAGE >> " + stringArrayToString(errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


    /**
     * 에러코드,에러메시지,디버깅메시지 겟셋 메소드
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String[] errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDebuggingMessage() {
        return debuggingMessage;
    }

    public BaseException setDebuggingMessage(String debuggingMessage) {
        this.debuggingMessage = debuggingMessage;
        return this;
    }

    /**
     * string 배열을 string 으로 합치는 것.
     *
     * @param messages
     * @return
     */
    private static String stringArrayToString(String[] messages) {
        StringBuffer sb = new StringBuffer();
        if (messages != null) {
            for (String msg : messages) {
                sb.append(msg + " ");
            }
        }
        return sb.toString();
    }

}
