package com.kiyoung.cloud.common.exception;

/**
 * CSBException 예외처리 클래스
 */
public class CSBException extends BaseException {

    /**
     * CSBException 생성자
     * @param errorCode
     * @param errorMessage
     * baseException 생성자 활용
     */
    public CSBException(String errorCode, String[] errorMessage) {
        super(errorCode, errorMessage);
    }

}
