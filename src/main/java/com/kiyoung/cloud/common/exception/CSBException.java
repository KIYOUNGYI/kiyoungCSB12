package com.kiyoung.cloud.common.exception;



import com.kiyoung.cloud.common.constants.CommonConst;

/**
 * CSBException
 * 
 * @author Kiyoung
 * @since 2019.10.16
 * @version 1.0
 */
public class CSBException extends BaseException {

	private static final long serialVersionUID = -458854076450500260L;

	public CSBException(String errorMessage) {
		super(CommonConst.ERROR_CODE_CSB_DEFAULT, new String[] { errorMessage });
	}
	
	public CSBException(String[] errorMessage) {
		super(CommonConst.ERROR_CODE_CUSTOM, errorMessage);
	}

	public CSBException(String errorCode, String[] errorMessage) {
		super(errorCode, errorMessage);
	}
}
